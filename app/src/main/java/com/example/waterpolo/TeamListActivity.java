package com.example.waterpolo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class TeamListActivity extends AppCompatActivity {
    private RecyclerView teamRecyclerView;
    private TeamAdapter teamAdapter;
    private List<Team> teams;
    private FirebaseFirestore db;
    private FloatingActionButton fabAddTeam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_list);

        db = FirebaseFirestore.getInstance();

        teamRecyclerView = findViewById(R.id.teamRecyclerView);
        teamRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        teams = new ArrayList<>();
        // TODO: Create TeamAdapter.java and team_item.xml
        teamAdapter = new TeamAdapter(teams);
        teamRecyclerView.setAdapter(teamAdapter);

        fabAddTeam = findViewById(R.id.fabAddTeam);
        fabAddTeam.setOnClickListener(view -> {
            Intent intent = new Intent(TeamListActivity.this, AddEditTeamActivity.class);
            startActivity(intent);
        });

        // TODO: Implement item click and delete listeners on the adapter
        teamAdapter.setOnItemClickListener(new TeamAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Team team) {
                Intent intent = new Intent(TeamListActivity.this, AddEditTeamActivity.class);
                intent.putExtra("teamId", team.getId());
                intent.putExtra("teamName", team.getName());
                intent.putExtra("teamCity", team.getCity());
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(String teamId) {
                deleteTeam(teamId);
            }
        });

        // loadTeams(); // Call in onResume
        Log.d("TeamListActivity", "onCreate finished");
    }

    private void loadTeams() {
        Log.d("TeamListActivity", "loadTeams called");
        db.collection("teams")
                .orderBy("name") // Complex query: Order by name
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    Log.d("TeamListActivity", "loadTeams success");
                    teams.clear();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Team team = documentSnapshot.toObject(Team.class);
                        team.setId(documentSnapshot.getId());
                        teams.add(team);
                    }
                    // TODO: Uncomment after creating TeamAdapter
                    teamAdapter.setTeams(teams);
                    teamAdapter.notifyDataSetChanged();
                    Log.d("TeamListActivity", "loadTeams success: adapter updated. Loaded " + teams.size() + " teams.");
                })
                .addOnFailureListener(e -> {
                    Log.e("TeamListActivity", "loadTeams failed", e);
                    // Handle error
                });
    }

    private void deleteTeam(String teamId) {
        Log.d("TeamListActivity", "deleteTeam called for ID: " + teamId);
        db.collection("teams").document(teamId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Log.d("TeamListActivity", "Team deleted successfully");
                    loadTeams(); // Refresh the list after deletion
                    // Toast.makeText(this, "Csapat törölve", Toast.LENGTH_SHORT).show(); // Consider adding a Toast
                })
                .addOnFailureListener(e -> {
                    Log.e("TeamListActivity", "Error deleting team", e);
                    // Handle error
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("TeamListActivity", "onResume called");
        loadTeams(); // Load teams whenever the activity resumes
    }

    // Example of a complex query: Filter by city and order by name
    private void loadTeamsByCity(String cityFilter) {
        db.collection("teams")
                .whereEqualTo("city", cityFilter)
                .orderBy("name")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    teams.clear();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Team team = documentSnapshot.toObject(Team.class);
                        team.setId(documentSnapshot.getId());
                        teams.add(team);
                    }
                    teamAdapter.setTeams(teams);
                    teamAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.e("TeamListActivity", "Error loading teams by city", e);
                    // Handle error
                });
    }
}
