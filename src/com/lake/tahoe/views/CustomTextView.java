package com.lake.tahoe.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.lake.tahoe.R;
import com.lake.tahoe.utils.Typefaces;


/**
 * Created with IntelliJ IDEA.
 * User: Mohsen Afshin
 * Date: 8/4/13
 * Time: 5:36 PM
 */
// Taken from https://github.com/mafshin/CustomTextView

public class CustomTextView extends TextView {
    public CustomTextView(Context context) {
        super(context);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyAttributes(context, this, attrs);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        applyAttributes(context, this, attrs);
    }

	public static void applyAttributes(Context context, TextView v, AttributeSet attrs) {
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView);
		final int N = a.getIndexCount();
		for (int i = 0; i < N; i++) {
			int attr = a.getIndex(i);
			switch (attr) {
				case R.styleable.CustomTextView_fontAssetName:
					try {
						Typeface font = Typefaces.get(context, a.getString(attr));
						if (font != null) {
							v.setTypeface(font);
						}
					} catch (RuntimeException e) {

					}
			}
		}
	}
}
 
