package edu.fe.util;

import android.graphics.Bitmap;

public class FoodItem {

    public boolean food;
    public boolean generic;
    public boolean card;

    private Bitmap mImage;
    private CharSequence mName;
    private CharSequence experationDate;

    public Bitmap getImage() {
        return mImage;
    }

    public void setImage(Bitmap mImage) {
        this.mImage = mImage;
    }

    public CharSequence getName() {
        return mName;
    }

    public void name(CharSequence text) {
        this.mName = text;
    }

    public boolean isFood() {
        return food;
    }

    public boolean isGeneric() {
        return generic;
    }

    public boolean isCard() {
        return card;
    }
}

