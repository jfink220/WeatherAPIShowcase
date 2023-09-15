package com.example.weatherapishowcase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void getData(View view) {
        String URL = "https://api.open-meteo.com/v1/forecast?latitude" +
                "=42.1103&longitude=-88.0342&hourly=temperature_2m" +
                "&temperature_unit=fahrenheit&forecast_days=1";
        Request req = new Request.Builder().url(URL).build();
        TextView weatherTxt = findViewById(R.id.WeatherText);
        client.newCall(req).enqueue(new Callback() {
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String JSONData = response.body().string();
                            ObjectMapper OM = new ObjectMapper();
                            JsonNode JN = OM.readTree(JSONData);
                            String temp = JN.get("hourly").get("temperature_2m").get
                                    (JN.get("hourly").get("temperature_2m").size() - 1).toString();
                            String time = JN.get("hourly").get("time").get(JN.get("hourly").get
                                    ("time").size() - 1).toString();
                            weatherTxt.setText("It is forecasted to be " + temp + " fahrenheit" +
                                    " on " + time);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        });
    }
}