package edu.fe.backend;
import java.net.URL;
import java.io.IOException;
/**
 * Created by wongk on 2/25/2016.
 */
public class RecipeItem {
    private String name;
    private String missingIngredients;
    private URL url;
    private String cookingTime = "";

    public RecipeItem(String nm, String missIng, String cTime,
                      String addr) throws IOException{
        name = nm;
        missingIngredients = missIng;
        cookingTime = cTime;
        url = new URL(addr);
    }

    public String getName() {
        return name;
    }


    public String getMissingIngredients() {
        return missingIngredients;
    }

    public URL getUrl() {
        return url;
    }

    public String getCookingTime() {
        return cookingTime;
    }

}
