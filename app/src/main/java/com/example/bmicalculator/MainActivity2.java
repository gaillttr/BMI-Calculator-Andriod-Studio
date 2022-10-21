package com.example.bmicalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity2 extends AppCompatActivity {

    private TextView history_res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        BmiResult();

        history_res = (TextView) findViewById(R.id.history_res);
        history_res.setText(CreateHistory());

    }

    private String CreateHistory() {
        String bmiResult = BmiResult();

        return bmiResult.replace("|", "       ").replace(";", "\n");
    }

    private String BmiResult() {
        SharedPreferences sp = getSharedPreferences("bmi", Context.MODE_PRIVATE);
        return  sp.getString("bmi", "error");

    }

}
