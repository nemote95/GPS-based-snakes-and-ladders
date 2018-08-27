package com.example.negar.snakesladders.model;

import android.location.Location;

import java.util.ArrayList;
import java.util.Calendar;

import com.example.negar.snakesladders.utility.Network;

public class Player {
    public String name ;
    //picture: jpg
    String avatar;
    Boolean host;
    Boolean finished=false;
    Integer score=0;
    ArrayList square;
    Location GPSLocation;
    String IPAddress;
    Integer turnsTakenToFinish=0;
    Integer durationOfPlaying;

    public Player(String name, Boolean host,String avatar){
        this.name=name;
        this.host=host;
        this.avatar=avatar;
        String ip=Network.getLocalIpAddress();
        this.IPAddress=ip;
    }

    public void calculateScrore(int diff){
        this.score+=(this.turnsTakenToFinish*10)-this.durationOfPlaying;
    }

    public void calculate_duration(Long game_initi_time){
        Long now = Calendar.getInstance().getTimeInMillis();
        this.durationOfPlaying = (int) ((now - game_initi_time)/60*1000);
    }

    //location listener


}
