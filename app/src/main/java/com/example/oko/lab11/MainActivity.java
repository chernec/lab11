package com.example.oko.lab11;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.graphics.*;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnTouchListener {
    private double zoom = 1;
    private double prevProportion = 1;
    private int mX = 0;
    private int mY = 0;
    private int pX = 0;
    private int pY = 0;
    private double stDistance = 0;
    private boolean inTouch = false;
    private boolean inZoom = false;
    private Draw mm;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mm = new Draw(this);
        mm.setOnTouchListener(this);
        setContentView(mm);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int x = (int)event.getX();
        int y = (int)event.getY();
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: {
                if (((x < (int) (mX + 156 * zoom)) && (x > mX))&&((y < (int) (mY + 230 * zoom)) && (y > mY))) {
                    inTouch = true;
                    pX = x;
                    pY = y;
                }
                break;
            }
            case MotionEvent.ACTION_POINTER_DOWN: {
                int[] p1 = new int[2];
                int[] p2 = new int[2];
                inZoom = true;
                p1[0] = (int)event.getX(0);
                p1[1] = (int)event.getY(0);
                p2[0] = (int)event.getX(1);
                p2[1] = (int)event.getY(1);
                stDistance = Math.sqrt(Math.pow(p1[0] - p2[0], 2) + Math.pow(p1[1] - p2[1], 2));
                prevProportion = 1;
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                if((event.getPointerCount() == 1)&&(inTouch)&&(!inZoom)) {
                    int dX = x - pX;
                    int dY = y - pY;
                    if ((Math.abs(dX) > 5) || (Math.abs(dY) > 5)) {
                        mX += dX;
                        mY += dY;
                        pX = x;
                        pY = y;
                        mm.invalidate();
                    }
                }
                if(event.getPointerCount() == 2) {
                    int cp1[] = new int[2];
                    int cp2[] = new int[2];
                    cp1[0] = (int)event.getX(0);
                    cp1[1] = (int)event.getY(0);
                    cp2[0] = (int)event.getX(1);
                    cp2[1] = (int)event.getY(1);
                    double cDistance = Math.sqrt(Math.pow(cp1[0] - cp2[0], 2) + Math.pow(cp1[1] - cp2[1], 2));
                    double proportion = cDistance / stDistance;
                    if(Math.abs(prevProportion - proportion) > 0.05){
                        zoom += proportion - prevProportion;
                        prevProportion = proportion;
                    }
                    mm.invalidate();
                }
                break;
            }
            case MotionEvent.ACTION_UP: {
                inTouch = false;
                inZoom = false;
            }
        }
        return true;
    }

    class Draw extends View {
        public Draw(Context context) {
            super(context);
        }
        @Override
        protected void onSizeChanged(int w, int h, int oWidth, int oHeight) {
            super.onSizeChanged(w, h, oWidth, oHeight);
        }
        @Override
        protected void onDraw(Canvas canvas) {
            Bitmap pic;
            Paint p = new Paint();
            pic = BitmapFactory.decodeResource(getResources(), R.drawable.ui);
            Rect part1src = new Rect(156, 0, 312, 230);
            Rect part2src = new Rect(805, 200, 825, 225);
            Rect part3src = new Rect(0, 295, 100, 395);
            Rect part1des = new Rect(mX, mY, (int)(mX + 156*zoom), (int)(mY + 230*zoom));
            Rect part2des = new Rect((int)(mX + 15*zoom), (int)(mY + 10*zoom), (int)(mX + 35*zoom), (int)(mY + 35*zoom));
            Rect part3des = new Rect((int)(mX + 25*zoom), (int)(mY + 35*zoom), (int)(mX + 125*zoom), (int)(mY + 135*zoom));
            canvas.drawBitmap(pic, part1src, part1des, p);
            canvas.drawBitmap(pic, part2src, part2des, p);
            canvas.drawBitmap(pic, part3src, part3des, p);
        }
    }
}