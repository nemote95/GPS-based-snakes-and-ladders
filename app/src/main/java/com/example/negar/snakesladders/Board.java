package com.example.negar.snakesladders;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.nfc.cardemulation.OffHostApduService;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private int mColorAccent;


    //get the size from indent later
    private int boardSize=5;
    private Point user_point=new Point(1,2);
    List<List<Integer>> board_tiles_point = new ArrayList<List<Integer>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        mColorBackground = ResourcesCompat.getColor(getResources(),
                R.color.colorBackground, null);
        mColorRectangle = ResourcesCompat.getColor(getResources(),
                R.color.colorRectangle, null);
        mColorAccent = ResourcesCompat.getColor(getResources(),
                R.color.colorAccent, null);

        mPaint.setColor(mColorBackground);

        mPaintText.setColor(
                ResourcesCompat.getColor(getResources(),
                        R.color.colorBoardText, null)
        );

        mImageView = (ImageView) findViewById(R.id.myimageview);
        //drawSomething(mImageView);



    }

    public void drawSomething(View view) {

        int vWidth = view.getWidth();
        int vHeight = view.getHeight();
        Log.d("view", view.toString());

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
                int cell_number= col+1+row*boardSize;
                mPaint.setColor(mColorRectangle - MULTIPLIER * mOffset);
                mRect.set(OFFSET + (sqWidth + OFFSET) * col, OFFSET + (sqHeight + OFFSET) * row, OFFSET + (sqWidth + OFFSET) * col + sqWidth, OFFSET + (sqHeight + OFFSET) * row + sqHeight);
                board_tiles_point.add(Arrays.asList(OFFSET + (sqWidth + OFFSET) * col, OFFSET + (sqHeight + OFFSET) * row, OFFSET + (sqWidth + OFFSET) * col + sqWidth, OFFSET + (sqHeight + OFFSET) * row + sqHeight));

                int cornersRadius = 25-boardSize;
                mCanvas.drawRoundRect(mRect, cornersRadius,cornersRadius,mPaint);
                mCanvas.drawText(String.valueOf(cell_number), OFFSET + (sqWidth + OFFSET)*col+sqWidth/2, (sqHeight + OFFSET) * row+sqHeight/2, mPaintText);

            }
        }

        moveAvatarToTile(8,sqWidth,sqHeight);

        view.invalidate();
    }

    int point_to_tile(int x,int y){
        for(int i=0;i<boardSize*boardSize;i++){
            if (x>board_tiles_point.get(i).get(0) & x<board_tiles_point.get(i).get(2) & y>board_tiles_point.get(i).get(1) & y<board_tiles_point.get(i).get(3))
            {
                return i;

            }
        }
        return -1;
    }

    Point tile_to_poit(int t){
        t=t/boardSize+((t%boardSize-1)*boardSize);
        int x= board_tiles_point.get(t).get(0);
        int y=board_tiles_point.get(t).get(1);
        Point p=new Point(x,y);
        return p;
    }

    void moveAvatarToTile(int tile,int sqWidth,int sqHeight){
        Point p =tile_to_poit(tile);

        Bitmap avatar = BitmapFactory.decodeResource(getResources(), R.drawable.avatar);

        Bitmap resizedBitmap = Bitmap.createScaledBitmap(avatar, sqWidth/2, sqHeight/2, true);

        mCanvas.drawBitmap(resizedBitmap,p.x+sqWidth/4,p.y+sqHeight/2,mPaint);
    }



}
