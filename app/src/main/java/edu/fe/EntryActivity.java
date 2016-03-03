package edu.fe;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseQuery;
import com.vorph.anim.AnimUtils;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import edu.fe.backend.Category;
import edu.fe.backend.FoodItem;
import lib.material.dialogs.MaterialDialog;
import lib.material.dialogs.Theme;
import lib.material.picker.date.DatePickerDialog;

public class EntryActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int RESULT_FAIL = RESULT_FIRST_USER;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int ACTION_TAKE_PHOTO_B = 1;
    private static final String TAG = EntryActivity.class.getSimpleName();

    private ImageView mImageView;
    private String mCurrentPhotoPath;

    Button mCategoryButton;
    AppCompatImageButton mDateButton;
    AppCompatImageButton mCameraButton;
    EditText mNameField;
    EditText mQuantityField;
    TextView mDateText;
    TextView mSelectThumbnailText;

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

        mImageView = (ImageView) findViewById(R.id.entry_thumbnail);

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
    }

    final DatePickerDialog.OnDateSetListener mOnDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePickerDialog.DateAttributeSet set) {
            mSelectedDate.setDate(set.day);
            mSelectedDate.setMonth(set.month + 1);
            mSelectedDate.setYear(set.year);
            mDateText.setText(DateFormat.getDateInstance().format(mSelectedDate));
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
                    .items(R.array.category_array)
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
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.entry_submit) {
            if (mNameField.getText().toString().isEmpty()) {
                invalidField("must enter food name.");
                return false;
            }
            if (mQuantityField.getText().toString().isEmpty()) {
                invalidField("must enter quantity.");
                return false;
            }

            FoodItem foodItem = new FoodItem();
            if(mQuantityField.getText().toString().trim().length() >0) {
                foodItem.setQuantity(Integer.parseInt(mQuantityField.getText().toString()));
            }

            // TODO still need to set image and category
//            Bitmap bitmap = ((BitmapDrawable) mImageView.getDrawable()).getBitmap();
//            if (null != bitmap) {
//
//            }

            ParseQuery<Category> q = ParseQuery.getQuery(Category.class);
            q.fromLocalDatastore();
            q.whereEqualTo(Category.NAME, mCategoryButton.getText());
            try {
                Category c = q.getFirst();
                foodItem.setCategory(c);
                foodItem.setName(mNameField.getText().toString());
                foodItem.pinInBackground();
                foodItem.saveEventually();
                setResult(RESULT_OK);
            } catch (ParseException e) {
                e.printStackTrace();
                setResult(RESULT_FAIL);
            }

            finish();
            return true;
        }
        else if (item.getItemId() == android.R.id.home) {
            // When the back-arrow button is pressed in toolbar, finish
            // the activity and go back to the previous one.
            finish();
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
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
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
            mCurrentPhotoPath = null;
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
