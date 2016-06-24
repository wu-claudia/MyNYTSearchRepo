package com.claudiawu.nytimessearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;

import com.claudiawu.nytimessearch.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends AppCompatActivity{

    @BindView(R.id.mySpinner) Spinner spinner;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
    }

    public void onSubmit(View v) {
        String spinnerVal = spinner.getSelectedItem().toString();
        int numCols = Integer.parseInt(spinnerVal);
        Log.d("numCols", String.valueOf(numCols));
        Intent data = new Intent();
        data.putExtra("setting", numCols);
        //startActivity(data);
        setResult(RESULT_OK, data);
        finish();
    }
}
