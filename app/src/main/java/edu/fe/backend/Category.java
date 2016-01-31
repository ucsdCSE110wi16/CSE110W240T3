package edu.fe.backend;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by david on 1/29/2016.
 */
@ParseClassName("Category")
public class Category extends ParseObject {
    public Category() {

    }

    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        put("name", name);
    }

    public String getDescription() {
        return getString("description");
    }

    public void setDescription(String description) {
        put("description", description);
    }
}
