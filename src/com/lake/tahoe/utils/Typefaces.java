package com.lake.tahoe.utils;

import android.content.Context;
import android.graphics.Typeface;

import java.util.Hashtable;

/**
 * Created by rhu on 11/2/13.
 */
// https://code.google.com/p/android/issues/detail?id=9904#c3
public class Typefaces {

	public static String FONT_DIR = "fonts/";

	private static final Hashtable<String, Typeface> cache = new Hashtable<String, Typeface>();

	public static Typeface get(Context c, String name){
		synchronized(cache){
			if(!cache.containsKey(name)){
				Typeface t = Typeface.createFromAsset(c.getAssets(), String.format(Typefaces.FONT_DIR + "%s.ttf", name));
				cache.put(name, t);
			}
			return cache.get(name);
		}
	}
}
