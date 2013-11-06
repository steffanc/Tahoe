package com.lake.tahoe.activities;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.ui.IconGenerator;
import com.lake.tahoe.R;
import com.lake.tahoe.callbacks.ModelCallback;
import com.lake.tahoe.channels.RequestUpdateChannel;
import com.lake.tahoe.models.Request;
import com.lake.tahoe.models.User;
import com.lake.tahoe.utils.ErrorUtil;
import com.lake.tahoe.utils.HandlesErrors;
import com.lake.tahoe.utils.MapUtil;
import com.lake.tahoe.utils.PushUtil;
import com.lake.tahoe.views.DynamicActionBar;
import com.lake.tahoe.widgets.SpeechBubble;

public class RequestOpenActivity extends GoogleLocationServiceActivity implements HandlesErrors, RequestUpdateChannel.HandlesRequestUpdates, ModelCallback<Request> {

	GoogleMap map;
	Marker marker;
	IconGenerator iconGenerator;
	boolean mapReadyToPan = false;
	BroadcastReceiver subscription;
	Request openRequest;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_request_open);

		User user = User.getCurrentUser();

		if (user == null) {
			finish();
			return;
		}

		DynamicActionBar actionBar = new DynamicActionBar(RequestOpenActivity.this, getResources().getColor(R.color.black));

		actionBar.setTitle(getString(R.string.waiting_for_vendors));
		actionBar.setXMarkVisibility(View.VISIBLE, new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				setResult(RESULT_CANCELED, i);
				finish();
			}
		});

		user.getUnfinishedRequest(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		subscription = RequestUpdateChannel.subscribe(this, this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (subscription != null) {
			PushUtil.unsubscribe(this, subscription);
			subscription = null;
		}
	}

	@Override
	public void onModelFound(Request request) {
		if (!request.getState().equals(Request.State.OPEN)) finish();
		else {
			onOpenRequest(request);
		}
	}

	@Override
	public void onModelError(Throwable t) {
		finish();
	}

	protected void onOpenRequest(Request request) {
		openRequest = request;
	}

	@Override
	public void onRequestUpdateError(Throwable t) {
		onError(t);
	}

	@Override
	public void onRequestUpdated(Request request) {
		if (openRequest == null)
			return;
		if (!request.getObjectId().equals(openRequest.getObjectId()))
			return;
		if (request.getState().equals(Request.State.OPEN))
			return;
		if (request.getState().equals(Request.State.ACTIVE))
			startRequestActiveActivity();

		onError(new IllegalStateException("Illegal Request State: " + request.getState().toString()));
	}

	protected void startRequestActiveActivity() {
		Intent i = new Intent(RequestOpenActivity.this, RequestActiveClientActivity.class);
		startActivity(i);
	}

	protected void onGooglePlayServicesReady() {

		SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		map = fragment.getMap();
		map.setMyLocationEnabled(true);

		iconGenerator = new IconGenerator(RequestOpenActivity.this);

		ModelCallback<User> markerFactoryCallback = new ModelCallback<User>() {

			@Override
			public void onModelFound(User user) {
				map.addMarker(MapUtil.getSpeechBubbleMarkerOptions(user.getGoogleMapsLocation(),
						user.getName(), iconGenerator, SpeechBubble.ColorType.BLACK));
			}

			@Override
			public void onModelError(Throwable e) {
				RequestOpenActivity.this.onError(e);
			}
		};

		User.getCurrentUser().findOthersByType(User.Type.VENDOR, markerFactoryCallback);

		mapReadyToPan = true;

	}

	@Override
	public void onError(Throwable t) {
		ErrorUtil.log(this, t);
	}

	@Override
	public void onLocationChanged(Location location) {
		super.onLocationChanged(location);
		LatLng position = MapUtil.locationToLatLng(location);
		if (mapReadyToPan) {
			mapReadyToPan = false;
			marker = map.addMarker(
				MapUtil.getSpeechBubbleMarkerOptions(position,
					getResources().getString(R.string.you),
					iconGenerator, SpeechBubble.ColorType.PURPLE
				));
			MapUtil.panAndZoomToLocation(map, location, MapUtil.DEFAULT_ZOOM_LEVEL);
		} else if (marker != null && !marker.getPosition().equals(position)) {
			marker.setPosition(position);
		}
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