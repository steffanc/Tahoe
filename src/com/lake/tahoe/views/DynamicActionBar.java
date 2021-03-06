package com.lake.tahoe.views;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.opengl.Visibility;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.lake.tahoe.R;

public class DynamicActionBar {

	TextView tvTitle;
	ImageView ivLeft, ivRight;
	ActionBar actionBar;
	Activity activity;

	public DynamicActionBar(Activity activity) {
		this(activity, activity.getResources().getColor(R.color.black));
	}

	public DynamicActionBar(Activity activity, int colorResourceId) {
		if ((actionBar = activity.getActionBar()) == null)
			throw new UnsupportedOperationException("ActionBar is not supported on this platform");
		this.activity = activity;
		this.setBackgroundColor(colorResourceId);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setCustomView(activity.getLayoutInflater().inflate(R.layout.action_bar, null));
		ivLeft  = (ImageView) actionBar.getCustomView().findViewById(R.id.ivLeft);
		ivRight = (ImageView) actionBar.getCustomView().findViewById(R.id.ivRight);
		tvTitle = (TextView) actionBar.getCustomView().findViewById(R.id.tvTitle);
	}

	public void setBackgroundColor(int colorResourceId) {
		actionBar.setBackgroundDrawable(new ColorDrawable(colorResourceId));
	}

	public void setTitle(String title) {
		tvTitle.setText(title);
	}

	public void setCancelAction(View.OnClickListener listener) {
		setLeftAction(R.drawable.navigation_cancel, listener);
	}

	public void setAcceptAction(View.OnClickListener listener) {
		setRightAction(R.drawable.navigation_accept, listener);
	}

	public void setLeftArrowAction(View.OnClickListener listener) {
		setLeftAction(R.drawable.navigation_previous_item, listener);
	}

	public void setRightArrowAction(View.OnClickListener listener) {
		setRightAction(R.drawable.navigation_next_item, listener);
	}

	public void setLeftAction(int drawableResourceId, View.OnClickListener listener) {
		setAction(ivLeft, drawableResourceId, listener);
	}

	public void setRightAction(int drawableResourceId, View.OnClickListener listener) {
		setAction(ivRight, drawableResourceId, listener);
	}

	public void toggleLeftAction(int visibilityId) {
		 ivLeft.setVisibility(visibilityId);
	}

	public void toggleRightAction(int visibilityId) {
		ivRight.setVisibility(visibilityId);
	}

	private void setAction(ImageView imageView, int drawableResourceId, View.OnClickListener listener) {
		imageView.setImageDrawable(activity.getResources().getDrawable(drawableResourceId));
		imageView.setOnClickListener(listener);
		imageView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					v.setBackgroundColor(activity.getResources().getColor(R.color.light_blue));
				}
				else if (event.getAction() == MotionEvent.ACTION_UP) {
					v.setBackgroundColor(0x00);
				}
				return false;
			}
		});
	}

}
