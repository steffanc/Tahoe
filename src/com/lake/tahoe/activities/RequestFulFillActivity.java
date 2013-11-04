package com.lake.tahoe.activities;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
public class RequestFulFillActivity extends GoogleLocationServiceActivity implements HandlesErrors {
	ProfilePictureView profilePictureView;
	GoogleMap map;
	Request request;
	IconGenerator iconGenerator = new IconGenerator(this);
	DynamicActionBar bar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_request_fulfill);

		// TODO make this use real intents
//		Intent i = getIntent();
//		Request request = i.getSerializableExtra("request");

		User user = User.getCurrentUser();
		request = Helpers.createMockRequest();
		request.setVendor(user);

		profilePictureView = (ProfilePictureView)findViewById(R.id.pvProfile);
		profilePictureView.setProfileId(user.getFacebookId());

		TextView tvName = (TextView) findViewById(R.id.tvName);
		tvName.setText(user.getName());

		// TODO calculate the real distance of users
		TextView tvDistance = (TextView) findViewById(R.id.tvDistance);
		tvDistance.setText("200m");

		bar = new DynamicActionBar(this, getResources().getColor(R.color.light_blue));
		bar.setTitle(request.getTitle());
		bar.setRightArrowVisibility(View.VISIBLE, null);
		bar.setXMarkVisibility(View.VISIBLE, null);
	}

	@Override
	protected void onGooglePlayServicesReady() {
		SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		map = fragment.getMap();
		map.setMyLocationEnabled(true);
		map.getUiSettings().setZoomControlsEnabled(false);
		map.getUiSettings().setMyLocationButtonEnabled(false);

		MarkerOptions markerOptions = MapUtil.getUserSpeechBubbleMarkerOptions(
				request.getClient(),
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

