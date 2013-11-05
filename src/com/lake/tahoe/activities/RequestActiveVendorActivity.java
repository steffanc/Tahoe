package com.lake.tahoe.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.lake.tahoe.R;
import com.lake.tahoe.callbacks.ModelCallback;
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
public class RequestActiveVendorActivity extends RequestActiveActivity implements ModelCallback<Request> {
	Request request;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		User.getCurrentUser().getUnfinishedRequest(this);
		bar = new DynamicActionBar(this, getResources().getColor(R.color.light_blue));
	}

	@Override
	public void onModelFound(Request _request) {
		request = _request;
		User client = request.getClient();
		createViews(client);
		createMapViews(client);

		bar.setTitle(request.getDisplayDollars() + " | " + request.getTitle());
		bar.setXMarkVisibility(View.VISIBLE, null);
		bar.setRightArrowVisibility(View.VISIBLE, new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				request.setState(Request.State.PENDING);
				request.saveEventually();
				// TODO Forward to real activity once implemented
				// Intent i = new Intent(this, RequestPendingVendorActivity.class);
				// startActivity(i);
			}
		});
	}

	@Override
	public void onModelError(Throwable t) {
		onError(t);
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
