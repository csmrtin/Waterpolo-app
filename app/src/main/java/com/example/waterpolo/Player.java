package com.example.waterpolo;

public class Player {
    private String id;
    private String name;
    private String team;
    private int year;

    public Player() {}

    public Player(String id, String name, String team, int year) {
        this.id = id;
        this.name = name;
        this.team = team;
        this.year = year;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getTeam() { return team; }
    public void setTeam(String team) { this.team = team; }
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
} 