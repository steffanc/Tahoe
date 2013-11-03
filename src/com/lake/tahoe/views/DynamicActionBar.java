package com.lake.tahoe.views;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
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
public class DynamicActionBar {

	ActionBar actionBar;

	String curText;
	int curColorId;

	public DynamicActionBar(Activity activity, int colorId) {

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

	public void setText(String headline) {

		this.curText = headline;

		if (headline == null) {
			return;
		}
		TextView textView = (TextView) actionBar.getCustomView().findViewById(R.id.actionBarHeadline);
		textView.setText(headline);
	}

	public void setLeftArrowVisibility(int visibility) {
		ImageView leftArrow = (ImageView) actionBar.getCustomView().findViewById(R.id.leftArrow);

		this.setVisibility(leftArrow, visibility);
	}

	public void setRightArrowVisibility(int visibility) {

		ImageView rightArrow = (ImageView) actionBar.getCustomView().findViewById(R.id.rightArrow);

		this.setVisibility(rightArrow, visibility);
	}

	public void setVisibility(ImageView arrow, int visibility) {
		arrow.setVisibility(visibility);
	}
}
