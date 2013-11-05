package com.lake.tahoe.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.lake.tahoe.R;
import com.lake.tahoe.models.Request;
import com.lake.tahoe.models.User;
import com.lake.tahoe.utils.ErrorUtil;
import com.lake.tahoe.utils.Helpers;
import com.lake.tahoe.views.DynamicActionBar;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.List;

/**
 * Created by steffan on 11/4/13.
 */
public class RequestActiveVendorActivity extends RequestActiveActivity {
	Request request;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// TODO Get real request
		request = Helpers.createMockRequest();
		request.setVendor(User.getCurrentUser());

		createViews(request.getClient());

		bar = new DynamicActionBar(this, getResources().getColor(R.color.light_blue));
		bar.setTitle(request.getDisplayDollars() + " | " + request.getTitle());
		bar.setXMarkVisibility(View.VISIBLE, null);
		bar.setRightArrowVisibility(View.VISIBLE, new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Forward to real activity once implemented
				// Intent i = new Intent(this, RequestPendingVendorActivity.class);
				// startActivity(i);
			}
		});
	}

	@Override
	protected void onGooglePlayServicesReady() {
		super.onGooglePlayServicesReady();
		createMapViews(request.getClient());
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

