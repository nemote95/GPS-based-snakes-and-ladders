package com.example.negar.snakesladders.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.negar.snakesladders.R;


public class selectGame extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_game);
    }


    //for a given game type, populate the details text box with that game
    public void showGameDetails(){

    }

    //hooked to the GO button, sets the currently selected gaem as the game type
    public void chooseGame(){
        //Type.updateType
    }
}
