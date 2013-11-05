package com.lake.tahoe.activities;

import android.os.Bundle;
import android.view.View;

import com.lake.tahoe.R;
import com.lake.tahoe.models.Request;
import com.lake.tahoe.models.User;
import com.lake.tahoe.utils.ErrorUtil;
import com.lake.tahoe.utils.Helpers;
import com.lake.tahoe.views.DynamicActionBar;

/**
 * Created by steffan on 11/4/13.
 */
public class RequestActiveClientActivity extends RequestActiveActivity {
	Request request;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// TODO Get real request
		request = Helpers.createMockRequest();
		request.setClient(User.getCurrentUser());

		createViews(request.getVendor());

		bar = new DynamicActionBar(this, getResources().getColor(R.color.light_blue));
		bar.setTitle(request.getVendor().getName() + " to the rescue!");
		bar.setXMarkVisibility(View.VISIBLE, null);
		bar.setRightArrowVisibility(View.VISIBLE, new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Forward to real activity once implemented
//				Intent i = new Intent(this, RequestPendingClientActivity.class);
//				startActivity(i);
			}
		});
	}

	@Override
	protected void onGooglePlayServicesReady() {
		super.onGooglePlayServicesReady();
		createMapViews(request.getVendor());
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
