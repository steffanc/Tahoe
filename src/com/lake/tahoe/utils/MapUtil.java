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

	private static MarkerOptions getSpeechBubbleMarkerOptions(String title,
	                                                          LatLng latLng,
	                                                          IconGenerator iconGenerator,
	                                                          SpeechBubble.ColorType colorType) {
		MarkerOptions markerOptions = new MarkerOptions().position(latLng);
		BitmapDescriptor bitmapDescriptor = SpeechBubble.generateMarkerBitmap(
				iconGenerator,
				title,
				colorType
		);
		markerOptions.icon(bitmapDescriptor);
		return markerOptions;
	}

	public static MarkerOptions getUserSpeechBubbleMarkerOptions(User user,
	                                                             IconGenerator iconGenerator,
	                                                             SpeechBubble.ColorType colorType) {
		return getSpeechBubbleMarkerOptions(
				user.getName(),
				user.getGoogleMapsLocation(),
				iconGenerator,
				colorType
		);
	}

	public static MarkerOptions getRequestSpeechBubbleMarkerOptions(Request request,
	                                                                IconGenerator iconGenerator,
	                                                                SpeechBubble.ColorType colorType) {
		return getSpeechBubbleMarkerOptions(
				request.getDisplayDollars(),
				request.getGoogleMapsLocation(),
				iconGenerator,
				colorType
		);
	}

}
