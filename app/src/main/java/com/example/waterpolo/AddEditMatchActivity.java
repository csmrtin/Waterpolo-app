package com.example.waterpolo;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddEditMatchActivity extends AppCompatActivity {
    private EditText editTextTeamA;
    private EditText editTextTeamB;
    private EditText editTextMatchDate;
    private EditText editTextScoreA;
    private EditText editTextScoreB;
    private Button buttonSaveMatch;

    private FirebaseFirestore db;
    private String matchId = null; // null if new, otherwise the ID if editing

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_match);

        db = FirebaseFirestore.getInstance();

        editTextTeamA = findViewById(R.id.editTextTeamA);
        editTextTeamB = findViewById(R.id.editTextTeamB);
        editTextMatchDate = findViewById(R.id.editTextMatchDate);
        editTextScoreA = findViewById(R.id.editTextScoreA);
        editTextScoreB = findViewById(R.id.editTextScoreB);
        buttonSaveMatch = findViewById(R.id.buttonSaveMatch);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            matchId = extras.getString("matchId");
            String teamA = extras.getString("teamA");
            String teamB = extras.getString("teamB");
            String date = extras.getString("date");
            int scoreA = extras.getInt("scoreA");
            int scoreB = extras.getInt("scoreB");

            editTextTeamA.setText(teamA);
            editTextTeamB.setText(teamB);
            editTextMatchDate.setText(date);
            editTextScoreA.setText(String.valueOf(scoreA));
            editTextScoreB.setText(String.valueOf(scoreB));
        }

        buttonSaveMatch.setOnClickListener(v -> saveMatch());
    }

    private void saveMatch() {
        String teamA = editTextTeamA.getText().toString().trim();
        String teamB = editTextTeamB.getText().toString().trim();
        String date = editTextMatchDate.getText().toString().trim();
        String scoreAString = editTextScoreA.getText().toString().trim();
        String scoreBString = editTextScoreB.getText().toString().trim();

        if (teamA.isEmpty() || teamB.isEmpty() || date.isEmpty() || scoreAString.isEmpty() || scoreBString.isEmpty()) {
            Toast.makeText(this, "Kérjük töltse ki az összes mezőt!", Toast.LENGTH_SHORT).show();
            return;
        }

        int scoreA = 0;
        int scoreB = 0;
        try {
            scoreA = Integer.parseInt(scoreAString);
            scoreB = Integer.parseInt(scoreBString);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Hibás pontszám formátum! Kérjük számot adjon meg.", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> match = new HashMap<>();
        match.put("teamA", teamA);
        match.put("teamB", teamB);
        match.put("date", date);
        match.put("scoreA", scoreA);
        match.put("scoreB", scoreB);

        if (matchId == null) {
            // Create new match
            db.collection("matches")
                    .add(match)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(this, "Mérkőzés hozzáadva", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Hiba a hozzáadás során: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            // Update existing match
            db.collection("matches").document(matchId)
                    .set(match)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Mérkőzés frissítve", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Hiba a frissítés során: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }
} 