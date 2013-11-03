package com.lake.tahoe.views;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lake.tahoe.R;

/**
 * Created by rhu on 11/3/13.
 */

// Inspired from https://github.com/johannilsson/android-actionbar/blob/master/actionbar/src/com/markupartist/android/widget/ActionBar.java

public class DynamicActionBar {

	ActionBar actionBar;

	String curTitle;
	int curColorId;

	public DynamicActionBar(Activity activity, int colorId) {

	/* Example usage:

		DynamicActionBar bar = new DynamicActionBar(RequestCreateActivity.this, getResources().getColor(R.color.dark_blue));

		bar.setTitle("Here we go");
		bar.setLeftArrowVisibility(View.VISIBLE, new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(RequestCreateActivity.this, v.getTag().toString(), Toast.LENGTH_SHORT).show();
			}
		});

	   Everything is invisible by default.  Explicitly set what you need.
	 */

		// http://stackoverflow.com/questions/12132370/is-there-any-difference-between-getlayoutinflater-and-getsystemservicecontex
		LayoutInflater inflater = (LayoutInflater) activity.getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final ViewGroup actionBarLayout = (ViewGroup) inflater.inflate(R.layout.action_bar, null);

		actionBar = activity.getActionBar();
		this.setBackgroundColor(colorId);

		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setCustomView(actionBarLayout);
	}

	public void setBackgroundColor(int colorId) {
		this.curColorId = colorId;
		actionBar.setBackgroundDrawable(new ColorDrawable(colorId));
	}

	public void setTitle(String headline) {

		this.curTitle = headline;

		if (headline == null) {
			return;
		}
		TextView textView = (TextView) actionBar.getCustomView().findViewById(R.id.actionBarHeadline);
		textView.setText(headline);
	}

	public void setCheckMarkVisibility(int visibility, View.OnClickListener handler) {
		ImageView checkMark = (ImageView) actionBar.getCustomView().findViewById(R.id.checkMark);
		this.setVisibility(checkMark, visibility);
		this.setHandler(checkMark, visibility, handler);
	}

	public void setXMarkVisibility(int visibility, View.OnClickListener handler) {
		ImageView xMark = (ImageView) actionBar.getCustomView().findViewById(R.id.xMark);

		this.setVisibility(xMark, visibility);
		this.setHandler(xMark, visibility, handler);
	}

	public void setLeftArrowVisibility(int visibility, View.OnClickListener handler) {
		ImageView leftArrow = (ImageView) actionBar.getCustomView().findViewById(R.id.leftArrow);

		this.setVisibility(leftArrow, visibility);
		this.setHandler(leftArrow, visibility, handler);
	}

	public void setRightArrowVisibility(int visibility, View.OnClickListener handler) {

		ImageView rightArrow = (ImageView) actionBar.getCustomView().findViewById(R.id.rightArrow);

		this.setVisibility(rightArrow, visibility);
		this.setHandler(rightArrow, visibility, handler);
	}

	public void setHandler(ImageView image, int visibility, View.OnClickListener handler) {
		if (handler != null && visibility == View.VISIBLE) {
			image.setOnClickListener(handler);
		}
	}

	public void setVisibility(ImageView image, int visibility) {
		image.setVisibility(visibility);
	}
}
