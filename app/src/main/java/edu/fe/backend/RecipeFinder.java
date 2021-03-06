package edu.fe.backend;

import android.os.AsyncTask;
import android.util.Log;

import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

@Deprecated
public class RecipeFinder {

    /* Parses the full page of ingredients and adds each valid inserted ingredient to the cookie,
     * then uses the cookie to access the recipe search page,
     * which is then parsed for all recipes on the page, whose relevant info is stored in
     * RecipeItems, which are returned in an array of the size of the number of recipes on the page.
     */
 //   private static String cString = "";
   // private static RecipeItem[] recList;
    private class FoodParsingTask extends AsyncTask<String, String, String> {

        @Override
        public String doInBackground(String... params) {
            String cookieString = "";
            Document doc;

            try {
                doc = Jsoup.connect("http://myfridgefood.com/?detailed=true").get();
            }
            catch(Exception e) {
                Log.d("DEBUG", "Exception thrown");
                doc = null;
                e.printStackTrace();
            }
            Elements ings = doc.select("div[class=tile ingredient]");
            for(String name : params) {
                for (Element ing : ings) {
                    String ingName = ing.getElementsByClass("ingredient-checkbox-label").first().text();
                    if(name.equals(ingName)) {
                        String ingID = ing.getElementsByTag("input").first().attr("value");
                        if (cookieString.equals("")) {
                            cookieString += ingID;
                        } else {
                            cookieString += "." + ingID;
                        }
                        break;
                    }
                }
            }
            return cookieString;

        }
/*
        protected void onPostExecute(String result) {
            cString = result;
        }
        */
    }

    private class RecipeParsingTask extends AsyncTask<String, String, RecipeItem[]> {

        public RecipeItem[] doInBackground(String... params) {
            String url, name, cookingTime, missingIngredients;
            String cookieString = params[0];
            RecipeItem[] recipeItems;
            Document recipePage;
            try {
                recipePage = Jsoup.connect("http://myfridgefood.com/search-by-ingredients?page=" + 1)
                        .cookie("_ingredients", cookieString).get();
            }
            catch(IOException io) {
                recipePage = null;
            }
            Elements recipes = recipePage.select("div[class=tile recipe]");

            recipeItems = new RecipeItem[recipes.size()];
            int index = 0;

            for (Element recipe : recipes) { //iterate through all recipes, parse all relevant info

                Element elm = recipe.select("a").first(); //parse the url
                url = elm.attr("abs:href");

                Element nameElm = elm.select("img").first(); //parse the name of the recipe
                name = nameElm.attr("alt");

                //parse desired details (currently just the cooking time)
                Element detElm = recipe.select("div[class=line-item-details]").first().select("p").first();
                String detHTML = detElm.html().replace("<strong>", "");
                detHTML = detHTML.replace("</strong>", "");
                String[] detHTMLArray = detHTML.split("<br>");
                cookingTime = detHTMLArray[1];

                //parse the missing ingredients
                Element missIngs = recipe.select("div[style=margin:0 0 20px;]").first();
                String missing = missIngs.text();

                if (!missing.equals("No Missing Ingredients")) {
                    missing = "";
                    Elements missingIngs = missIngs.getElementsByTag("p");
                    for (Element missingIng : missingIngs) {
                        if (missing.equals("")) {
                            missing += missingIng.text().substring(3);
                        } else {
                            missing += ", " + missingIng.text().substring(3);
                        }
                    }
                }

                missingIngredients = missing;
                recipeItems[index++] = new RecipeItem(name, missingIngredients, cookingTime, url);
            }
            Log.d("DEBUG", "Recipe name: " + recipes.size());

            return recipeItems;
        }
/*
        protected void onPostExecute(RecipeItem[] result) {
            recList = result;
        }
        */
    }

    public RecipeItem[] getRecipes(int pageNum) throws IOException{



        String url, name, missingIngredients, cookingTime; //parameters for RecipeItem
        RecipeItem[] recipeItems = null;
        //final ArrayList<FoodItem> foodList = new ArrayList<FoodItem>();
        List<FoodItem> foodList;
        String cookieString = "";
        ParseObject.registerSubclass(FoodItem.class);
        ParseQuery<FoodItem> query = new ParseQuery<FoodItem>(FoodItem.class);
        query.fromLocalDatastore();
            /*query.findInBackground(new FindCallback<FoodItem>() {
                public void done(List<FoodItem> foodItems, ParseException e ) {
                    if(e == null) {
                        for(FoodItem foodItem : foodItems) {
                            foodList.add(foodItem);
                        }
                        Log.d("DEBUG", "ParseException not thrown");
                    }
                    else {
                        Log.d("DEBUG", "ParseException thrown");
                    }
                }
            }); */

        try {
            foodList = query.find();
            Log.d("DEBUG", "size of foodList:" + foodList.size());
        }
        catch(com.parse.ParseException p) {
            Log.d("DEBUG", "ParseException thrown");
            foodList = null;
        }
        Log.d("DEBUG", "Outside of try/catch block");
   /*     Document doc = null;
        Elements ings = null;
        try {
            doc = Jsoup.connect("http://myfridgefood.com/?detailed=true").get();
        }
        catch(Exception e) {
            Log.d("DEBUG", "Some exception thrown");
        }
        ings = doc.select("div[class=tile ingredient]");
        Log.d("DEBUG", "foodList == null: " + (foodList == null)); */
            if (foodList != null) {
                String[] names = new String[foodList.size()];
                int nameIndex = 0;
                //populate cookie with valid ingredient IDs
                for (FoodItem item : foodList) {
                    names[nameIndex++] = item.getName();

                   /* if (item != null) {
                        for (Element ing : ings) {
                            String ingName = ing.getElementsByClass("ingredient-checkbox-label").first().text();
                            if (item.getName().equals(ingName)) {
                                String ingID = ing.getElementsByTag("input").first().attr("value");
                                if (cookieString.equals("")) {
                                    cookieString += ingID;
                                } else {
                                    cookieString += "." + ingID;
                                }
                                break;
                            }
                        }
                    } */
                }
                FoodParsingTask foodParse = new FoodParsingTask();
                try {
                   cookieString = foodParse.execute(names).get();
                } catch (Exception e) {
                    e.printStackTrace();
                }


                Log.d("DEBUG", "cookieString: " + cookieString);
                //open the corresponding page
         /*       Document recipePage = Jsoup.connect("http://myfridgefood.com/search-by-ingredients?page=" + pageNum)
                        .cookie("_ingredients", cookieString).get();
            if (recipePage != null) {
                Elements recipes = recipePage.select("div[class=tile recipe]");

                recipeItems = new RecipeItem[recipes.size()];
                int index = 0;

                for (Element recipe : recipes) { //iterate through all recipes, parse all relevant info

                    Element elm = recipe.select("a").first(); //parse the url
                    url = elm.attr("abs:href");

                    Element nameElm = elm.select("img").first(); //parse the name of the recipe
                    name = nameElm.attr("alt");

                    //parse desired details (currently just the cooking time)
                    Element detElm = recipe.select("div[class=line-item-details]").first().select("p").first();
                    String detHTML = detElm.html().replace("<strong>", "");
                    detHTML = detHTML.replace("</strong>", "");
                    String[] detHTMLArray = detHTML.split("<br>");
                    cookingTime = detHTMLArray[1];

                    //parse the missing ingredients
                    Element missIngs = recipe.select("div[style=margin:0 0 20px;]").first();
                    String missing = missIngs.text();

                    if (!missing.equals("No Missing Ingredients")) {
                        missing = "";
                        Elements missingIngs = missIngs.getElementsByTag("p");
                        for (Element missingIng : missingIngs) {
                            if (missing.equals("")) {
                                missing += missingIng.text().substring(3);
                            } else {
                                missing += ", " + missingIng.text().substring(3);
                            }
                        }
                    }

                    missingIngredients = missing;
                    recipeItems[index++] = new RecipeItem(name, missingIngredients, cookingTime, url);
                }
            } else {
                recipeItems = null;
            } */
                RecipeParsingTask recipeParse = new RecipeParsingTask();
                try {
                    recipeItems = recipeParse.execute(cookieString).get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        return recipeItems;
    }
}