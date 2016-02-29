package edu.fe.backend;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;
import java.io.IOException;

import com.parse.ParseException;
import com.parse.ParseQuery;

public class RecipeFinder {

    /* Parses the full page of ingredients and adds each valid inserted ingredient to the cookie,
     * then uses the cookie to access the recipe search page,
     * which is then parsed for all recipes on the page, whose relevant info is stored in
     * RecipeItems, which are returned in an array of the size of the number of recipes on the page.
     */
    public static RecipeItem[] getRecipes(int pageNum) throws ParseException, IOException {

        String url, name, missingIngredients, cookingTime; //parameters for RecipeItem
        RecipeItem[] recipeItems;
        int count = 0;

        List<FoodItem> foodList; //for the parse query
        String cookieString = "";
        ParseQuery<FoodItem> query = new ParseQuery<FoodItem>(FoodItem.class);
        foodList = query.find();

        Document doc = Jsoup.connect("http://www.myfridgefoods.com/?detailed=true").get();
        Elements ings = doc.select("div[class=tile ingredient]");

        //populate cookie with valid ingredient IDs
        for(FoodItem item: foodList) {
            for(Element ing : ings) {
                String ingName = ing.getElementsByClass("ingredient-checkbox-label").first().text();
                if(item.getName().equals(ingName)) {
                    String ingID = ing.getElementsByTag("input").first().attr("value");
                    if(cookieString.equals("")) {
                        cookieString += ingID;
                    }
                    else {
                        cookieString += "." + ingID;
                    }
                    break;
                }
            }
        }

        //open the corresponding page
        Document recipePage = Jsoup.connect("http://myfridgefood.com/search-by-ingredients?page=" + pageNum)
                                   .cookie("_ingredients", cookieString).get();
        Elements recipes = recipePage.select("div[class=tile recipe]");

        recipeItems = new RecipeItem[recipes.size()];
        int index = 0;

        for(Element recipe : recipes) { //iterate through all recipes, parse all relevant info

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

            if(!missing.equals("No Missing Ingredients")) {
                missing = "";
                Elements missingIngs = missIngs.getElementsByTag("p");
                for(Element missingIng : missingIngs) {
                    if(missing.equals("")) {
                        missing += missingIng.text().substring(3);
                    }
                    else {
                        missing += ", " + missingIng.text().substring(3);
                    }
                }
            }
            
            missingIngredients = missing;
            recipeItems[index++] = new RecipeItem(name, missingIngredients, cookingTime, url);
        }

        return recipeItems;
    }
}