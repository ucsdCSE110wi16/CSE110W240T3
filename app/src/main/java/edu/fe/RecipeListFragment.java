package edu.fe;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Fragment;
import android.os.AsyncTask;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import android.app.ProgressDialog;

import edu.fe.backend.FoodItem;
import edu.fe.backend.RecipeFinder;
import edu.fe.backend.RecipeItem;
import java.io.IOException;
import java.util.List;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class RecipeListFragment extends Fragment {
    RecyclerView recyclerView;

    private RecipeItemRecyclerAdapter mAdapter;


    public RecipeListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);
            if (view instanceof RecyclerView) {
                recyclerView = (RecyclerView) view;
            }
            new AsyncRecipeFinder(recyclerView).execute();

        return view;
    }

    private class AsyncRecipeFinder extends AsyncTask<Integer, String, RecipeItem[]> {
        ProgressDialog progressDialog;
        RecyclerView mRecyclerView;
        protected AsyncRecipeFinder(RecyclerView recyclerView) {
            mRecyclerView = recyclerView;
        }
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(getActivity(), "Loading Recipes",
                    "Please be patient...");
        }
        protected RecipeItem[] doInBackground(Integer... params) {
            RecipeItem[] recipeItems = null;
            List<FoodItem> foodList;
            String cookieString = "";
            ParseObject.registerSubclass(FoodItem.class);
            ParseQuery<FoodItem> query = new ParseQuery<FoodItem>(FoodItem.class);
            query.fromLocalDatastore();
            try {
                foodList = query.find();
            }
            catch(com.parse.ParseException p) {
                Log.d("DEBUG", "ParseException thrown");
                foodList = null;
            }
            if (foodList != null) {
                String[] names = new String[foodList.size()];
                int nameIndex = 0;
                //populate cookie with valid ingredient IDs
                for (FoodItem item : foodList) {
                    names[nameIndex++] = item.getName();
                }
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
                for(String name : names) {
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


                Log.d("DEBUG", "cookieString: " + cookieString);

                String url, name, cookingTime, missingIngredients; //params for recipeitem
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
                    String[] detHTMLArray = detHTML.split("<br>"); //details are separated by line breaks
                    cookingTime = detHTMLArray[1];

                    //parse the missing ingredients
                    Element missIngs = recipe.select("div[style=margin:0 0 20px;]").first();
                    String missing = missIngs.text();

                    if (!missing.equals("No Missing Ingredients")) {
                        missing = "";
                        Elements missingIngs = missIngs.getElementsByTag("p");
                        for (Element missingIng : missingIngs) {
                            if (missing.equals("")) {
                                missing += "Missing ingredients: " + missingIng.text().substring(3);
                            } else {
                                missing += ", " + missingIng.text().substring(3);
                            }
                        }
                    }

                    missingIngredients = missing;
                    recipeItems[index++] = new RecipeItem(name, missingIngredients, cookingTime, url);
                }
            }
            return recipeItems;
        }

        protected void onPostExecute(RecipeItem[] recipeItems) {
            progressDialog.cancel();
            mRecyclerView.setAdapter(new RecipeItemRecyclerAdapter(recipeItems));
        }

    }


}