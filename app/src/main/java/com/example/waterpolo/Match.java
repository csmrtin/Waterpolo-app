package com.example.waterpolo;

public class Match {
    private String id;
    private String teamA;
    private String teamB;
    private String date;
    private int scoreA;
    private int scoreB;

    public Match() {}

    public Match(String id, String teamA, String teamB, String date, int scoreA, int scoreB) {
        this.id = id;
        this.teamA = teamA;
        this.teamB = teamB;
        this.date = date;
        this.scoreA = scoreA;
        this.scoreB = scoreB;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTeamA() { return teamA; }
    public void setTeamA(String teamA) { this.teamA = teamA; }
    public String getTeamB() { return teamB; }
    public void setTeamB(String teamB) { this.teamB = teamB; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public int getScoreA() { return scoreA; }
    public void setScoreA(int scoreA) { this.scoreA = scoreA; }
    public int getScoreB() { return scoreB; }
    public void setScoreB(int scoreB) { this.scoreB = scoreB; }
} 