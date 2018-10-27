package com.example.latihanfirebase;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Awal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_awal);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.loading_awal).setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else {
                            startActivity(new Intent(getApplicationContext(), Login.class));
                        }
                    }
                }, 3000);
            }
        }, 2000);

    }
}
