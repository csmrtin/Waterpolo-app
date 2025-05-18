package com.example.waterpolo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MatchListActivity extends AppCompatActivity {
    private RecyclerView matchRecyclerView;
    private MatchAdapter matchAdapter;
    private List<Match> matches;
    private FirebaseFirestore db;
    private FloatingActionButton fabAddMatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_list);

        db = FirebaseFirestore.getInstance();

        matchRecyclerView = findViewById(R.id.matchRecyclerView);
        matchRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        matches = new ArrayList<>();
        // TODO: Create MatchAdapter.java and match_item.xml
        matchAdapter = new MatchAdapter(matches);
        matchRecyclerView.setAdapter(matchAdapter);

        fabAddMatch = findViewById(R.id.fabAddMatch);
        fabAddMatch.setOnClickListener(view -> {
            Intent intent = new Intent(MatchListActivity.this, AddEditMatchActivity.class);
            startActivity(intent);
        });

        // TODO: Implement item click and delete listeners on the adapter
        matchAdapter.setOnItemClickListener(new MatchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Match match) {
                Intent intent = new Intent(MatchListActivity.this, AddEditMatchActivity.class);
                intent.putExtra("matchId", match.getId());
                intent.putExtra("teamA", match.getTeamA());
                intent.putExtra("teamB", match.getTeamB());
                intent.putExtra("date", match.getDate());
                intent.putExtra("scoreA", match.getScoreA());
                intent.putExtra("scoreB", match.getScoreB());
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(String matchId) {
                deleteMatch(matchId);
            }
        });

        // loadMatches(); // Call in onResume
        Log.d("MatchListActivity", "onCreate finished");
    }

    private void loadMatches() {
        Log.d("MatchListActivity", "loadMatches called");
        db.collection("matches")
                .orderBy("date", Query.Direction.DESCENDING) // Complex query: Order by date descending
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    Log.d("MatchListActivity", "loadMatches success");
                    matches.clear();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Match match = documentSnapshot.toObject(Match.class);
                        match.setId(documentSnapshot.getId());
                        matches.add(match);
                    }
                    // TODO: Uncomment after creating MatchAdapter
                    matchAdapter.setMatches(matches);
                    matchAdapter.notifyDataSetChanged();
                    Log.d("MatchListActivity", "loadMatches success: adapter updated");
                })
                .addOnFailureListener(e -> {
                    Log.e("MatchListActivity", "loadMatches failed", e);
                    // Handle error
                });
    }

    private void deleteMatch(String matchId) {
        Log.d("MatchListActivity", "deleteMatch called for ID: " + matchId);
        db.collection("matches").document(matchId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Log.d("MatchListActivity", "Match deleted successfully");
                    loadMatches(); // Refresh the list after deletion
                    // Toast.makeText(this, "Mérkőzés törölve", Toast.LENGTH_SHORT).show(); // Consider adding a Toast
                })
                .addOnFailureListener(e -> {
                    Log.e("MatchListActivity", "Error deleting match", e);
                    // Handle error
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MatchListActivity", "onResume called");
        loadMatches(); // Load matches whenever the activity resumes
    }
}
