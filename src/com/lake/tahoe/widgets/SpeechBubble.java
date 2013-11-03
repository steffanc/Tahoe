package com.lake.tahoe.widgets;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;

/**
 * Created by rhu on 11/2/13.
 */
public class SpeechBubble {

	public static enum ColorType {
		PURPLE,
		BLUE,
		BLACK
	}

	String text;
	ColorType color;

	LatLng coordinates;

	public SpeechBubble(String name, LatLng coordinates, ColorType color) {
		this.text = name;
		this.coordinates = coordinates;
		this.color = color;
	}

	public Bitmap generateBitmap(IconGenerator iconGenerator) {

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

		return iconGenerator.makeIcon(this.text);
	}

	public MarkerOptions generateMarker(IconGenerator iconGenerator) {
		Bitmap bitmap = this.generateBitmap(iconGenerator);
		MarkerOptions options = new MarkerOptions().position(this.coordinates).icon(BitmapDescriptorFactory.fromBitmap(bitmap));
		return options;

	}
	public ColorType getColor() {
		return color;
	}

	public void setColor(ColorType color) {
		this.color = color;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public LatLng getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(LatLng coordinates) {
		this.coordinates = coordinates;
	}


}