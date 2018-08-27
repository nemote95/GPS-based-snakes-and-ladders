package com.example.negar.snakesladders.model;

import android.location.Location;

import java.util.ArrayList;
import java.util.Calendar;

public class Game {
    String name;
    Location center;
    ArrayList <Player> players=null;
    Long startTime;
    Type type;


    public Game(String name, Location center, Type type){
        this.name=name;
        this.startTime= Calendar.getInstance().getTimeInMillis();
        this.center=center;
        this.type=type;
    }

    public ArrayList <Player> leaderboard(){
        //sort required
        return players;
    }

    //public calculateDestination
}
