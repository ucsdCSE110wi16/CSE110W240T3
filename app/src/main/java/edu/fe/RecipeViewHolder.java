package edu.fe;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
/**
 * Created by wongk on 3/2/2016.
 */
public class RecipeViewHolder extends RecyclerView.ViewHolder {

    protected TextView vName;
    protected TextView vMissIngs;
    protected TextView vUrl;
    protected TextView vCookingTime;

    public RecipeViewHolder(View v) {
        super(v);
        vName = (TextView) v.findViewById(R.id.txtName);
        vMissIngs = (TextView) v.findViewById(R.id.txtMissingIngredients);
        vUrl = (TextView) v.findViewById(R.id.txtFat);
        vCookingTime = (TextView) v.findViewById(R.id.txtCookingTime);

    }
}
