package com.lake.tahoe.activities;

import android.os.Bundle;
import android.view.View;

import com.lake.tahoe.R;
import com.lake.tahoe.callbacks.ModelCallback;
import com.lake.tahoe.models.Request;
import com.lake.tahoe.models.User;
import com.lake.tahoe.utils.ErrorUtil;
import com.lake.tahoe.utils.Helpers;
import com.lake.tahoe.views.DynamicActionBar;

/**
 * Created by steffan on 11/4/13.
 */
public class RequestActiveClientActivity extends RequestActiveActivity implements ModelCallback<Request> {
	Request request = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		User.getCurrentUser().getUnfinishedRequest(this);
		bar = new DynamicActionBar(this, getResources().getColor(R.color.light_blue));
	}

	@Override
	public void onModelFound(Request _request) {
		if (_request.getState() == Request.State.PENDING) {
			// TODO Forward to real activity once implemented
			// Intent i = new Intent(this, RequestPendingClientActivity.class);
			// startActivity(i);
			return;
		} else if (request == null) {
			request = _request;
			User vendor = request.getClient();
			createViews(vendor);
			createMapViews(vendor);

			bar.setTitle(request.getVendor().getName() + " to the rescue!");
			bar.setXMarkVisibility(View.VISIBLE, null);
		}
		User.getCurrentUser().getUnfinishedRequest(this);
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
