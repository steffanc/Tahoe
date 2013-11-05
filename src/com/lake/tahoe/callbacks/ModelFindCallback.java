package com.lake.tahoe.callbacks;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.List;

public class ModelFindCallback<T extends ParseObject> extends FindCallback<T> {

	private ModelCallback<T> callback;

	public ModelFindCallback(ModelCallback<T> callback) {
		this.callback = callback;
	}

	@Override
	public void done(List<T> models, ParseException e) {
		if (e != null)
			callback.onModelError(e);
		for (T model : models)
			callback.onModelFound(model);
	}

}
