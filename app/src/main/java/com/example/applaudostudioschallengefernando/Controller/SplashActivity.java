package com.example.applaudostudioschallengefernando.Controller;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import static java.lang.Thread.sleep;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(this, MainActivityController.class);
        startActivity(intent);
        finish();
    }
}