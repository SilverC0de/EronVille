package i.brains.eronville;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class XScroll extends ScrollView {

    public XScroll(Context context) {
        super(context);
    }

    public XScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public XScroll(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                //Log.i("XScroll", "onInterceptTouchEvent: DOWN super false" );
                super.onTouchEvent(ev);
                break;

            case MotionEvent.ACTION_MOVE:
                return false; // redirect MotionEvents to ourself

            case MotionEvent.ACTION_CANCEL:
                // Log.i("XScroll", "onInterceptTouchEvent: CANCEL super false" );
                super.onTouchEvent(ev);
                break;

            case MotionEvent.ACTION_UP:
                //Log.i("XScroll", "onInterceptTouchEvent: UP super false" );
                return false;

            default:
                //Log.i("XScroll", "onInterceptTouchEvent: " + action );
                break;
        }

        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        super.onTouchEvent(ev);
        //Log.i("XScroll", "onTouchEvent. action: " + ev.getAction() );
        return true;
    }
}