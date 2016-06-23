package com.claudiawu.nytimessearch.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;

import com.claudiawu.nytimessearch.R;
import com.claudiawu.nytimessearch.models.Filter;

import java.util.Calendar;

import butterknife.BindView;

public class FilterActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.checkbox_arts) CheckBox check_arts;
    @BindView(R.id.checkbox_fashion) CheckBox check_fashion;
    @BindView(R.id.checkbox_sports) CheckBox check_sports;
    @BindView(R.id.button)Button submit;
    EditText datePicker;
    int mYear;
    int mMonth;
    int mDay;
    boolean isArts = false;
    boolean isSports = false;
    boolean isFashion = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        //ButterKnife.bind(this);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        datePicker = (EditText) findViewById(R.id.editDate);

        datePicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar mcurrentDate=Calendar.getInstance();
                mYear=mcurrentDate.get(Calendar.YEAR);
                mMonth=mcurrentDate.get(Calendar.MONTH);
                mDay=mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker=new DatePickerDialog(FilterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        datePicker.setText(selectedmonth + "/" + selectedday + "/" + selectedyear);
                    }
                },mYear, mMonth, mDay);
                mDatePicker.setTitle("Select date");
                mDatePicker.show();  }
        });
    }

    public void onSubmit(View v) {
        Intent data = new Intent();
        Filter filter = new Filter();
        data.putExtra("filter", filter);
        setResult(RESULT_OK, data);
        finish();
    }



    // When checkbox is clicked
    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkbox_arts:
                if (checked) {
                    isArts = true;
                }
                break;
            case R.id.checkbox_fashion:
                if (checked) {
                    isFashion = true;
                }
                break;
            case R.id.checkbox_sports:
                if (checked) {
                    isSports = true;
                }
                break;
        }
    }
}
