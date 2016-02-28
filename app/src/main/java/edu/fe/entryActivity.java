package edu.fe;

import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import edu.fe.backend.Category;
import edu.fe.backend.FoodItem;
import lib.material.picker.date.DatePickerDialog;
import lib.material.util.TypefaceHelper;

public class entryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        setTitle("Add an item");


        final Spinner spinner = (Spinner) findViewById(R.id.categorySpinner);
        final EditText nameField = (EditText) findViewById(R.id.itemEditText);
        final EditText quantityField = (EditText) findViewById(R.id.quantityAmtEditText);
        final TextView dateField = (TextView) findViewById(R.id.expirationDateTextView);
        final Button sbtBtn = (Button) findViewById(R.id.sbtBtn);
        final Button cncBtn = (Button) findViewById(R.id.cancelBtn);
        String currentDateTimeString = DateFormat.getDateInstance().format(new Date());
        dateField.setText(currentDateTimeString);
        dateField.setTypeface(null, Typeface.BOLD);

        final SpinAdapter adapter = new SpinAdapter(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        final DatePickerDialog.OnDateSetListener onDateSetListener =
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog.DateAttributeSet set) {
                        String date = String.format("%d/%d/%d", set.month + 1, set.day, set.year);
                        dateField.setText(date);
                    }
                };

        dateField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog.Builder(entryActivity.this)
                        .listener(onDateSetListener)
                        .setCalendar(Calendar.getInstance())
                        .show();
            }
        });

        sbtBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FoodItem f = new FoodItem();
                Category c = adapter.getCategory(spinner.getSelectedItemPosition());
                f.setCategory(c);
                f.setName(nameField.getText().toString());
                f.pinInBackground();
                f.saveEventually();
                finish();
            }
        });

        cncBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
