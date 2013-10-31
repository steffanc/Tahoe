package com.lake.tahoe.utils;

/**
 * General purpose error handler. All Activities should probably implement this interface, since we don't want errors
 * bubbling up beyond the Activity scope. Using this pattern also simplifies all underlying classes, since their methods
 * can simply throw errors without worrying about handling them beforehand.
 */
public interface HandlesErrors {
	public void onError(Throwable t);
}
