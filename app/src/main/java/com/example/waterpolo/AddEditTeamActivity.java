package com.example.waterpolo;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddEditTeamActivity extends AppCompatActivity {
    private EditText editTextTeamName;
    private EditText editTextTeamCity;
    private Button buttonSaveTeam;

    private FirebaseFirestore db;
    private String teamId = null; // null if new, otherwise the ID if editing

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_team);

        db = FirebaseFirestore.getInstance();

        editTextTeamName = findViewById(R.id.editTextTeamName);
        editTextTeamCity = findViewById(R.id.editTextTeamCity);
        buttonSaveTeam = findViewById(R.id.buttonSaveTeam);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            teamId = extras.getString("teamId");
            String teamName = extras.getString("teamName");
            String teamCity = extras.getString("teamCity");

            editTextTeamName.setText(teamName);
            editTextTeamCity.setText(teamCity);
        }

        buttonSaveTeam.setOnClickListener(v -> saveTeam());
    }

    private void saveTeam() {
        String name = editTextTeamName.getText().toString().trim();
        String city = editTextTeamCity.getText().toString().trim();

        // Add logging for saveTeam start
        Log.d("AddEditTeamActivity", "Saving team: " + name + ", " + city);

        if (name.isEmpty() || city.isEmpty()) {
            Toast.makeText(this, "Kérjük töltse ki mindkét mezőt!", Toast.LENGTH_SHORT).show();
            Log.d("AddEditTeamActivity", "Save failed: Empty fields");
            return;
        }

        Map<String, Object> team = new HashMap<>();
        team.put("name", name);
        team.put("city", city);

        if (teamId == null) {
            // Create new team
            db.collection("teams")
                    .add(team)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(this, "Csapat hozzáadva", Toast.LENGTH_SHORT).show();
                        Log.d("AddEditTeamActivity", "Team added successfully with ID: " + documentReference.getId());
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Hiba a hozzáadás során: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("AddEditTeamActivity", "Error adding team", e);
                    });
        } else {
            // Update existing team
            db.collection("teams").document(teamId)
                    .set(team)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Csapat frissítve", Toast.LENGTH_SHORT).show();
                        Log.d("AddEditTeamActivity", "Team updated successfully for ID: " + teamId);
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Hiba a frissítés során: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("AddEditTeamActivity", "Error updating team", e);
                    });
        }
    }
} 