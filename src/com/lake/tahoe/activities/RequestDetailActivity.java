package com.lake.tahoe.activities;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.widget.ProfilePictureView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;
import com.lake.tahoe.R;
import com.lake.tahoe.callbacks.ModelCallback;
import com.lake.tahoe.models.Request;
import com.lake.tahoe.models.User;
import com.lake.tahoe.utils.ActivityUtil;
import com.lake.tahoe.utils.MapUtil;
import com.lake.tahoe.utils.PushUtil;
import com.lake.tahoe.views.CustomTextView;
import com.lake.tahoe.views.DynamicActionBar;
import com.lake.tahoe.widgets.SpeechBubble;
import com.lake.tahoe.widgets.SpeechBubbleIconGenerator;
import com.parse.ParsePush;

public class RequestDetailActivity extends GoogleLocationServiceActivity implements PushUtil.HandlesPublish {
	ProfilePictureView profilePictureView;
	GoogleMap map;
	Request request;
	SpeechBubbleIconGenerator iconGenerator = new SpeechBubbleIconGenerator(this);
	DynamicActionBar actionBar;

	public final static String REQUEST_ID = "requestId";
	public final static String REQUEST_STATE = "requestState";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_request_detail);

		actionBar = new DynamicActionBar(this, getResources().getColor(R.color.black));
		actionBar.setLeftArrowAction(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		String state = getIntent().getStringExtra(REQUEST_STATE);
		if (state.equals(Request.State.OPEN.toString())) {
			actionBar.setAcceptAction(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					activateRequest();
				}
			});
		} else if (state.equals(Request.State.ACTIVE.toString())) {
			int backgroundColor = getResources().getColor(R.color.light_blue);
			actionBar.setBackgroundColor(backgroundColor);
			RelativeLayout rl = (RelativeLayout) findViewById(R.id.rlParent);
			rl.setBackgroundColor(backgroundColor);
		}
	}

	@Override
	public void finish() {
		super.finish();
		ActivityUtil.transitionLeft(this);
	}

	public void activateRequest() {
		request.setState(Request.State.ACTIVE);
		request.setVendor(User.getCurrentUser());
		toggleBlocker(true);
		request.saveAndPublish(this);
	}

	@Override
	public void onPublished(ParsePush push) {
		ActivityUtil.startRequestActiveActivity(this, User.getCurrentUser());
		ActivityUtil.transitionRight(this);
	}

	public void updateRequestDetail() {
		User client = request.getClient();
		profilePictureView = (ProfilePictureView)findViewById(R.id.pvProfile);
		profilePictureView.setProfileId(client.getFacebookId());

		actionBar.setTitle(client.getName());

		TextView tvCost = (TextView) findViewById(R.id.tvCost);
		tvCost.setText(request.getDisplayDollars());

		TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvTitle.setText(request.getTitle());

		TextView tvDescription = (TextView) findViewById(R.id.tvDescription);
		tvDescription.setText(request.getDescription());

		MarkerOptions markerOptions = MapUtil.getSpeechBubbleMarkerOptions(
				request,
				iconGenerator,
				SpeechBubble.ColorType.BLUE
		);
		map.addMarker(markerOptions);
		MapUtil.panAndZoomToUser(map, client, MapUtil.DEFAULT_ZOOM_LEVEL);
	}

	@Override
	protected void onGooglePlayServicesReady() {
		SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		map = fragment.getMap();
		map.setMyLocationEnabled(true);
		map.getUiSettings().setZoomControlsEnabled(false);
		map.getUiSettings().setMyLocationButtonEnabled(false);

		Intent i = getIntent();
		String requestId = i.getStringExtra(REQUEST_ID);
		Request.getByObjectId(requestId, new ModelCallback<Request>() {

			@Override public void onModelFound(Request req) {
				request = req;
				RequestDetailActivity.this.updateRequestDetail();
			}

			@Override public void onModelError(Throwable e) {
				onError(e);
			}

		});
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
