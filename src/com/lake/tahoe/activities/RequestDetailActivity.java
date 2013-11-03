package com.lake.tahoe.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

import com.facebook.widget.ProfilePictureView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.lake.tahoe.R;
import com.lake.tahoe.models.Request;
import com.lake.tahoe.models.User;
import com.lake.tahoe.utils.ErrorUtil;
import com.lake.tahoe.utils.HandlesErrors;
import com.lake.tahoe.utils.Helpers;
import com.parse.ParseFacebookUtils;

import org.json.JSONException;

/**
 * Created by steffan on 11/3/13.
 */
public class RequestDetailActivity extends GoogleLocationServiceActivity implements HandlesErrors {
	ProfilePictureView profilePictureView;
	GoogleMap map;
	Request request;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_request_detail);
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setTitle(R.string.select_client);

//		Intent i = getIntent();
//		Request request = i.getSerializableExtra("request");

		User user = User.getCurrentUser();
		request = Helpers.createMockRequest();
		request.setVendor(user);
		String facebookId = user.getFacebookId();
		profilePictureView = (ProfilePictureView)findViewById(R.id.pvProfile);
		profilePictureView.setProfileId(facebookId);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.vendor_menu, menu);
		return true;
	}

	@Override
	protected void onGooglePlayServicesReady() {
		SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		map = fragment.getMap();
		map.setMyLocationEnabled(true);
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
