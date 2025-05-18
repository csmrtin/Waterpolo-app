package com.example.waterpolo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class PlayerListActivity extends AppCompatActivity {
    private RecyclerView playerRecyclerView;
    private PlayerAdapter playerAdapter;
    private List<Player> players;
    private FirebaseFirestore db;
    private Button buttonAddPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("PlayerListActivity", "onCreate started");
        setContentView(R.layout.activity_player_list);

        db = FirebaseFirestore.getInstance();

        playerRecyclerView = findViewById(R.id.playerRecyclerView);
        playerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        players = new ArrayList<>();
        playerAdapter = new PlayerAdapter(players);
        playerRecyclerView.setAdapter(playerAdapter);
        Log.d("PlayerListActivity", "Adapter set");

        buttonAddPlayer = findViewById(R.id.buttonAddPlayer);
        buttonAddPlayer.setOnClickListener(v -> {
            Intent intent = new Intent(PlayerListActivity.this, AddEditPlayerActivity.class);
            startActivity(intent);
        });

        playerAdapter.setOnItemClickListener(new PlayerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Player player) {
                Intent intent = new Intent(PlayerListActivity.this, AddEditPlayerActivity.class);
                intent.putExtra("playerId", player.getId());
                intent.putExtra("playerName", player.getName());
                intent.putExtra("playerTeam", player.getTeam());
                intent.putExtra("playerYear", player.getYear());
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(String playerId) {
                deletePlayer(playerId);
            }
        });

        loadPlayers();
        Log.d("PlayerListActivity", "onCreate finished, loadPlayers called");
    }

    // Példa: Játékosok lekérdezése csapatnév alapján (where feltétel)
    private void loadPlayersByTeam(String teamName) {
        db.collection("players")
                .whereEqualTo("team", teamName)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    players.clear();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Player player = documentSnapshot.toObject(Player.class);
                        player.setId(documentSnapshot.getId());
                        players.add(player);
                    }
                    playerAdapter.setPlayers(players);
                })
                .addOnFailureListener(e -> {
                    // Hiba kezelése
                });
    }

    // Példa: Játékosok lekérdezése születési év szerint rendezve (orderBy)
    private void loadPlayersOrderedByYear() {
        db.collection("players")
                .orderBy("year")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    players.clear();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Player player = documentSnapshot.toObject(Player.class);
                        player.setId(documentSnapshot.getId());
                        players.add(player);
                    }
                    playerAdapter.setPlayers(players);
                })
                .addOnFailureListener(e -> {
                    // Hiba kezelése
                });
    }

    // Példa: Adott számú játékos lekérdezése (limit)
    private void loadLimitedPlayers(int limit) {
        db.collection("players")
                .limit(limit)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    players.clear();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Player player = documentSnapshot.toObject(Player.class);
                        player.setId(documentSnapshot.getId());
                        players.add(player);
                    }
                    playerAdapter.setPlayers(players);
                })
                .addOnFailureListener(e -> {
                    // Hiba kezelése
                });
    }

    private void loadPlayers() {
        Log.d("PlayerListActivity", "loadPlayers called");
        db.collection("players")
                .orderBy("year")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    Log.d("PlayerListActivity", "loadPlayers success");
                    players.clear();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Player player = documentSnapshot.toObject(Player.class);
                        player.setId(documentSnapshot.getId());
                        players.add(player);
                    }
                    playerAdapter.setPlayers(players);
                    Log.d("PlayerListActivity", "loadPlayers success: adapter updated");
                })
                .addOnFailureListener(e -> {
                    Log.e("PlayerListActivity", "loadPlayers failed", e);
                    // Hiba kezelése
                });
    }

    private void deletePlayer(String playerId) {
        Log.d("PlayerListActivity", "deletePlayer called for ID: " + playerId);
        db.collection("players").document(playerId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Log.d("PlayerListActivity", "Player deleted successfully");
                    loadPlayers();
                    Toast.makeText(this, "Játékos törölve", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e("PlayerListActivity", "Error deleting player", e);
                    // Hiba kezelése
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("PlayerListActivity", "onResume called");
        loadPlayers();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("PlayerListActivity", "onDestroy called");
    }
}
