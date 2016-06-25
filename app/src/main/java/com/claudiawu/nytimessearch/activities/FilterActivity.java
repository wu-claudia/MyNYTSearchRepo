package com.claudiawu.nytimessearch.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.claudiawu.nytimessearch.R;
import com.claudiawu.nytimessearch.models.Filter;

import org.parceler.Parcels;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FilterActivity extends AppCompatActivity {

    //@BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.checkbox_arts) CheckBox check_arts;
    @BindView(R.id.checkbox_fashion) CheckBox check_fashion;
    @BindView(R.id.checkbox_sports) CheckBox check_sports;
    @BindView(R.id.button) Button submit;
    @BindView(R.id.editDate) EditText datePicker;
    @BindView(R.id.mySpinner) Spinner spinner;
    int mYear;
    int mMonth;
    int mDay;
    boolean isArts = false;
    boolean isSports = false;
    boolean isFashion = false;
    String date;
    String month;
    String day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        //datePicker = (EditText) findViewById(R.id.editDate);
        //spinner = (Spinner) findViewById(R.id.mySpinner);


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
                        if (selectedmonth < 10) {
                            month = "0" + String.valueOf(selectedmonth);
                        } else {
                            month = String.valueOf(selectedmonth);
                        }
                        if (selectedday < 10) {
                            day = "0" + String.valueOf(selectedday);
                        } else {
                            day = String.valueOf(selectedday);
                        }
                        date = selectedyear + month + day;
                    }
                },mYear, mMonth, mDay);
                mDatePicker.setTitle("Select date");
                mDatePicker.show();  }
        });
    }

    public void onSubmit(View v) {
        String spinnerVal = spinner.getSelectedItem().toString();

        Filter filter = new Filter();
        filter.setArt(isArts);
        filter.setArt(isFashion);
        filter.setArt(isSports);
        filter.setArray_news_desk(isArts,isFashion,isSports);
        filter.setDate(date);
        filter.setSpinnerVal(spinnerVal);

        Intent data = new Intent();
        data.putExtra("filter", Parcels.wrap(filter));
        //startActivity(data);
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
                    isArts=true;
                    //Toast.makeText(getApplicationContext(),"Chose arts!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.checkbox_fashion:
                if (checked) {
                    isFashion=true;
                    //Toast.makeText(getApplicationContext(),"Chose fashion!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.checkbox_sports:
                if (checked) {
                    isSports=true;
                    //Toast.makeText(getApplicationContext(),"Chose sports!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
