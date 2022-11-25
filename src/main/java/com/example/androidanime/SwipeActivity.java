package com.example.androidanime;

import android.support.constraint.ConstraintSet;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;

public abstract class SwipeActivity extends AppCompatActivity {

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    GestureDetectorCompat gestureDetector;

    // start at right, end at left
    protected abstract  void onSwipeLeft();
    // start at left, end at right
    protected abstract void onSwipeRight();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gestureDetector = new GestureDetectorCompat(this, new SwipeDetector());
    }

    public class SwipeDetector extends GestureDetector.SimpleOnGestureListener
    {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
        {
            //Check movement along the Y-axis if it exceeds SWIPE_MAX_OFF_PATH, then dismiss the swipe
            if(SWIPE_MAX_OFF_PATH < Math.abs(e1.getY()- e2.getY()))
                return false;

            // meaning we start from right and end at left
            if(e1.getX() > e2.getX())
            {
                if(SWIPE_MIN_DISTANCE < e1.getX() - e2.getX() && SWIPE_THRESHOLD_VELOCITY < Math.abs(velocityX))
                {
                    onSwipeLeft();
                    return true;
                }
            }

            // meaning we start from left and end at right
            if(e2.getX() > e1.getX())
            {
                if(SWIPE_MIN_DISTANCE < e2.getX() - e1.getX() &&  SWIPE_THRESHOLD_VELOCITY < Math.abs(velocityX))
                {
                    onSwipeRight();
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event)
    {
        // dispatcher
        if(gestureDetector != null)
        {
            // if the gesture detector handles the event, a swipe has been executed and no more needs to be done
            if(gestureDetector.onTouchEvent(event))
                return true;
        }

        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        return gestureDetector.onTouchEvent(event);
    }

}