package com.lake.tahoe.activities;

import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;

import com.facebook.widget.ProfilePictureView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.lake.tahoe.R;
import com.lake.tahoe.handlers.UserUpdateChannel;
import com.lake.tahoe.models.Request;
import com.lake.tahoe.models.User;
import com.lake.tahoe.utils.MapUtil;
import com.lake.tahoe.views.DynamicActionBar;
import com.lake.tahoe.widgets.SpeechBubble;
import com.lake.tahoe.widgets.SpeechBubbleIconGenerator;

public abstract class RequestActiveActivity extends GoogleLocationServiceActivity implements
	UserUpdateChannel.HandlesUserUpdates {

	GoogleMap map;
	DynamicActionBar actionBar;
	ProfilePictureView profilePictureView;
	SpeechBubbleIconGenerator iconGenerator = new SpeechBubbleIconGenerator(this);

	Request request;
	Marker currentUserMarker;
	Marker remoteUserMarker;

	TextView tvDistance;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		actionBar = new DynamicActionBar(this, getResources().getColor(R.color.light_blue));
		actionBar.setTitle(getString(R.string.loading_details));
		setContentView(R.layout.activity_request_active);
	}

	protected DynamicActionBar getDynamicActionBar() {
		return actionBar;
	}

	protected void createViews(User user) {
		profilePictureView = (ProfilePictureView)findViewById(R.id.pvProfile);
		profilePictureView.setProfileId(user.getFacebookId());

		TextView tvName = (TextView) findViewById(R.id.tvName);
		tvName.setText(user.getName());

		// TODO calculate the real distance of users
		tvDistance = (TextView) findViewById(R.id.tvDistance);
	}

	@Override
	protected void onGooglePlayServicesReady() {
		SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		map = fragment.getMap();
		map.setMyLocationEnabled(true);
	}

	protected void createMapViews(User user) {
		updateRemoteUserMarker(user);
		MarkerOptions markerOptions = MapUtil.getSpeechBubbleMarkerOptions(
				user,
				iconGenerator,
				SpeechBubble.ColorType.BLUE
		);
		remoteUserMarker = map.addMarker(markerOptions);
		MapUtil.panAndZoomToUser(map, user, MapUtil.DEFAULT_ZOOM_LEVEL);
	}

	public void updateRemoteUserMarker(User user) {
		if (remoteUserMarker != null) {
			remoteUserMarker.setPosition(user.getGoogleMapsLocation());
		}
	}

	protected void updateUserDistance(User user1, User user2) {
		if (user1 != null && user2 != null) {
			double miles = user1.calculateDistance(user2);
			String displayDistance = MapUtil.getMapDisplayDistance(this, miles);
			tvDistance.setText(displayDistance);
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		super.onLocationChanged(location);
		LatLng position = MapUtil.locationToLatLng(location);
		if (currentUserMarker == null) {
			currentUserMarker = map.addMarker(MapUtil.getSpeechBubbleMarkerOptions(
					position,
					getResources().getString(R.string.you),
					iconGenerator,
					SpeechBubble.ColorType.PURPLE
			));
		} else if (!currentUserMarker.getPosition().equals(position)) {
			currentUserMarker.setPosition(position);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		UserUpdateChannel.subscribe(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		UserUpdateChannel.unsubscribe(this);
	}

	@Override
	protected void onGooglePlayServicesError(Throwable t) {
		onError(t);
	}

	@Override
	protected void onLocationTrackingFailed(Throwable t) {
		onError(t);
	}

}

