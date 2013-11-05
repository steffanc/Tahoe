package com.lake.tahoe.callbacks;

import com.parse.ParseObject;

/**
 * Created on 11/4/13.
 */
public interface ModelCallback<T extends ParseObject> {
	public void onModelFound(T model);
	public void onModelError(Throwable t);
}
