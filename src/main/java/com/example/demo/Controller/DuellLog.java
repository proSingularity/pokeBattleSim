package com.example.demo.Controller;

public class DuellLog {
    public LogPokemon[] blueTeam;
    public LogPokemon[] redTeam;
    public LogRound[] rounds;
    public boolean blueWon;

    public DuellLog(LogPokemon[] blue, LogPokemon[] red, LogRound[] rounds, boolean blueWon) {
        this.blueTeam = blue;
        this.redTeam = red;
        this.rounds = rounds;
        this.blueWon = blueWon;
    }

    public DuellLog() {
        LogPokemon[] blue = {};
        LogPokemon[] red = {};
        LogRound[] rounds = {};
        this.blueTeam = blue;
        this.redTeam = red;
        this.rounds = rounds;
        this.blueWon = true;
    }
}