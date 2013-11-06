package com.lake.tahoe.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.lake.tahoe.R;
import com.lake.tahoe.callbacks.ModelCallback;
import com.lake.tahoe.channels.RequestUpdateChannel;
import com.lake.tahoe.models.Request;
import com.lake.tahoe.models.User;
import com.lake.tahoe.utils.ErrorUtil;
import com.lake.tahoe.utils.Helpers;
import com.lake.tahoe.views.DynamicActionBar;
import com.parse.ParseException;
import com.parse.SaveCallback;

import org.json.JSONException;

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
			launchPending();
			return;
		}
		if (!_request.getState().equals(Request.State.OPEN)) {
			return;
		}
		if (request == null) {
			request = _request;
			User vendor = request.getClient();
			createViews(vendor);
			createMapViews(vendor);

			if (request.getVendor() == null) {
				onError(new IllegalStateException("Bad request with no vendor"));
			}
			else {
				bar.setTitle(request.getVendor().getName() + " to the rescue!");
				bar.setCheckMarkVisibility(View.VISIBLE, new View.OnClickListener () {
					@Override
					public void onClick(View v) {
						Log.d("debug", "Setting Request to Active");
						request.setState(Request.State.ACTIVE);
						request.saveEventually(new SaveCallback() {
							@Override
							public void done(ParseException e) {
								if (e == null) {
									try {
										RequestUpdateChannel.publish(RequestActiveClientActivity.this, request);
									} catch (JSONException e1) {
										onError(e1);
									}

									RequestActiveClientActivity.this.launchPending();
								} else {
									onError(e);
								}
							}
						});
					}
				});
			}
		User.getCurrentUser().getUnfinishedRequest(this);
	  }
	}

	protected void launchPending() {
		Intent i = new Intent(RequestActiveClientActivity.this, RequestPendingClientActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(i);
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
