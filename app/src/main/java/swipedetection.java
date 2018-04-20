/**
 * Created by wilop on 19/04/2018.
 */

import android.content.Context;
        import android.support.v7.widget.RecyclerView;
        import android.util.Log;
        import android.view.GestureDetector;
        import android.view.MotionEvent;
        import android.view.View;

public class swipedetection implements RecyclerView.OnItemTouchListener, RecyclerView.OnLongClickListener {

    private OnItemClickListener mListener;
    GestureDetector gestureDetector;
    Runnable runnableSwipeLeftToRight, runnableSwipeRightToLeft;

    private static final String TAG = "RecyclerItemClickListen";
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 200;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    @Override
    public boolean onLongClick(View view) {
        return false;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    GestureDetector mGestureDetector;

    public swipedetection(Context context, OnItemClickListener listener, Runnable runnableSwipeLeftToRight, Runnable runnableSwipeRightToLeft) {
        this.mListener = listener;
        this.runnableSwipeLeftToRight = runnableSwipeLeftToRight;
        this.runnableSwipeRightToLeft = runnableSwipeRightToLeft;

        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });

        gestureDetector = new GestureDetector(context, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent motionEvent) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent motionEvent) { }

            @Override
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) { return false; }

            @Override
            public void onLongPress(MotionEvent motionEvent) { }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float v1) {
                try {
                    float diffAbs = Math.abs(e1.getY() - e2.getY());
                    float diff = e1.getX() - e2.getX();

                    if (diffAbs > SWIPE_MAX_OFF_PATH) {
                        return false;
                    }

                    // Left swipe
                    if (diff > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                        onLeftSwipe();

                        // Right swipe
                    } else if (-diff > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                        onRightSwipe();
                    }
                } catch (Exception e) {
                    Log.e("YourActivity", "Error on gestures");
                }
                return false;
            }
        });
    }

    private void onLeftSwipe() {
        Log.d(TAG, "onLeftSwipe: ");
        runnableSwipeRightToLeft.run();
    }

    private void onRightSwipe() {
        Log.d(TAG, "onRightSwipe:");
        runnableSwipeLeftToRight.run();
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        View childView = view.findChildViewUnder(e.getX(), e.getY());
        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
        }
        return gestureDetector.onTouchEvent(e);
    }

    @Override
    public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

}