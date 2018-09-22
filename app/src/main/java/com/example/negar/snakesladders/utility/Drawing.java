package com.example.negar.snakesladders.utility;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.negar.snakesladders.model.Board;

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
    public int sqHeight,sqWidth;
    
    public Board board;

    public Drawing(int bgColor, int recColor,int textColor,int boardSize){
        mColorBackground = bgColor;
        mColorRectangle = recColor;
        mPaint.setColor(mColorBackground);
        mPaintText.setColor(textColor);
        this.board=board=new Board(boardSize);

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
        sqWidth = (vWidth - OFFSET) / board.size - OFFSET;
        sqHeight = (vHeight - OFFSET) / board.size - OFFSET;

        mPaintText.setTextSize(sqWidth/3);


        for (int col = 0; col < board.size; col++) {
            for (int row = 0; row < board.size; row++) {
                int cellNumber= col+1+row*board.size;
                mPaint.setColor(mColorRectangle);
                mRect.set(OFFSET + (sqWidth + OFFSET) * col, OFFSET + (sqHeight + OFFSET) * row, OFFSET + (sqWidth + OFFSET) * col + sqWidth, OFFSET + (sqHeight + OFFSET) * row + sqHeight);
                board.boardTilesPoints.add(Arrays.asList(OFFSET + (sqWidth + OFFSET) * col, OFFSET + (sqHeight + OFFSET) * row, OFFSET + (sqWidth + OFFSET) * col + sqWidth, OFFSET + (sqHeight + OFFSET) * row + sqHeight));
                int cornersRadius = 25-board.size;
                mCanvas.drawRoundRect(mRect, cornersRadius,cornersRadius,mPaint);
                mCanvas.drawText(String.valueOf(cellNumber), OFFSET + (sqWidth + OFFSET)*col+sqWidth/2, (sqHeight + OFFSET) * row+sqHeight/2, mPaintText);

            }
        }
        //view.invalidate();
    }

    public void showAvatarInTile(int userTile,int userPrevTile,Bitmap avatar){
        Point p =board.tileToPoit(userTile);
        Point prev=board.tileToPoit(userPrevTile);

        //clear_prev
        mRect.set(prev.x+sqWidth/4, prev.y+sqHeight/2, prev.x+3*sqWidth/4, prev.y + sqHeight);
        mCanvas.drawRect(mRect,mPaint);

        Bitmap resizedBitmap = Bitmap.createScaledBitmap(avatar, sqWidth/2, sqHeight/2, true);
        mCanvas.drawBitmap(resizedBitmap,p.x+sqWidth/4,p.y+sqHeight/2,mPaint);
        Log.e("show tile drawing","tile"+userTile);
    }
}
