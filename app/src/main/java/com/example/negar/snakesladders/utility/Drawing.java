package com.example.negar.snakesladders.utility;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Drawing {
    private Canvas mCanvas;
    private Paint mPaint = new Paint();
    private Paint mPaintText = new Paint();
    private Bitmap mBitmap;
    private ImageView mImageView;
    private RectF mRect = new RectF();
    public static final int OFFSET = 10;
    private int mColorBackground;
    private int mColorRectangle;
    private int sqHeight,sqWidth;
    private int boardSize;
    public List<List<Integer>> boardTilesPoints = new ArrayList<List<Integer>>();

    public Drawing(int bgColor, int recColor,int textColor,int boardSize){
        mColorBackground = bgColor;
        mColorRectangle = recColor;
        mPaint.setColor(mColorBackground);
        mPaintText.setColor(textColor);
        this.boardSize=boardSize;

    }

    public void drawBoard(View view)  {

        int vWidth = view.getWidth();
        int vHeight = view.getHeight();

        mBitmap = Bitmap.createBitmap(vWidth, vHeight, Bitmap.Config.ARGB_8888);
        mImageView= (ImageView) view;
        mImageView.setImageBitmap(mBitmap);
        mCanvas = new Canvas(mBitmap);
        mCanvas.drawColor(mColorBackground);


        // Change the color by subtracting an integer.
        sqWidth = (vWidth - OFFSET) / boardSize - OFFSET;
        sqHeight = (vHeight - OFFSET) / boardSize - OFFSET;

        mPaintText.setTextSize(sqWidth/3);


        for (int col = 0; col < boardSize; col++) {
            for (int row = 0; row < boardSize; row++) {
                int cellNumber= col+1+row*boardSize;
                mPaint.setColor(mColorRectangle);
                mRect.set(OFFSET + (sqWidth + OFFSET) * col, OFFSET + (sqHeight + OFFSET) * row, OFFSET + (sqWidth + OFFSET) * col + sqWidth, OFFSET + (sqHeight + OFFSET) * row + sqHeight);
                boardTilesPoints.add(Arrays.asList(OFFSET + (sqWidth + OFFSET) * col, OFFSET + (sqHeight + OFFSET) * row, OFFSET + (sqWidth + OFFSET) * col + sqWidth, OFFSET + (sqHeight + OFFSET) * row + sqHeight));

                int cornersRadius = 25-boardSize;
                mCanvas.drawRoundRect(mRect, cornersRadius,cornersRadius,mPaint);
                mCanvas.drawText(String.valueOf(cellNumber), OFFSET + (sqWidth + OFFSET)*col+sqWidth/2, (sqHeight + OFFSET) * row+sqHeight/2, mPaintText);

            }
        }
        //view.invalidate();
    }


    public int pointToTile(int x,int y){
        for(int i=0;i<boardSize*boardSize;i++){
            if (x>=boardTilesPoints.get(i).get(0) & x<=boardTilesPoints.get(i).get(2) & y>=boardTilesPoints.get(i).get(1) & y<=boardTilesPoints.get(i).get(3))
            {
                return i;
            }
        }
        return -1;
    }

    public Point tileToPoit(int t){
        if (t%boardSize==0){
            t=t/boardSize+(boardSize-1)*boardSize-1;
        }
        else{
            t=t/boardSize+((t%boardSize-1)*boardSize);
        }

        int x= boardTilesPoints.get(t).get(0);
        int y=boardTilesPoints.get(t).get(1);
        Point p=new Point(x,y);
        return p;
    }

    public void showAvatarInTile(int userTile,int userPrevTile,Bitmap avatar){
        Point p =tileToPoit(userTile);
        Point prev=tileToPoit(userPrevTile);
        Log.e("manage tile drawing","tile"+userTile);


        //clear_prev
        mRect.set(prev.x+sqWidth/4, prev.y+sqHeight/2, prev.x+3*sqWidth/4, prev.y + sqHeight);
        mCanvas.drawRect(mRect,mPaint);

        Bitmap resizedBitmap = Bitmap.createScaledBitmap(avatar, sqWidth/2, sqHeight/2, true);
        mCanvas.drawBitmap(resizedBitmap,p.x+sqWidth/4,p.y+sqHeight/2,mPaint);
    }

}
