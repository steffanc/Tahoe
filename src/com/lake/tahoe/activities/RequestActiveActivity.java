package com.lake.tahoe.activities;

import android.os.Bundle;
import android.widget.TextView;
import com.facebook.widget.ProfilePictureView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;
import com.lake.tahoe.R;
import com.lake.tahoe.models.User;
import com.lake.tahoe.utils.MapUtil;
import com.lake.tahoe.views.DynamicActionBar;
import com.lake.tahoe.widgets.SpeechBubble;

public abstract class RequestActiveActivity extends GoogleLocationServiceActivity {

	GoogleMap map;
	DynamicActionBar actionBar;
	ProfilePictureView profilePictureView;
	IconGenerator iconGenerator = new IconGenerator(this);

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
		TextView tvDistance = (TextView) findViewById(R.id.tvDistance);
		tvDistance.setText("0.56mi");
	}

	@Override
	protected void onGooglePlayServicesReady() {
		SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		map = fragment.getMap();
		map.setMyLocationEnabled(true);
	}

	protected void createMapViews(User user) {
		MarkerOptions markerOptions = MapUtil.getSpeechBubbleMarkerOptions(
				user,
				iconGenerator,
				SpeechBubble.ColorType.BLUE
		);
		map.addMarker(markerOptions);
		MapUtil.panAndZoomToUser(map, user, MapUtil.DEFAULT_ZOOM_LEVEL);
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

