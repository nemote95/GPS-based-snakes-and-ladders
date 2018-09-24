package com.example.negar.snakesladders;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.negar.snakesladders.R;

import com.example.negar.snakesladders.model.Game;
import com.example.negar.snakesladders.model.Type;
import com.example.negar.snakesladders.model.Board;

import java.util.ArrayList;


public class SelectGame extends AppCompatActivity{
    Board currBoard=new Board(4);


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_game);
    }


    //get list of game types from Game, populate the xml list with game info
    public void populateGames(){
        //ArrayList gameList = Game.getAllGTypes();
        //currBoard = (Board) gameList.get(0);
    }


    //for a given game type, populate the details text box with that game
    //hook to selecting an option from the list of games
    public void showGameDetails(){

    }

    //hooked to the GO button, sets the currently selected gaem as the game type
    public void chooseGame(){
        //Type.updateType("test", "test", this.currBoard);
    }
}
