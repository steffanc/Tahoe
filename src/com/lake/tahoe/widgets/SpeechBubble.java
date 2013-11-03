package com.lake.tahoe.widgets;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;

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

	private static Bitmap generateBitmap(IconGenerator iconGenerator,
	                                     String text,
	                                     ColorType color) {

		// TODO: extend IconGenerator to use our speech bubbles, since it doesn't
		// appear to have the color black!

		if (color == ColorType.PURPLE) {
			iconGenerator.setStyle(IconGenerator.STYLE_PURPLE);
		}
		else if (color == ColorType.BLUE) {
			iconGenerator.setStyle(IconGenerator.STYLE_BLUE);
		}
		else if (color == ColorType.BLACK) {
			iconGenerator.setStyle(IconGenerator.STYLE_DEFAULT);
		}

		return iconGenerator.makeIcon(text);
	}

	public static BitmapDescriptor generateMarkerBitmap(IconGenerator iconGenerator,
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