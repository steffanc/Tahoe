package com.lake.tahoe.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;


/**
 * Created with IntelliJ IDEA.
 * User: Mohsen Afshin
 * Date: 8/4/13
 * Time: 5:36 PM
 */

// EditText inherits over TextView, so we can reuse the static method to apply attributes to editText.

public class CustomButton extends Button {
    public CustomButton(Context context) {
        super(context);
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        CustomTextView.applyAttributes(context, this, attrs);
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        CustomTextView.applyAttributes(context, this, attrs);
    }
}
 
