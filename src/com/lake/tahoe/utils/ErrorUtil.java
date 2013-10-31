package com.lake.tahoe.utils;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created on 10/30/13.
 */
public class ErrorUtil {

	public static void log(Context c, Throwable t) {
		Log.e(c.getClass().getName(), "Login Error", t);
		Toast toast = Toast.makeText(c, t.getMessage(), Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

}
