package edu.fe;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageButton;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import lib.material.picker.date.DatePickerDialog;

public class EntryActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int ACTION_TAKE_PHOTO_B = 1;
    private static final String TAG = EntryActivity.class.getSimpleName();

    private ImageButton mImageView;
    private String mCurrentPhotoPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_entry);
        setTitle("Add an item");

//        final Spinner spinner = (Spinner) findViewById(R.id.categorySpinner);
//        final EditText nameField = (EditText) findViewById(R.id.itemEditText);
//        final EditText quantityField = (EditText) findViewById(R.id.quantityAmtEditText);
//        final TextView dateField = (TextView) findViewById(R.id.expirationDateTextView);
//        mImageView = (ImageButton) findViewById(R.id.foodImageView);
//        String currentDateTimeString = DateFormat.getDateInstance().format(new Date());
//        dateField.setText(currentDateTimeString);
//        dateField.setTypeface(null, Typeface.BOLD);

//        Toolbar mToolbar = (Toolbar) findViewById(R.id.entryToolbar);
//        setSupportActionBar(mToolbar);
//        mToolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

//        mImageView.setImageResource(R.drawable.ic_menu_camera);

//        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });

//        final SpinAdapter adapter = new SpinAdapter(this, R.layout.custom_spinner);
//        adapter.setDropDownViewResource(R.layout.custom_dropdown_item);
//        spinner.setAdapter(adapter);

//        mImageView.getLayoutParams().height = 800;
//        mImageView.getLayoutParams().width = 600;

        final DatePickerDialog.OnDateSetListener onDateSetListener =
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog.DateAttributeSet set) {
                        String date = String.format("%d/%d/%d", set.month + 1, set.day, set.year);
//                        dateField.setText(date);
                    }
                };

//        dateField.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new DatePickerDialog.Builder(EntryActivity.this)
//                        .listener(onDateSetListener)
//                        .setCalendar(Calendar.getInstance())
//                        .show();
//            }
//        });

//        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.submitEntry:
//                        FoodItem f = new FoodItem();
//                        Category c = adapter.getCategory(spinner.getSelectedItemPosition());
//                        if( quantityField.getText().toString().trim().length() >0) {
//                            f.setQuantity(Integer.parseInt(quantityField.getText().toString()));
//                        }
//                        f.setCategory(c);
//                        f.setName(nameField.getText().toString());
//                        f.pinInBackground();
//                        f.saveEventually();
//                        finish();
//                        return true;
//                }
//                return false;
//            }
//        });

//        mImageView.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                dispatchTakePictureIntent(ACTION_TAKE_PHOTO_B);
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_entry, menu);
        return super.onCreateOptionsMenu(menu);
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
                        f = null;
                        mCurrentPhotoPath = null;
                    }
                    break;

                default:
                    break;
            } // switch

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
            setPic();
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

    private void setPic() {

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
