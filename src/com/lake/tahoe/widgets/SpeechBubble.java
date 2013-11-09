package com.lake.tahoe.widgets;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

/**
 * Created by rhu on 11/2/13.
 */


// XXX unfortunately we can't subclass Marker, MarkerOptions, Bitmap or BitmapDescriptor
// so this is the best we can do, generate a new SpeechBubble every time we need an update
public class SpeechBubble {

	public static enum ColorType {
		PURPLE,
		BLUE,
		BLACK
	}


	private static Bitmap generateBitmap(SpeechBubbleIconGenerator iconGenerator,
	                                     String text,
	                                     ColorType color) {

		if (color == ColorType.PURPLE) {
			iconGenerator.setStyle(SpeechBubbleIconGenerator.STYLE_PURPLE);
		}
		else if (color == ColorType.BLUE) {
			iconGenerator.setStyle(SpeechBubbleIconGenerator.STYLE_BLUE);
		}
		else if (color == ColorType.BLACK) {
			iconGenerator.setStyle(SpeechBubbleIconGenerator.STYLE_BLACK);
		}

		return iconGenerator.makeIcon(text);
	}

	public static BitmapDescriptor generateMarkerBitmap(SpeechBubbleIconGenerator iconGenerator,
	                                                    String text,
	                                                    ColorType color) {
		Bitmap bitmap = SpeechBubble.generateBitmap(
				iconGenerator,
				text,
				color
		);

		return BitmapDescriptorFactory.fromBitmap(bitmap);
	}

}