package com.lake.tahoe.widgets;

import android.content.Context;
import android.content.res.TypedArray;

import com.google.maps.android.ui.IconGenerator;
import com.lake.tahoe.R;
import com.lake.tahoe.views.CustomTextView;

/**
 * Created by rhu on 11/9/13.
 */
public class SpeechBubbleIconGenerator extends IconGenerator {
	private Context mContext;

	public static final int STYLE_BLACK = 0; /* AndroidMapUtils doesn't have black */

	public SpeechBubbleIconGenerator (Context context){
		super(context);
		mContext = context;
	}

	private static int getTextStyle(int style) {
		switch (style) {
			default:
			case STYLE_BLACK:
			case STYLE_BLUE:
			case STYLE_PURPLE:
				return R.style.Bubble_TextAppearance_Light_Roboto;
		}
	}

	public void setStyle(int style) {
		setBackground(mContext.getResources().getDrawable(getBackground(style)));

		int textStyle = getTextStyle(style);

		setTextAppearance(mContext, textStyle);
		setCustomFont(textStyle);
	}

	public void setCustomFont(int textStyle) {
		int[] attrs = {R.attr.fontAssetName};
		TypedArray ta = mContext.obtainStyledAttributes(textStyle, attrs);
		CustomTextView.applyAttributes(mContext, ta, mTextView);
	}

	private static int getBackground(int style) {
		switch (style) {
			default:
			case STYLE_BLACK:
				return R.drawable.black_tooltip;
			case STYLE_BLUE:
				return R.drawable.blue_tooltip;
			case STYLE_PURPLE:
				return R.drawable.purple_tooltip;
		}
	}
}
