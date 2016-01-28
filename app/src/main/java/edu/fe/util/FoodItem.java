package edu.fe.util;

import android.graphics.Bitmap;

public class FoodItem {

    public boolean food;
    public boolean generic;
    public boolean card;

    private Bitmap mImage;
    private CharSequence mH1;
    private CharSequence mH2;
    private CharSequence mH3;

    public Bitmap getImage() {
        return mImage;
    }

    public void setImage(Bitmap mImage) {
        this.mImage = mImage;
    }

    public CharSequence getHeaderText() {
        return mH1;
    }

    public CharSequence getHeaderText2() {
        return mH2;
    }

    public CharSequence getHeaderText3() {
        return mH3;
    }

    public void setHeader(CharSequence text) {
        this.mH1 = text;
    }

    public void setHeader2(CharSequence text) {
        this.mH2 = text;
    }

    public void setHeader3(CharSequence text) {
        this.mH3 = text;
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

