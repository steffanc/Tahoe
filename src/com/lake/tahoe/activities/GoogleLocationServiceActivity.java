package com.lake.tahoe.activities;

import android.content.IntentSender;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
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
 * Only subclass this activity if you want to track the current geo to the user
 */
public abstract class GoogleLocationServiceActivity extends GooglePlayServicesActivity implements
		LocationListener,
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener {

	public static final int DEFAULT_INTERVAL_MS = 5000;
	public static final int DEFAULT_FASTEST_INTERVAL_MS = 1000;
	public static final int USER_ZOOM_LEVEL = 15;

	private LocationClient locationClient;
	private LocationRequest locationRequest;

	abstract protected void onLocationTrackingFailed(Throwable t);

	@Override
	public void onConnected(Bundle bundle) {
		if (locationRequest == null) {
			locationRequest = LocationRequest.create();
			locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
			locationRequest.setInterval(DEFAULT_INTERVAL_MS);
			locationRequest.setFastestInterval(DEFAULT_FASTEST_INTERVAL_MS);
		}
		locationClient.requestLocationUpdates(locationRequest, this);
	}

	@Override
	protected void onStart() {
		if (locationClient == null)
			locationClient = new LocationClient(this, this, this);
		if (!locationClient.isConnected())
			locationClient.connect();
		super.onStart();
	}

	@Override
	protected void onStop() {
		if (locationClient.isConnected())
			locationClient.disconnect();
		super.onStop();
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		if (!connectionResult.hasResolution()) {
			spawnGooglePlayDialog(connectionResult.getErrorCode());
			return;
		} try {
			connectionResult.startResolutionForResult(this, GOOGLE_PLAY_SERVICES_REQUEST_CODE);
		} catch (IntentSender.SendIntentException e) {
			onLocationTrackingFailed(e);
		}
	}

	public void zoomToUser(User user, GoogleMap map) {
		LatLng latLng = user.getGoogleMapsLocation();
		if (latLng != null) {
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(
					latLng,
					USER_ZOOM_LEVEL));
		}
	}

	public MarkerOptions createRequestMarkerOptions(Request request,
	                                                IconGenerator iconGenerator,
	                                                SpeechBubble.ColorType colorType) {
		MarkerOptions markerOptions =
				new MarkerOptions()
						.position(request.getGoogleMapsLocation());
		String title = request.getDisplayDollars();
		BitmapDescriptor bitmapDescriptor = SpeechBubble.generateMarkerBitmap(
				iconGenerator,
				title,
				colorType
		);
		markerOptions.title(title);
		markerOptions.icon(bitmapDescriptor);

		return markerOptions;
	}

	@Override
	public void onLocationChanged(Location location) {
		User user = User.getCurrentUser();
		if (user == null) return;
		user.setLocation(location.getLatitude(), location.getLongitude());
		user.saveEventually();
	}

	@Override
	public void onDisconnected() { }

}
