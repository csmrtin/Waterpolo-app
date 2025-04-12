package com.example.waterpolo;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    EditText email, password, username, fullname, team, year;
    Button registerBtn, backBtn;
    FirebaseAuth auth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        username = findViewById(R.id.username);
        fullname = findViewById(R.id.fullname);
        team = findViewById(R.id.team);
        year = findViewById(R.id.year);
        registerBtn = findViewById(R.id.registerBtn);
        backBtn = findViewById(R.id.backBtn);

        backBtn.setOnClickListener(v -> {
            finish(); // This will close the current activity and return to the previous one (LoginActivity)
        });

        registerBtn.setOnClickListener(v -> {
            String e = email.getText().toString().trim();
            String p = password.getText().toString().trim();
            String un = username.getText().toString().trim();
            String fn = fullname.getText().toString().trim();
            String t = team.getText().toString().trim();
            String y = year.getText().toString().trim();

            // Check for empty inputs
            if (e.isEmpty() || p.isEmpty() || un.isEmpty() || fn.isEmpty() || t.isEmpty() || y.isEmpty()) {
                Toast.makeText(this, "Kérjük töltse ki az összes mezőt!", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.createUserWithEmailAndPassword(e, p)
                    .addOnSuccessListener(result -> {
                        String uid = auth.getCurrentUser().getUid();
                        Map<String, Object> user = new HashMap<>();
                        user.put("username", un);
                        user.put("fullname", fn);
                        user.put("team", t);
                        user.put("year", y);
                        user.put("email", e);

                        db.collection("users").document(uid).set(user)
                                .addOnSuccessListener(doc -> {
                                    Toast.makeText(this, "Sikeres Regisztráció!", Toast.LENGTH_SHORT).show();
                                    finish(); // vissza a loginhoz
                                })
                                .addOnFailureListener(e1 -> 
                                    Toast.makeText(this, "Hiba történt a regisztráció során: " + e1.getMessage(), Toast.LENGTH_SHORT).show()
                                );
                    })
                    .addOnFailureListener(e1 ->
                            Toast.makeText(this, "Hiba történt a regisztráció során: " + e1.getMessage(), Toast.LENGTH_SHORT).show()
                    );
        });
    }
}

