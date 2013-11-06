package com.lake.tahoe.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.maps.android.ui.IconGenerator;
import com.lake.tahoe.R;
import com.lake.tahoe.callbacks.ModelCallback;
import com.lake.tahoe.models.User;
import com.lake.tahoe.utils.ErrorUtil;
import com.lake.tahoe.utils.HandlesErrors;
import com.lake.tahoe.utils.MapUtil;
import com.lake.tahoe.views.DynamicActionBar;
import com.lake.tahoe.widgets.SpeechBubble;

public class RequestOpenActivity extends GoogleLocationServiceActivity implements HandlesErrors {

	GoogleMap map;
	IconGenerator iconGenerator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_request_open);

		DynamicActionBar actionBar = new DynamicActionBar(RequestOpenActivity.this, getResources().getColor(R.color.black));

		actionBar.setTitle(getString(R.string.waiting_for_vendors));
		actionBar.setXMarkVisibility(View.VISIBLE, new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				setResult(RESULT_CANCELED, i);
				finish();
			}
		});
	}

	protected void onGooglePlayServicesReady() {

		SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		map = fragment.getMap();
		map.setMyLocationEnabled(true);
		MapUtil.panAndZoomToCurrentUser(map, MapUtil.DEFAULT_ZOOM_LEVEL);

		iconGenerator = new IconGenerator(RequestOpenActivity.this);

		ModelCallback<User> markerFactoryCallback = new ModelCallback<User>() {

			@Override
			public void onModelFound(User user) {
				map.addMarker(MapUtil.getSpeechBubbleMarkerOptions(user.getGoogleMapsLocation(),
						user.getName(), iconGenerator, SpeechBubble.ColorType.BLACK));
			}

			@Override
			public void onModelError(Throwable e) {
				RequestOpenActivity.this.onError(e);
			}
		};

		User user = User.getCurrentUser();
		user.findNearbyUsers(User.Type.VENDOR, markerFactoryCallback);

		map.addMarker(MapUtil.getSpeechBubbleMarkerOptions(user.getGoogleMapsLocation(),
				getResources().getString(R.string.you), iconGenerator, SpeechBubble.ColorType.PURPLE));
	}

	@Override
	public void onError(Throwable t) {
		ErrorUtil.log(this, t);
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