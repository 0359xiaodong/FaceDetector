package teamthree.facedetector.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class DisableTouchFrameLayout extends FrameLayout {

	private boolean mIsDisableTouch;

	public DisableTouchFrameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	public DisableTouchFrameLayout(Context context) {
		super(context);

	}

	public DisableTouchFrameLayout(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);

	}

	public void setDisableTouch(boolean disable) {

		mIsDisableTouch = disable;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {

		if (mIsDisableTouch)
			return true;
		return super.onInterceptTouchEvent(ev);
	}
}
