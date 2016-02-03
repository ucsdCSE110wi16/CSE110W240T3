package edu.fe.util;

import android.graphics.Bitmap;
import java.util.Calendar;

@Deprecated
public class FoodItem {

    private boolean food;
    private boolean generic;
    private boolean card;
  //  private boolean hasVolume = false;
   // private double volume;
    private int quantity;
    private Calendar expDate;
    private Bitmap mImage;
    private CharSequence mH1;
    private CharSequence mH2;
    private CharSequence mH3;

    public FoodItem(CharSequence h1, CharSequence h2, CharSequence h3, Bitmap image,
                    Calendar exp, int quant) {
        mH1 = h1;
        mH2 = h2;
        mH3 = h3;
        mImage = image;
        quantity = quant;
        expDate = exp;
    }

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

    //Manage quantity of item
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int q) {
        quantity = q;
    }

    public void decQuantity() {
        if(quantity > 0) { //can't have negative quantities
            quantity--;
        }
    }

    public void incQuantity() {
        quantity++;
    }

    //returns the expiration date in a Calendar object
    public Calendar getExpDate() {
        return expDate;
    }

    //returns a comparison of the expiration date with the current date
    public int timeUntilExp() {
        Calendar now = Calendar.getInstance();
        return expDate.compareTo(now);
    }

    //manage volume of item
   /* public double getVolume() {
        if(hasVolume) { //always make sure it has volume
            return volume;
        }
        else {
            return -1.0; //invalid
        }
    }

    public void setVolume(double vol) {
        if(hasVolume) {
            volume = vol;
        }
    }


    public void decVolume() {
        if(hasVolume) {
            if(volume == 0) {
                if(quantity > 0) { //decrement quantity and reset volume
                    decQuantity();
                    volume = 1.0;
                }
            }
            volume -= 0.25; //decrement by quarters
        }
    } */



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

