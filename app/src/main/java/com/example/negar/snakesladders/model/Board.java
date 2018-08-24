package com.example.negar.snakesladders.model;

import android.graphics.Point;

import java.util.ArrayList;

public class Board {
    private int size;
    private String style="standard";

    public Board(int size, String style){
        this.size=size;
        this.style=style;
        //boardmap
    }

    public Board(int size){
        this.size=size;
    }

    int getSize(){
        return this.size;
    }

    void setSize(int newSize){
        this.size=newSize;
    }
}
