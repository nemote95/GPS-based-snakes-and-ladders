package com.example.negar.snakesladders;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.nfc.cardemulation.OffHostApduService;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

public class Board extends Activity {
    private Canvas mCanvas;
    private Paint mPaint = new Paint();
    private Paint mPaintText = new Paint();
    private Bitmap mBitmap;
    private ImageView mImageView;
    private Rect mRect = new Rect();
    private Rect mBounds = new Rect();
    private static final int OFFSET = 10;
    private int mOffset = OFFSET;
    private static final int MULTIPLIER = 20;
    private int mColorBackground;
    private int mColorRectangle;
    private int mColorAccent;
    //get the size from indent later
    private int boardSize=10;



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
                        R.color.colorPrimaryDark, null)
        );

        mImageView = (ImageView) findViewById(R.id.myimageview);



    }

    public void drawSomething(View view) {

        int vWidth = view.getWidth();
        int vHeight = view.getHeight();
        int halfWidth = vWidth / 2;
        int halfHeight = vHeight / 2;
        Log.d("view", view.toString());

        mBitmap = Bitmap.createBitmap(vWidth, vHeight, Bitmap.Config.ARGB_8888);
        mImageView.setImageBitmap(mBitmap);
        mCanvas = new Canvas(mBitmap);
        mCanvas.drawColor(mColorBackground);


    // Change the color by subtracting an integer.
        int sqWidth = (vWidth - OFFSET) / boardSize - OFFSET;
        int sqHeight = (vHeight - OFFSET) / boardSize - OFFSET;

        mPaintText.setTextSize(sqWidth/3);


        for (int r = 0; r < boardSize; r++) {
            for (int c = 0; c < boardSize; c++) {
                int cell_number= r+1+c *10;
                mPaint.setColor(mColorRectangle - MULTIPLIER * mOffset);
                mRect.set(OFFSET + (sqWidth + OFFSET) * r, OFFSET + (sqHeight + OFFSET) * c, OFFSET + (sqWidth + OFFSET) * r + sqWidth, OFFSET + (sqHeight + OFFSET) * c + sqHeight);
                mCanvas.drawRect(mRect, mPaint);

                mCanvas.drawText(String.valueOf(cell_number), OFFSET + (sqWidth + OFFSET)*r+sqWidth/2, (sqHeight + OFFSET) * c+sqHeight/2, mPaintText);

            }
        }

        view.invalidate();
    }


}
