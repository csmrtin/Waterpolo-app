package com.example.waterpolo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText emailInput, passwordInput;
    Button loginBtn, goToRegisterBtn;
    FirebaseAuth auth;
    private static final String PREFS_NAME = "WaterpoloPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        emailInput = findViewById(R.id.email);
        passwordInput = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginBtn);
        goToRegisterBtn = findViewById(R.id.goToRegisterBtn);

        loginBtn.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            // Check for empty inputs
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Sikertelen Bejelentkezés: Kérjük töltse ki mindkét mezőt!", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener(result -> {
                        // Save login status
                        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                        prefs.edit().putBoolean(KEY_IS_LOGGED_IN, true).apply();

                        startActivity(new Intent(this, MainActivity.class));
                        finish();
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Sikertelen Bejelentkezés: Hibás email vagy jelszó!", Toast.LENGTH_SHORT).show()
                    );
        });

        goToRegisterBtn.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class))
        );
    }
}
