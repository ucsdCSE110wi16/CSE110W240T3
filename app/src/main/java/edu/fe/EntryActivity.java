package edu.fe;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseQuery;
import com.vorph.anim.AnimUtils;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import bolts.Continuation;
import bolts.Task;
import edu.fe.backend.Category;
import edu.fe.backend.FoodItem;
import lib.material.dialogs.DialogAction;
import lib.material.dialogs.MaterialDialog;
import lib.material.dialogs.Theme;
import lib.material.picker.date.DatePickerDialog;

public class EntryActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String ITEM_CATEGORY_HINT = "item-category-hint";
    public static final String EDIT_ITEM_ID = "edit-item-id";
    public static final int RESULT_FAIL = RESULT_FIRST_USER;
    public static final int RESULT_DELETED = RESULT_FAIL + 1;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int ACTION_TAKE_PHOTO_B = 1;
    private static final String TAG = EntryActivity.class.getSimpleName();

    private ParseImageView mImageView;
    private String mCurrentPhotoPath;
    private FoodItem mFoodItem;

    Button mCategoryButton;
    AppCompatImageButton mDateButton;
    AppCompatImageButton mCameraButton;
    EditText mNameField;
    EditText mQuantityField;
    TextView mDateText;
    TextView mSelectThumbnailText;
    MenuItem mDeleteMenuItem;

    Date mSelectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        Log.d("DEBUG", "[Entry] Initializing entry");

        AnimUtils.init(this);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.entry_toolbar);
        mToolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Add an item");

        mImageView = (ParseImageView) findViewById(R.id.entry_thumbnail);

        // Set the current date to the Date Field.
        mSelectedDate = new Date();
        String currentDateTime = DateFormat.getDateInstance().format(new Date());
        mDateText = (TextView) findViewById(R.id.item_date_text);
        mDateText.setText(currentDateTime);
        mDateText.setTypeface(null, Typeface.BOLD);
        mSelectThumbnailText = (TextView) findViewById(R.id.item_thumbnail_text);

        mNameField = (EditText) findViewById(R.id.item_name_edit);
        mQuantityField = (EditText) findViewById(R.id.item_quantity_edit);

        mCategoryButton = (Button) findViewById(R.id.item_select_category);
        mCameraButton = (AppCompatImageButton) findViewById(R.id.item_camera_button);
        mDateButton = (AppCompatImageButton) findViewById(R.id.item_date_button);

        mCategoryButton.setOnClickListener(this);
        mCameraButton.setOnClickListener(this);
        mDateButton.setOnClickListener(this);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            String editId = extras.getString(EDIT_ITEM_ID);
            if(editId != null && !editId.isEmpty()) {
                // we are editing an object
                ParseQuery<FoodItem> q = ParseQuery.getQuery(FoodItem.class);
                q.fromLocalDatastore();
                try {
                    mFoodItem = q.get(editId);
                    mNameField.setText(mFoodItem.getName());
                    mQuantityField.setText(Integer.toString(mFoodItem.getQuantity()));
                    mSelectedDate = mFoodItem.getExpirationDate();
                    setDateText(mSelectedDate);
                    Category c = mFoodItem.getCategory();
                    if(c != null) {
                        mCategoryButton.setText(c.getName());
                    }
                    mImageView.setParseFile(mFoodItem.getImageLazy());
                    mImageView.loadInBackground();
                } catch (ParseException e) {
                    invalidField("Failed to load existing item for editing");
                }
            } else {
                String categoryHint = extras.getString(ITEM_CATEGORY_HINT);
                if(categoryHint != null && !categoryHint.isEmpty()) {
                    mCategoryButton.setText(categoryHint);
                }
            }
        }
    }

    private void setDateText(Date date) {
        if(mDateText != null && mSelectedDate != null) {
            DateFormat df = new SimpleDateFormat("MMM d, yyyy");
            mDateText.setText(df.format(date));
        }
    }

    final DatePickerDialog.OnDateSetListener mOnDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePickerDialog.DateAttributeSet set) {
            Calendar c = new GregorianCalendar(set.year, set.month, set.day);
            mSelectedDate = c.getTime();
            setDateText(mSelectedDate);
        }
    };

    // Handles all onClick requests:
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.item_date_button) {
            new DatePickerDialog.Builder(EntryActivity.this)
                    .listener(mOnDateSetListener)
                    .setCalendar(Calendar.getInstance())
                    .show();
        }
        else if (view.getId() == R.id.item_camera_button) {
            dispatchTakePictureIntent(ACTION_TAKE_PHOTO_B);
        }
        else if (view.getId() == R.id.item_select_category) {
            new MaterialDialog.Builder(EntryActivity.this)
                            .theme(Theme.LIGHT)
                            .items(Category.getCategoryNames())
                            .itemsCallback(new MaterialDialog.ListCallback() {
                                @Override
                                public void onSelection(MaterialDialog dialog,
                                                        View view,
                                                        int which,
                                                        CharSequence text) {
                                    mCategoryButton.setText(text);
                                }
                            }).show();
        }
        else if (view.getId() == R.id.entry_toolbar) {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_entry, menu);
        mDeleteMenuItem = menu.findItem(R.id.entry_delete);
        if(mFoodItem != null) {
            mDeleteMenuItem.setVisible(true);
        }
        return super.onCreateOptionsMenu(menu);
    }

    private boolean saveItem() throws ParseException {
        if (mNameField.getText().toString().isEmpty()) {
            invalidField("must enter food name.");
            return false;
        }
        if (mQuantityField.getText().toString().isEmpty()) {
            invalidField("must enter quantity.");
            return false;
        }

        final FoodItem foodItem = mFoodItem == null ? new FoodItem() : mFoodItem;
        if (mQuantityField.getText().toString().trim().length() >0) {
            foodItem.setQuantity(Integer.parseInt(mQuantityField.getText().toString()));
        }

        if(mSelectedDate != null) {
            foodItem.setExpirationDate(mSelectedDate);
        }

        if(mCategoryButton.getText().toString().isEmpty()
                || mCategoryButton.getText().toString().equalsIgnoreCase("SELECT A CATEGORY"))
        {
            invalidField("must enter a category.");
            return false;
        }

        Category c = Category.getCategoryByName(mCategoryButton.getText().toString());
        foodItem.setCategory(c);
        foodItem.setName(mNameField.getText().toString());

        if(mCurrentPhotoPath != null) {
            final MaterialDialog dlg = new MaterialDialog.Builder(this)
                    .title("Saving Item")
                    .content("Please Wait")
                    .progress(true, 0)
                    .build();
            dlg.show();
            final ParseFile image = FoodItem.createUnsavedImage(mCurrentPhotoPath);
            // this will happen after the save below happens. We don't want to block on image saving.
            image.saveInBackground().onSuccess(new Continuation<Void, Void>() {
                @Override
                public Void then(Task<Void> task) throws Exception {
                    foodItem.put(FoodItem.IMAGE, image);
                    foodItem.pinInBackground();
                    foodItem.saveEventually();
                    dlg.dismiss();
                    setResult(RESULT_OK);
                    finish();
                    return null;
                }
            });
            return true;
        } else {
            foodItem.pin();
            foodItem.saveEventually();
            setResult(RESULT_OK);
            finish();
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.entry_submit) {
            try {
                return saveItem();
            } catch (ParseException e) {
                setResult(RESULT_FAIL);
                finish();
                return true;
            }
        }
        else if (item.getItemId() == android.R.id.home) {
            // When the back-arrow button is pressed in toolbar, finish
            // the activity and go back to the previous one.
            finish();
        }
        else if(item.getItemId() == R.id.entry_delete) {
            new MaterialDialog.Builder(this)
                    .title(R.string.item_delete_confirmation)
                    .content(R.string.item_delete_content)
                    .positiveText(android.R.string.yes)
                    .negativeText(android.R.string.no)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(MaterialDialog dialog, DialogAction which) {
                            if(mFoodItem != null) {
                                try {
                                    mFoodItem.deleteEventually();
                                    mFoodItem.unpin();
                                    setResult(RESULT_DELETED);
                                    finish();
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                    setResult(RESULT_FAIL);
                                    finish();
                                }
                            }
                        }
                    }).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mImageView.getDrawable() != null) {
            Bitmap bitmap = ((BitmapDrawable) mImageView.getDrawable()).getBitmap();
            bitmap.recycle();
        }
    }

    private void invalidField(String s) {
        new MaterialDialog.Builder(this)
                .content("One or more missing fields: " + s)
                .title("Invalid Field")
                .positiveText("dismiss")
                .show();
    }

    private void dispatchTakePictureIntent(int actionCode) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            switch(actionCode) {
                case ACTION_TAKE_PHOTO_B:
                    File f = null;

                    try {
                        f = setUpPhotoFile();
                        mCurrentPhotoPath = f.getAbsolutePath();
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    } catch (IOException e) {
                        e.printStackTrace();
                        mCurrentPhotoPath = null;
                    }
                    break;

                default:
                    break;
            }

            startActivityForResult(takePictureIntent, actionCode);
        }
    }

    //This is where we can use the image data captured by the camera
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            //Bundle extras = data.getExtras();
            //Bitmap imageBitmap = (Bitmap) extras.get("data");
            //mImageView.setImageBitmap(imageBitmap);
            handleCameraPhoto();
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(null);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    private File setUpPhotoFile() throws IOException {
        File f = createImageFile();
        mCurrentPhotoPath = f.getAbsolutePath();
        return f;
    }

    private void handleCameraPhoto() {
        if (mCurrentPhotoPath != null) {
            setPicture();
            galleryAddPic();
        }
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void setPicture() {
        if (mSelectThumbnailText.getVisibility() == View.VISIBLE) {
            mSelectThumbnailText.setVisibility(View.GONE);
//            AnimUtils.fadeOut(mSelectThumbnailText);
        }

		/* There isn't enough memory to open up more than a couple camera photos */
		/* So pre-scale the target bitmap into which the file is decoded */

		/* Get the size of the ImageView */
        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();

        Log.d(TAG, "SIZE OF TARGET W" + targetW);
        Log.d(TAG, "SIZE OF TARGET H" + targetH);

		/* Get the size of the image */
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        Log.d(TAG, "PHOTOPATH" + mCurrentPhotoPath);

        Log.d(TAG, "SIZE OF PHOTO W" + photoW);
        Log.d(TAG, "SIZE OF PHOTO H" + photoH);

		/* Figure out which way needs to be reduced less */
        int scaleFactor = 1;
        if ((targetW > 0) || (targetH > 0)) {
            scaleFactor = Math.min(photoW/targetW, photoH/targetH);
        }

        Log.d(TAG, "VALUE OF SCALEFACTOR " + scaleFactor);

		/* Set bitmap options to scale the image decode target */
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

		/* Decode the JPEG file into a Bitmap */
        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

		/* Associate the Bitmap to the ImageView */
        mImageView.setImageBitmap(bitmap);
        mImageView.setVisibility(View.VISIBLE);
    }

}
