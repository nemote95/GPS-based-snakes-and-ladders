package com.example.negar.snakesladders;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;

public class Finish extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
    }

    void startAgain(View view){
        Intent intent = new Intent(this,SetBoardStart.class);
        startActivity(intent);
    }

}
