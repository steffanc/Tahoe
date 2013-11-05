package com.lake.tahoe.callbacks;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.List;

/**
 * Created by rhu on 11/3/13.
 */
public abstract class ParseResultCallback<T extends ParseObject> extends FindCallback<T> {

	public ParseResultCallback() {
	}

	@Override
	public void done(List<T> parseObjects, ParseException e) {
		if (e != null) {
			onError(e);
		}

		Log.d("debug", "Results returned found " + parseObjects.size());

		for (T parseObject : parseObjects) {
			handleObject(parseObject);
		}

	}

	public abstract void handleObject(T parseObject);

	public abstract void onError(Throwable e);
}
