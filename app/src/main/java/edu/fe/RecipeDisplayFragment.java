package edu.fe;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import edu.fe.backend.Recipe;
import edu.fe.widget.LoadingDialog;

/**
 * Created by wongk on 3/9/2016.
 */
public class RecipeDisplayFragment extends Fragment {
    private RecipePageParsingTask mRecipePageParsingTask;
    private boolean running = false;
    private String pageUrl;
    private ProgressDialog mProgressDialog;

    public RecipeDisplayFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_display, container, false);
        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        RecipePageParsingTask parser = new RecipePageParsingTask(getActivity(), view);
        pageUrl = getArguments().getString("key");
        parser.execute(pageUrl);
        return view;
    }

    private class RecipePageParsingTask extends AsyncTask<String, String, Recipe.Display> {
        private Context mContext;
        private View mView;

        public RecipePageParsingTask(Context context, View v) {
            mContext = context;
            mView = v;
        }

        @Override
        public void onPreExecute() {
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setMessage("Loading Recipe...");
            mProgressDialog.setCancelable(true);
            mProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mRecipePageParsingTask.cancel(true);
                    dialog.dismiss();
                }
            });
            mProgressDialog.show();
        }
        @Override
        public Recipe.Display doInBackground(String... params) {
            String pageTitle;
            String description = "Description:\n";
            String details = "Details:\n";
            String imgUrl;
            String ingredients = "Ingredients:\n";
            String instructions = "Instructions:\n";
            Document doc;
            try {
                doc = Jsoup.connect(params[0]).get();
            }
            catch(IOException e) {
                Log.d("DEBUG", "IOException thrown in RecipePageParsingTask");
                e.printStackTrace();
                return null;
            }
            Element titleElm = doc.select("h1").first();
            pageTitle = titleElm.text() + "\n";

            Element imgElm = doc.select("img[class=recipe-img]").first();



            Elements detElms = doc.select("tr");
            for(Element detElm : detElms) {
                details += detElm.text() + "\n";
            }

            Element descElm = doc.select("p[style=margin-bottom: 20px;]").first();
            description += descElm.text() + "\n";
            Elements ingElms = doc.select("p[style=margin-bottom: 20px;]").last().select("p");
            for(Element ingElm : ingElms) {
                ingredients += ingElm.text() + "\n";
            }
            Element possibleIngElem = doc.select("ul[id=zlrecipe-ingredients-list").first();
            if(possibleIngElem != null) {
                Elements liElms = possibleIngElem.select("li[class=ingredient]");
                for(Element liElm : liElms) {
                    ingredients += liElm.text() + "\n";
                }
            }
            Element possibleInstrElm = doc.select("ul[id=zlrecipe-instructions-list").first();
            if(possibleIngElem != null) {
                Elements liElms = possibleInstrElm.select("li[class=instruction]");
                for(Element liElm : liElms) {
                    instructions += liElm.text() + "\n";
                }
            }

            Element orderedList = doc.select("ol").first();
            if(orderedList != null) {
                Elements liElms = orderedList.select("li");
                for(Element liElm : liElms) {
                    instructions += liElm.text() + "\n";
                }
            }

            Elements listElms = doc.select("ul");
            for(Element listElm : listElms) {
                if(listElm.className().equals("")) {
                    Elements liElms = listElm.select("li");
                    if(ingredients.equals("Ingredients:\n\n")) {
                        for(Element liElm : liElms) {
                            ingredients += liElm.text() + "\n";
                        }
                    }
                    else if(instructions.equals("Instructions:\n") &&
                            listElm == listElms.last()) {
                        for(Element liElm : liElms) {
                            instructions += liElm.text() + "\n";
                        }
                    }
                }
                else if(listElm.className().equals("zlrecipe-ingredients-list")) {
                    Elements liElms = listElm.select("li[class=ingredient]");
                    for(Element liElm : liElms) {
                        ingredients += liElm.text() + "\n";
                    }
                }
                else if(listElm.className().equals("zlrecipe-instructions-list")) {
                    Elements liElms = listElm.select("li[class=instruction]");
                    for(Element liElm : liElms) {
                        instructions += liElm.text() + "\n";
                    }
                }
            }

            Element instrElm = doc.select("p[style=margin-bottom: 10px;]").first();
            Elements directions = instrElm.select("ul li");
            /*
            if(directions.size() != 0) {
                int i = 1;
                for(Element direction : directions) {
                    instructions += i++ + ". " + direction.text() + "\n";
                }
            }
            else {
                directions = instrElm.select("ol li");
                if(directions.size() != 0) {
                    int i = 1;
                    for(Element direction : directions) {
                        instructions += i++ + ". " + direction.text() + "\n";
                    }
                }
            }
            directions = instrElm.select("p");
            for(Element direction : directions) {
                instructions += direction.text() + "\n";
            }
            instructions += instrElm.text();
            */
            instructions += instrElm.html();
            instructions = instructions.replace("<br>", "\n");

            Recipe.Display retDisplay = new Recipe.Display(pageTitle, description, details,
                                                           ingredients, instructions);
            return retDisplay;
        }

        public void onPostExecute(Recipe.Display disp) {
            if(disp != null) {
                TextView title = (TextView) mView.findViewById(R.id.txtTitle);
                title.setText(disp.pageTitle);
                TextView description = (TextView) mView.findViewById(R.id.txtDesc);
                description.setText(disp.description);
                TextView ingredients = (TextView) mView.findViewById(R.id.txtIngredients);
                ingredients.setText(disp.ingredients);
                TextView details = (TextView) mView.findViewById(R.id.txtDetails);
                details.setText(disp.details);
                TextView instructions = (TextView) mView.findViewById(R.id.txtInstructions);
                instructions.setText(disp.instructions);
            }
            mProgressDialog.dismiss();
        }
    }

}
