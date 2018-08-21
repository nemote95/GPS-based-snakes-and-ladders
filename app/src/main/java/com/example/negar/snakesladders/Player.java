package com.example.negar.snakesladders;

import android.text.format.Time;
import android.util.ArrayMap;

import java.util.ArrayList;

public class Player {
    String name ;
    //picture: jpg
    // Avatar: string (colour)
    Boolean host=false;
    Boolean finished=false;
    Integer score=0;
    ArrayList square;
    //Location GPSLocation
    String IPAddress;
    Integer turnsTakenToFinish=0;
    Time durationOfPlaying;

    public Player(String name, Boolean host, String IPAddress){
        this.name=name;
        this.host=host;
        this.IPAddress=IPAddress;

    }
}
