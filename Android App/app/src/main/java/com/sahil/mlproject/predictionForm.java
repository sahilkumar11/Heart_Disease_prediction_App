package com.sahil.mlproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.slider.Slider;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class predictionForm extends AppCompatActivity {

    RadioGroup radioGroup;
    Button proceed_btn;
    Slider age_sl,heart_rate_slider,cholestrol_slider,blood_pr_sl;
    String age,gender ,max_heart_rate,cholesterol_level,blood_pressure,chest_pain;
    ImageView men,women;
    Boolean result = false;
    String url = "https://ml-project-sahil-2k19co331.herokuapp.com/predict";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediction_form);
        initialize();
        gender_fn();
        slider_fn();
        //createDialog();
        proceed_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    // hitting the api here
                ApiCall();
            }
        });

    }

    void initialize(){
        men = findViewById(R.id.iv_man);
        women = findViewById(R.id.iv_woman);
        radioGroup  = findViewById(R.id.rg_chest_pain);
        proceed_btn = findViewById(R.id.btn_start_analysis);
        age_sl = findViewById(R.id.ms_age_slider);
        blood_pr_sl = findViewById(R.id.ms_blood_pressure_slider);
        heart_rate_slider = findViewById(R.id.ms_heart_rate_slider);
        cholestrol_slider  = findViewById(R.id.ms_cholesterol_slider);
    }

    void gender_fn(){
        men.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                men.setVisibility(View.VISIBLE);
                men.setImageResource(R.drawable.ic_man);
                women.setImageResource(R.drawable.ic_blank);
                gender = "0";
            }
        });

        women.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                women.setVisibility(View.VISIBLE);
                women.setImageResource(R.drawable.ic_woman);
                men.setImageResource(R.drawable.ic_blank);
                gender = "1";
//                Log.d("sssss", "ddd   " + gender  + " " + age);
            }
        });
    }

    void slider_fn(){
        age_sl.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull @NotNull Slider slider, float value, boolean fromUser) {
//                Log.d("sssss","dd" +  value);
                age = Float.toString(value);
            }
        });

        blood_pr_sl.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull @NotNull Slider slider, float value, boolean fromUser) {
//                Log.d("sssss","dd" +  value);
                blood_pressure = Float.toString(value);
            }
        });

        heart_rate_slider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull @NotNull Slider slider, float value, boolean fromUser) {
//                Log.d("sssss","dd" +  value);
                max_heart_rate = Float.toString(value);
            }
        });

        cholestrol_slider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull @NotNull Slider slider, float value, boolean fromUser) {
//                Log.d("sssss","dd" +  value);
                cholesterol_level = Float.toString(value);
            }
        });
    }
    public void onRadioButtonClicked(View view) {
            // Is the button now checked?
            boolean checked = ((RadioButton) view).isChecked();

            // Check which radio button was clicked
            switch(view.getId()) {
                case R.id.rb_asymptomatic:
                    if (checked)
                        chest_pain ="3";
                        // Pirates are the best
                        Toast.makeText(predictionForm.this, "sdf", Toast.LENGTH_SHORT).show();
                        break;
                case R.id.rb_atypical_angina:
                    if (checked)
                        chest_pain = "1";
                        // Ninjas rule
                        Toast.makeText(predictionForm.this, "ssdsdd", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.rb_non_anginal:
                    if(checked)
                        chest_pain = "2";
                    break;
                case R.id.rb_typical_angina:
                    if(checked)
                        chest_pain = "0";
                    break;
            }
    }

    void ApiCall(){
        // hit the API -> Volley
//        createDialog();
        StringRequest stringRequest = new StringRequest( Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
//                            Toast.makeText(predictionForm.this, "1", Toast.LENGTH_SHORT).show();
                            JSONObject jsonObject = new JSONObject(response);
                            String data = jsonObject.getString("prediction");
                            if(data.equals("1")){
                                result = true;
                                Toast.makeText(predictionForm.this, "2", Toast.LENGTH_SHORT).show();
                                createDialog("1");
                            }else{
                                result = false;
                                Toast.makeText(predictionForm.this, "3", Toast.LENGTH_SHORT).show();
                                createDialog("0");
                            }
                            Log.d("ssss","ss" + response);
//                            createDialog();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(predictionForm.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }){

            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String,String>();
                params.put("age",age);
                params.put("gender",gender);
                params.put("chest_pain",chest_pain);
                params.put("blood_pressure",blood_pressure);
                params.put("cholesterol_level",cholesterol_level);
                params.put("max_heart_rate",max_heart_rate);

                return params;
            }

        };
        RequestQueue queue = Volley.newRequestQueue(predictionForm.this);
        queue.add(stringRequest);
    }

    @SuppressLint("ResourceAsColor")
    void createDialog(String ans){
        String res = "";
        Log.d("ssss", res);
        if(ans.equals("0")){
            res = "YOU ARE SAFE";
        }
        else{
            res = "You have risk of heart disease";
        }
        new AlertDialog.Builder(predictionForm.this)
                .setTitle("Prediction result")
                .setMessage(res)

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }
}