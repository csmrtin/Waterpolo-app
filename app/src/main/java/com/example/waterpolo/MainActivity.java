package com.example.waterpolo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    Button btnTeams, btnMatches, btnPlayers, btnSettings;
    private static final String PREFS_NAME = "WaterpoloPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if user is logged in
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean(KEY_IS_LOGGED_IN, false);

        if (!isLoggedIn) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        btnTeams = findViewById(R.id.btnTeams);
        btnMatches = findViewById(R.id.btnMatches);
        btnPlayers = findViewById(R.id.btnPlayers);
        btnSettings = findViewById(R.id.btnSettings);

        btnTeams.setOnClickListener(v -> {
            v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_scale));
            startActivity(new Intent(this, TeamListActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });
        btnMatches.setOnClickListener(v -> {
            v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_scale));
            startActivity(new Intent(this, MatchListActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });
        btnPlayers.setOnClickListener(v -> {
            v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_scale));
            startActivity(new Intent(this, PlayerListActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });
        btnSettings.setOnClickListener(v -> {
            v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_scale));
            startActivity(new Intent(this, SettingsActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
