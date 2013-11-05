package com.lake.tahoe.activities;

import android.os.Bundle;
import android.view.View;
import com.lake.tahoe.R;
import com.lake.tahoe.channels.RequestUpdateChannel;
import com.lake.tahoe.models.Request;
import com.parse.ParseException;
import com.parse.SaveCallback;
import org.json.JSONException;

/**
 * Created on 11/5/13.
 */
public class RequestPendingVendorActivity extends RequestPendingActivity {

	Request pendingRequest;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		ivCheck.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		ivCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				abortRequest();
			}
		});

	}

	@Override
	protected void onPendingRequest(Request request) {
		pendingRequest = request;
		ivCancel.setVisibility(View.VISIBLE);
		tvSubText.setText(getString(R.string.waiting_for_payment));
	}

	@Override
	public void onRequestUpdated(Request request) {
		if (pendingRequest == null)
			return;
		if (!request.getObjectId().equals(pendingRequest.getObjectId()))
			return;
		if (request.getState().equals(Request.State.PENDING))
			return;
		if (request.getState().equals(Request.State.FULFILLED))
			showRequestComplete();
		else
			onError(new IllegalStateException("Illegal Request State: " + request.getState().toString()));
	}

	public void showRequestComplete() {
		ivCancel.setVisibility(View.INVISIBLE);
		ivCheck.setVisibility(View.VISIBLE);
		tvSubText.setText("Payment Received!");
	}

	public void abortRequest() {
		pendingRequest.setVendor(null);
		pendingRequest.setState(Request.State.OPEN);
		pendingRequest.saveInBackground(new SaveCallback() {
			@Override public void done(ParseException e) {
				if (e != null) {
					onError(e);
				} else try {
					RequestUpdateChannel.publish(RequestPendingVendorActivity.this, pendingRequest);
					finish();
				} catch (JSONException ex) {
					onError(e);
				}
			}
		});
	}

}
