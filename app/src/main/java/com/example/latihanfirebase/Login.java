package com.example.latihanfirebase;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity {

    private EditText email, password;
    private Button login;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        email = (EditText) findViewById(R.id.input_email);
        password = (EditText) findViewById(R.id.input_password);
        login = (Button) findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading(true);
                String em = email.getText().toString();
                String pass = password.getText().toString();
                auth.signInWithEmailAndPassword(em, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(Login.this, MainActivity.class));
                        } else {
                            loading(false);
                            Toast.makeText(getApplicationContext(), "gagal:" + task.getException() , Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    private void loading(boolean stat) {
        if (stat) {
            findViewById(R.id.overlay).setVisibility(View.VISIBLE);
            findViewById(R.id.loading_login).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.overlay).setVisibility(View.GONE);
            findViewById(R.id.loading_login).setVisibility(View.GONE);
        }
    }
}
