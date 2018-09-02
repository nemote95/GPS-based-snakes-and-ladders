package com.example.negar.snakesladders;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Board extends AppCompatActivity {
    private Canvas mCanvas;
    private Paint mPaint = new Paint();
    private Paint mPaintText = new Paint();
    private Bitmap mBitmap;
    private ImageView mImageView;
    private RectF mRect = new RectF();
    private static final int OFFSET = 10;
    private int mOffset = OFFSET;
    private static final int MULTIPLIER = 20;
    private int mColorBackground;
    private int mColorRectangle;


    //get the size from indent later
    private int boardSize=5;
    private int userTile=1;
    List<List<Integer>> boardTilesPoints = new ArrayList<List<Integer>>();
    
    
    //communicating with other activities 
    static final int ROLL_Dice_REQUEST = 1;  // The request code


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        mColorBackground = ResourcesCompat.getColor(getResources(),
                R.color.colorBackground, null);
        mColorRectangle = ResourcesCompat.getColor(getResources(),
                R.color.colorRectangle, null);


        mPaint.setColor(mColorBackground);

        mPaintText.setColor(
                ResourcesCompat.getColor(getResources(),
                        R.color.colorBoardText, null)
        );

        mImageView = (ImageView) findViewById(R.id.myimageview);
        //drawSomething(mImageView);



    }

    public void drawSomething(View view) throws InterruptedException {

        int vWidth = view.getWidth();
        int vHeight = view.getHeight();

        mBitmap = Bitmap.createBitmap(vWidth, vHeight, Bitmap.Config.ARGB_8888);
        mImageView.setImageBitmap(mBitmap);
        mCanvas = new Canvas(mBitmap);
        mCanvas.drawColor(mColorBackground);


    // Change the color by subtracting an integer.
        int sqWidth = (vWidth - OFFSET) / boardSize - OFFSET;
        int sqHeight = (vHeight - OFFSET) / boardSize - OFFSET;

        mPaintText.setTextSize(sqWidth/3);


        for (int col = 0; col < boardSize; col++) {
            for (int row = 0; row < boardSize; row++) {
                int cellNumber= col+1+row*boardSize;
                mPaint.setColor(mColorRectangle - MULTIPLIER * mOffset);
                mRect.set(OFFSET + (sqWidth + OFFSET) * col, OFFSET + (sqHeight + OFFSET) * row, OFFSET + (sqWidth + OFFSET) * col + sqWidth, OFFSET + (sqHeight + OFFSET) * row + sqHeight);
                boardTilesPoints.add(Arrays.asList(OFFSET + (sqWidth + OFFSET) * col, OFFSET + (sqHeight + OFFSET) * row, OFFSET + (sqWidth + OFFSET) * col + sqWidth, OFFSET + (sqHeight + OFFSET) * row + sqHeight));

                int cornersRadius = 25-boardSize;
                mCanvas.drawRoundRect(mRect, cornersRadius,cornersRadius,mPaint);
                mCanvas.drawText(String.valueOf(cellNumber), OFFSET + (sqWidth + OFFSET)*col+sqWidth/2, (sqHeight + OFFSET) * row+sqHeight/2, mPaintText);

            }
        }

        //user movement;use while statement later

        if (userTile!=boardSize*boardSize){
            showAvatarInTile(userTile,sqWidth,sqHeight);
        }
        else{
            userTile=0;// got congratulation page!
        }

        //view.invalidate();
    }

    int pointToTile(int x,int y){
        for(int i=0;i<boardSize*boardSize;i++){
            if (x>boardTilesPoints.get(i).get(0) & x<boardTilesPoints.get(i).get(2) & y>boardTilesPoints.get(i).get(1) & y<boardTilesPoints.get(i).get(3))
            {
                return i;

            }
        }
        return -1;
    }

    Point tileToPoit(int t){
        t=t/boardSize+((t%boardSize-1)*boardSize);
        int x= boardTilesPoints.get(t).get(0);
        int y=boardTilesPoints.get(t).get(1);
        Point p=new Point(x,y);
        return p;
    }

    void showAvatarInTile(int tile,int sqWidth,int sqHeight){
        Point p =tileToPoit(tile);

        Bitmap avatar = BitmapFactory.decodeResource(getResources(), R.drawable.avatar);

        Bitmap resizedBitmap = Bitmap.createScaledBitmap(avatar, sqWidth/2, sqHeight/2, true);

        mCanvas.drawBitmap(resizedBitmap,p.x+sqWidth/4,p.y+sqHeight/2,mPaint);
    }

    void getDiceNumber(View view){
        Intent rollDice = new Intent(this,DiceRoll.class);
        startActivityForResult(rollDice, ROLL_Dice_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ROLL_Dice_REQUEST) {
            if (resultCode == RESULT_OK) {
                userTile+=data.getIntExtra("diceNumber",1);
            }
        }
    }

}
