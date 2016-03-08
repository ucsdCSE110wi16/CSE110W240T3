package edu.fe.backend;
/**
 * Created by wongk on 2/25/2016.
 */
@Deprecated
public class RecipeItem {
    private String name;
    private String missingIngredients;
    private String url;
    private String cookingTime = "";

    public RecipeItem(String nm, String missIng, String cTime,
                      String addr) {
        name = nm;
        missingIngredients = missIng;
        cookingTime = cTime;
        url = addr;
    }

    public String getName() {
        return name;
    }


    public String getMissingIngredients() {
        return missingIngredients;
    }

    public String getUrl() {
        return url;
    }

    public String getCookingTime() {
        return cookingTime;
    }

}
