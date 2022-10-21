package com.example.bmicalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.DataFormatException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BmiResult();
        setContentView(R.layout.activity_main);

        final EditText edWeg,edHei;
        final TextView status_result,bmi_result;
        Button button;
        ImageButton imageButton;

        edWeg=(EditText) findViewById(R.id.edweg);
        edWeg.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(8, 2)});
        edHei= (EditText) findViewById(R.id.edhei);
        edHei.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(8, 2)});

        bmi_result=(TextView) findViewById(R.id.bmi_result);
        status_result=(TextView) findViewById(R.id.status_result);

        button= (Button) findViewById(R.id.button);
        imageButton = (ImageButton) findViewById(R.id.imageButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String strweg= edWeg.getText().toString();
                String strhei= edHei.getText().toString();

                if(strweg.equals("")){
                    edWeg.setError("Please Enter Your Weight ");
                    edWeg.requestFocus();
                    return;
                }
                if(strhei.equals("")){
                    edHei.setError("Please Enter Your Height");
                    edHei.requestFocus();
                    return;
                }

                float weight = Float.parseFloat(strweg);
                float height = Float.parseFloat(strhei)/100;

                float bmiValue = BMICalculate(weight,height);

                bmi_result.setText(""+ bmiValue);
                status_result.setText(interpreteBMI(bmiValue));

                saveHistory(weight, bmiValue, interpreteBMI(bmiValue));
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        final TextView bmi = (TextView) findViewById(R.id.bmi);
        bmi.setTextSize(newConfig.fontScale*32);
        final TextView status = (TextView) findViewById(R.id.status);
        status.setTextSize(newConfig.fontScale*32);
        final TextView status_res = (TextView) findViewById(R.id.status_result);
        status_res.setTextSize(newConfig.fontScale*32);
        final TextView bmi_res = (TextView) findViewById(R.id.bmi_result);
        bmi_res.setTextSize(newConfig.fontScale*32);
        final TextView weight = (TextView) findViewById(R.id.weightvar);
        weight.setTextSize(newConfig.fontScale*32);
        final TextView height = (TextView) findViewById(R.id.heightvar);
        height.setTextSize(newConfig.fontScale*32);
        final TextView title = (TextView) findViewById(R.id.title_text);
        title.setTextSize(newConfig.fontScale*32);
        final Button buttonCal = (Button) findViewById(R.id.button);
        buttonCal.setTextSize(newConfig.fontScale*32);
    }

    public float BMICalculate(float weight,float height){
        float result = weight / (height * height);
        return Float.parseFloat(new DecimalFormat("##.##").format(result));
    }

    public String interpreteBMI(float bmiValue){
        String[] my_status = getResources().getStringArray(R.array.status_result);

        if(bmiValue <18.5){
            return my_status[0];
        }
        else if(bmiValue > 18.5 || bmiValue < 22.9){
            return my_status[1];
        }
        else if(bmiValue > 23 || bmiValue < 24.9){
            return my_status[2];
        }
        else if(bmiValue > 25 || bmiValue < 29.9){
            return my_status[3];
        }
        else {
            return my_status[4];
        }
    }

    private String BmiResult() {
        SharedPreferences sp = getSharedPreferences("bmi", Context.MODE_PRIVATE);
        return  sp.getString("bmi", "error");
    }

    private void saveHistory(double weight, double bmi, String status) {

        DecimalFormat formatter = new DecimalFormat("#,###.##");

        SharedPreferences sp = getSharedPreferences("bmi", Context.MODE_PRIVATE);
        SharedPreferences.Editor spEdit = sp.edit();

        SimpleDateFormat date = new SimpleDateFormat("dd-MM-yyyy");
        String currentDate = date.format(new Date());

        String lastBmiResult = BmiResult();
        String bmiResult = currentDate + "|" + formatter.format(weight) + "|" + formatter.format(bmi) + "|" + status;

        if(! lastBmiResult.equals("error")) {
            bmiResult = lastBmiResult + ";" + bmiResult;
        }

        spEdit.putString("bmi", bmiResult);
        spEdit.commit();
    }
}

class DecimalDigitsInputFilter implements InputFilter {
    private Pattern mPattern;
    DecimalDigitsInputFilter(int digits, int digitsAfterZero) {
        mPattern = Pattern.compile("[0-9]{0," + (digits - 1) + "}+((\\.[0-9]{0," + (digitsAfterZero - 1) +
                "})?)||(\\.)?");
    }
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        Matcher matcher = mPattern.matcher(dest);
        if (!matcher.matches())
            return "";
        return null;
    }
}