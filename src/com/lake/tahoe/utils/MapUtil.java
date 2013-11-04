package com.lake.tahoe.utils;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;
import com.lake.tahoe.models.Request;
import com.lake.tahoe.models.User;
import com.lake.tahoe.widgets.SpeechBubble;
import com.parse.FindCallback;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Created on 11/3/13.
 */
public class MapUtil {

	public static final int DEFAULT_ZOOM_LEVEL = 17;

	public static void panAndZoomToCurrentUser(GoogleMap map, int zoom) {
		panAndZoomToUser(map, User.getCurrentUser(), zoom);
	}

	public static void panAndZoomToUser(GoogleMap map, User user, int zoom) {
		panAndZoomToPoint(map, user.getGoogleMapsLocation(), zoom);
	}

	public static void panAndZoomToPoint(GoogleMap map, LatLng point, int zoom) {
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(point, zoom));
	}

	public static MarkerOptions getSpeechBubbleMarkerOptions(Request request, IconGenerator iconGenerator, SpeechBubble.ColorType colorType) {
		String title = request.getDisplayDollars();
		return getSpeechBubbleMarkerOptions(request.getGoogleMapsLocation(), title, iconGenerator, colorType);
	}

	public static MarkerOptions getSpeechBubbleMarkerOptions(LatLng coordinates, String title, IconGenerator iconGenerator, SpeechBubble.ColorType colorType) {
		MarkerOptions markerOptions = new MarkerOptions().position(coordinates);
		BitmapDescriptor bitmapDescriptor = SpeechBubble.generateMarkerBitmap(iconGenerator, title, colorType);
		markerOptions.title(title);
		markerOptions.icon(bitmapDescriptor);
		return markerOptions;
	}

}
