package lib.material;

import android.content.Context;

import lib.material.dialogs.MaterialDialog;

public class Material {
    public static MaterialDialog.Builder Dialog(Context context) {
        return new MaterialDialog.Builder(context);
    }
}
