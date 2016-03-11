package edu.fe;

import com.parse.Parse;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import edu.fe.backend.Category;

/**
 * Created by david on 3/10/2016.
 */
public class CategoryUnitTest {
    @Test
    public void testGetCategories() {
        List<Category> list = Category.getCategories();
        Assert.assertTrue(list.size() > 0);
        Category first = list.get(0);
        Assert.assertTrue(first.getName().length() > 0);
    }

    @Test
    public void testGetCategoryByName() {
        Category c = Category.getCategoryByName("Alcohol");
        Assert.assertNotNull(c);
        Assert.assertEquals(c.getName(), "Alcohol");
    }
}

