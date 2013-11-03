package com.lake.tahoe.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;

import com.lake.tahoe.R;
import com.lake.tahoe.utils.Typefaces;


/**
 * Created with IntelliJ IDEA.
 * User: Mohsen Afshin
 * Date: 8/4/13
 * Time: 5:36 PM
 */

// EditText inherits over TextView, so we can reuse the static method to apply attributes to editText.

public class CustomEditText extends EditText {
    public CustomEditText(Context context) {
        super(context);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        CustomTextView.applyAttributes(context, this, attrs);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        CustomTextView.applyAttributes(context, this, attrs);
    }
}
 
