package edu.fe.backend;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class Recipe {

    public static class Item {
        public String url;
        public String name;
        public String calories;
        public Bitmap image;
        public String missingIngredients;
        public String cookingTime = "";
        public String fat;

        public Item(String name,
                    String missingIngredients,
                    String cookingTime,
                    String address,
                    Bitmap img,
                    String cals,
                    String fat)
        {
            this.url = address;
            this.name = name;
            this.missingIngredients = missingIngredients;
            this.cookingTime = cookingTime;
            image = img;
            this.calories = cals;
            this.fat = fat;
        }
    }

    public static class Display {
        public String pageTitle;
        public String description;
        public String details;
        public String ingredients;
        public String instructions;

        public Display(String pgTitle,
                       String desc,
                       String dets,
                       String ings,
                       String instructs) {
            pageTitle = pgTitle;
            description = desc;
            details = dets;
            ingredients = ings;
            instructions = instructs;
        }
    }

    public static class Finder {
        public static class ParsingTask extends AsyncTask<String, Void, Void> {

            private static final String URI = "http://myfridgefood.com/?detailed=true";
            private OnRecipeRetrievedListener mOnRecipeRetrieveListener;
            private OnUpdateQueryListener mOnUpdateQueryListener;

            public ParsingTask setOnRecipeRetrieveListener(OnRecipeRetrievedListener listener) {
                this.mOnRecipeRetrieveListener = listener;
                return this;
            }

            public ParsingTask setOnUpdateQueryListener(OnUpdateQueryListener listener) {
                this.mOnUpdateQueryListener = listener;
                return this;
            }

            private String queryFoodIngredients(String[] ingredientNames) {
                StringBuilder cookieBuilder = new StringBuilder();
                Document document;

                try { document = Jsoup.connect(URI).get(); }
                catch (IOException e) {
                    Log.e("DEBUG", "IOException thrown in queryFoodIngredients", e);
                    return null;
                }

                Elements ingredients = document.select("div[class=tile ingredient]");
                for (String name : ingredientNames) {
                    for (Element ingredient : ingredients) {
                        String ingredientName =
                                ingredient
                                        .getElementsByClass("ingredient-checkbox-label")
                                        .first()
                                        .text();

                        if(name.equals(ingredientName)) {
                            String ingredientId = ingredient.getElementsByTag("input")
                                    .first()
                                    .attr("value");

                            if (cookieBuilder.toString().equals("")) {
                                cookieBuilder.append(ingredientId);
                            }
                            else {
                                cookieBuilder.append("." + ingredientId);
                            }
                            break;
                        }
                    }
                }

                return cookieBuilder.toString();
            }

            private Recipe.Item[] queryRecipes(String cookie) {
                Document recipeDocument;
                try {
                    recipeDocument =
                            Jsoup.connect("http://myfridgefood.com/search-by-ingredients?page=" + 1)
                                 .cookie("_ingredients", cookie)
                                 .get();
                }
                catch (IOException e) {
                    Log.e("DEBUG", "IOException thrown in queryRecipes", e);
                    return null;
                }

                Elements recipes = recipeDocument.select("div[class=tile recipe]");
                Recipe.Item[] recipeItems = new Recipe.Item[recipes.size()];

                int index = 0;
                for (Element recipe : recipes) {
                    //parse the url
                    Element element = recipe.select("a").first();
                    String uri = element.attr("abs:href");

                    //parse the name of the recipe
                    Element nameElm = element.select("img").first();
                    String name = nameElm.attr("alt");
                    URL imgUri;
                    Bitmap img;
                    try {
                        imgUri = new URL(nameElm.attr("abs:src"));
                        img = BitmapFactory.decodeStream(imgUri.openConnection().getInputStream());
                    }
                    catch(Exception e) {
                        e.printStackTrace();
                        img = null;
                    }


                    //parse desired details (currently just the cooking time)
                    Element detailElement = recipe.select("div[class=line-item-details]")
                                                  .first()
                                                  .select("p")
                                                  .first();
                    String htmlDetails = detailElement.html()
                                            .replace("<strong>", "")
                                            .replace("</strong>", "");
                    String[] htmlSplitDetails = htmlDetails.split("<br>");
                    String cookingTime = htmlSplitDetails[1];
                    String cals = htmlSplitDetails[2];
                    String fat = htmlSplitDetails[3];

                    //parse the missing ingredients
                    Element missingIngredients = recipe.select("div[style=margin:0 0 20px;]").first();
                    String missing = missingIngredients.text();

                    if (!missing.equals("No Missing Ingredients")) {
                        missing = "";
                        Elements missingIngs = missingIngredients.getElementsByTag("p");
                        for (Element missingIng : missingIngs) {
                            if (missing.equals("")) {
                                missing += "Missing Ingredients: " + missingIng.text().substring(3);
                            } else {
                                missing += ", " + missingIng.text().substring(3);
                            }
                        }
                    }

                    recipeItems[index++] = new Recipe.Item(name, missing, cookingTime, uri, img, cals, fat);
                }
                return recipeItems;
            }


            @Override
            protected Void doInBackground(String... ingredientNames){
                Log.d("DEBUG", "Querying food ingredients' cookie");
                String cookie = queryFoodIngredients(ingredientNames);
                if (cookie != null) {
                    Log.d("DEBUG", "Query for food ingredients' cookie passed");
                    Recipe.Item[] recipeItems = queryRecipes(cookie);
                    if (recipeItems == null) {
                        Log.d("DEBUG", "Invalid find of recipes");
                        return null;
                    }

                    Log.d("DEBUG", "Executing onRecipeRetrieve listener");
                    mOnRecipeRetrieveListener.onRecipeGet(recipeItems);
                }

                return null;
            }
        }


        Context mContext;
        OnRecipeRetrievedListener mRetrieveListener = new OnRecipeRetrievedListener() {
            @Override public void onRecipeGet(Item[] items) { }
        };
        OnUpdateQueryListener mUpdateListener = new OnUpdateQueryListener() {
            @Override public void onQueryUpdate(String s) { }
        };

        public static Finder of(Context context) {
            Finder finder = new Finder();
            finder.mContext = context;
            return finder;
        }

        public Finder setOnRecipeRetrieveListener(OnRecipeRetrievedListener listener) {
            this.mRetrieveListener = listener;
            return this;
        }

        public Finder setOnUpdateQueryListener(OnUpdateQueryListener listener) {
            this.mUpdateListener = listener;
            return this;
        }

        public ParsingTask search() {
            String message = "Searching for recipes.";
            mUpdateListener.onQueryUpdate(message);
            Log.d("DEBUG", message);

            List<FoodItem> foodItemList;
            ParseObject.registerSubclass(FoodItem.class);
            ParseQuery<FoodItem> query = new ParseQuery<>(FoodItem.class);

            try {
                foodItemList = query.fromLocalDatastore().find();
            }
            catch (ParseException e) {
                Log.e("DEBUG", "ParseException thrown in Recipe.Finder.search()", e);
                return null;
            }

            if (foodItemList != null) {
                Log.d("DEBUG", "Query was successful for getting food-items in background");
                int index = 0;
                int count = foodItemList.size();
                String[] names = new String[count];

                for (FoodItem item : foodItemList)
                    names[index++] = item.getName();

                ParsingTask task = new ParsingTask()
                                    .setOnRecipeRetrieveListener(mRetrieveListener)
                                    .setOnUpdateQueryListener(mUpdateListener);
                task.execute(names);
                return task;
            }

            return null;
        }

    }

    public interface OnUpdateQueryListener {
        void onQueryUpdate(String s);
    }

    public interface OnRecipeRetrievedListener {
        void onRecipeGet(Recipe.Item[] items);
    }

}
