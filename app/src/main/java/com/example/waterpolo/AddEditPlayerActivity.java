package com.example.waterpolo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddEditPlayerActivity extends AppCompatActivity {
    private EditText editTextPlayerName;
    private EditText editTextPlayerTeam;
    private EditText editTextPlayerYear;
    private Button buttonSavePlayer;

    private FirebaseFirestore db;
    private String playerId = null; // null ha új, egyébként az ID ha szerkesztés

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_player);

        db = FirebaseFirestore.getInstance();

        editTextPlayerName = findViewById(R.id.editTextPlayerName);
        editTextPlayerTeam = findViewById(R.id.editTextPlayerTeam);
        editTextPlayerYear = findViewById(R.id.editTextPlayerYear);
        buttonSavePlayer = findViewById(R.id.buttonSavePlayer);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            playerId = extras.getString("playerId");
            String playerName = extras.getString("playerName");
            String playerTeam = extras.getString("playerTeam");
            int playerYear = extras.getInt("playerYear");

            editTextPlayerName.setText(playerName);
            editTextPlayerTeam.setText(playerTeam);
            editTextPlayerYear.setText(String.valueOf(playerYear));
        }

        buttonSavePlayer.setOnClickListener(v -> savePlayer());
    }

    private void savePlayer() {
        String name = editTextPlayerName.getText().toString().trim();
        String team = editTextPlayerTeam.getText().toString().trim();
        String yearString = editTextPlayerYear.getText().toString().trim();

        if (name.isEmpty() || team.isEmpty() || yearString.isEmpty()) {
            Toast.makeText(this, "Kérjük töltse ki az összes mezőt!", Toast.LENGTH_SHORT).show();
            return;
        }

        int year = 0;
        try {
            year = Integer.parseInt(yearString);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Hibás születési év formátum! Kérjük számot adjon meg.", Toast.LENGTH_SHORT).show();
            return; // Ne mentsük a játékost, ha hibás a bevitel
        }

        Map<String, Object> player = new HashMap<>();
        player.put("name", name);
        player.put("team", team);
        player.put("year", year);

        if (playerId == null) {
            // Create new player
            db.collection("players")
                    .add(player)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(this, "Játékos hozzáadva", Toast.LENGTH_SHORT).show();
                        // Navigate back to PlayerListActivity
                        Intent intent = new Intent(AddEditPlayerActivity.this, PlayerListActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Hiba a hozzáadás során: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            // Update existing player
            db.collection("players").document(playerId)
                    .set(player)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Játékos frissítve", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Hiba a frissítés során: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }
} 