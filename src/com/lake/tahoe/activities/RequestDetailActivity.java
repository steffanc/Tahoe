package com.lake.tahoe.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.facebook.widget.ProfilePictureView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;
import com.lake.tahoe.R;
import com.lake.tahoe.models.Request;
import com.lake.tahoe.models.User;
import com.lake.tahoe.utils.ErrorUtil;
import com.lake.tahoe.utils.HandlesErrors;
import com.lake.tahoe.utils.Helpers;
import com.lake.tahoe.utils.MapUtil;
import com.lake.tahoe.views.DynamicActionBar;
import com.lake.tahoe.widgets.SpeechBubble;

/**
 * Created by steffan on 11/3/13.
 */
public class RequestDetailActivity extends GoogleLocationServiceActivity implements HandlesErrors {
	ProfilePictureView profilePictureView;
	GoogleMap map;
	Request request;
	IconGenerator iconGenerator = new IconGenerator(this);
	DynamicActionBar bar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_request_detail);

		// TODO make this use real intents
//		Intent i = getIntent();
//		Request request = i.getSerializableExtra("request");

		User user = User.getCurrentUser();
		request = Helpers.createMockRequest();
		request.setVendor(user);
		profilePictureView = (ProfilePictureView)findViewById(R.id.pvProfile);
		profilePictureView.setProfileId(user.getFacebookId());

		TextView tvCost = (TextView) findViewById(R.id.tvCost);
		tvCost.setText(request.getDisplayDollars());

		TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvTitle.setText(request.getTitle());

		TextView tvDescription = (TextView) findViewById(R.id.tvDescription);
		tvDescription.setText(request.getDescription());

		bar = new DynamicActionBar(this, getResources().getColor(R.color.black));
		bar.setTitle(user.getName());
		bar.setXMarkVisibility(View.VISIBLE, null);
		bar.setCheckMarkVisibility(View.VISIBLE, null);
	}

	@Override
	protected void onGooglePlayServicesReady() {
		SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		map = fragment.getMap();
		map.setMyLocationEnabled(true);
		map.getUiSettings().setZoomControlsEnabled(false);
		map.getUiSettings().setMyLocationButtonEnabled(false);

		MarkerOptions markerOptions = MapUtil.getSpeechBubbleMarkerOptions(
				request,
				iconGenerator,
				SpeechBubble.ColorType.BLUE
		);
		map.addMarker(markerOptions);
		MapUtil.panAndZoomToUser(map, request.getClient(), MapUtil.DEFAULT_ZOOM_LEVEL);
	}

	@Override
	protected void onGooglePlayServicesError(Throwable t) {
		onError(t);
	}

	@Override
	protected void onLocationTrackingFailed(Throwable t) {
		onError(t);
	}

	@Override
	public void onError(Throwable t) {
		ErrorUtil.log(this, t);
	}
}
