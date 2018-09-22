package com.example.negar.snakesladders.model;

import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Board {
    public int size;
    private String style="standard";
    public List<List<Integer>> boardTilesPoints = new ArrayList<List<Integer>>();

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

    public int pointToTile(int x,int y){
        for(int i=0;i<size*size;i++){
            if (boardTilesPoints.get(i).get(0)<=x & x<=boardTilesPoints.get(i).get(2) & boardTilesPoints.get(i).get(1)<=y & y<=boardTilesPoints.get(i).get(3))
            {
                return i;
            }
        }
        return -1;
    }

    public void showBoardPoints(){
        for(int i=0;i<size*size;i++){
            Log.d("tile"+i,"["+boardTilesPoints.get(i).get(0)+","+boardTilesPoints.get(i).get(2)+"], [" +boardTilesPoints.get(i).get(1)+"," + boardTilesPoints.get(i).get(3)+"]");

        }
    }

    public Point tileToPoit(int t){
        if (t%size==0){
            t=t/size+(size-1)*size-1;
        }
        else{
            t=t/size+((t%size-1)*size);
        }

        int x= boardTilesPoints.get(t).get(0);
        int y=boardTilesPoints.get(t).get(1);
        Point p=new Point(x,y);
        return p;
    }
}
