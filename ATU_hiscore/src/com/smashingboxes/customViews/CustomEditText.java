package com.smashingboxes.customViews;

import android.R;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

import com.smashingboxes.ATUHiscore.*;

public class CustomEditText extends EditText {
	
	private static final int[] GOOD_STATE_SET = {
		com.smashingboxes.ATUHiscore.R.attr.state_good
	};	
	private static final int[] ERROR_STATE_SET = {
		com.smashingboxes.ATUHiscore.R.attr.state_error
	};
	
	private static final String LOG_TAG = "log_CustomEditText";
	
	private boolean _isGood;
	private boolean _isError;

	public CustomEditText(Context context) {
		super(context);
	}
	
	public CustomEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public void setGood(boolean isGood) {
		_isGood = isGood;
		refreshDrawableState();
	}
	
	public void setError(boolean isError) {
		_isError = isError;
		Log.v(LOG_TAG,"@ _isError = " + _isError);
		refreshDrawableState();
	}
	
	@Override
	protected int[] onCreateDrawableState(int extraSpace) {
		final int[] drawableState = super.onCreateDrawableState(extraSpace + 2);
		if(_isGood) {
			mergeDrawableStates(drawableState, GOOD_STATE_SET);
		}
		if(_isError) {
			Log.v(LOG_TAG,"# onCreateDrawableState : error");
			mergeDrawableStates(drawableState, ERROR_STATE_SET);
		}
		
		return drawableState;
		
	}
}
