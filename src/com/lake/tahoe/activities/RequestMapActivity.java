package com.lake.tahoe.activities;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.ui.IconGenerator;
import com.lake.tahoe.R;
import com.lake.tahoe.callbacks.ModelCallback;
import com.lake.tahoe.channels.UserUpdateChannel;
import com.lake.tahoe.models.Request;
import com.lake.tahoe.models.User;
import com.lake.tahoe.utils.AsyncStateUtil;
import com.lake.tahoe.utils.MapUtil;
import com.lake.tahoe.utils.PushUtil;
import com.lake.tahoe.views.DynamicActionBar;
import com.lake.tahoe.widgets.SpeechBubble;
import com.parse.ParseUser;

import java.util.HashMap;

public class RequestMapActivity extends GoogleLocationServiceActivity implements
		UserUpdateChannel.HandlesUserUpdates, ModelCallback<Request> {

	GoogleMap map;
	Marker marker;
	DynamicActionBar actionBar;
	BroadcastReceiver subscription;
	HashMap<Marker, Request> markerRequestMap = new HashMap<Marker, Request>();
	IconGenerator iconGenerator = new IconGenerator(this);
	boolean mapReadyToPan = false;

	final int DETAIL_REQUEST = 0;
	public String REQUEST_ID = "requestId";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_request_map);

		actionBar = new DynamicActionBar(this);
		actionBar.setTitle(getResources().getString(R.string.select_client));

		actionBar.setLeftAction(R.drawable.ic_action_client_mode, new View.OnClickListener() {
			@Override public void onClick(View v) {
				convertToClient();
			}
		});

	}

	private void convertToClient() {
		User user = User.getCurrentUser();
		user.setType(User.Type.CLIENT);
		AsyncStateUtil.saveAndStartActivity(user, this, RequestCreateActivity.class, this);
	}

	@Override
	public void onModelFound(Request request) {

		if (request.getClient() == null) {
			onError(new IllegalStateException("Request does not have a Client"));

		} else {

			Marker marker = map.addMarker(MapUtil.getSpeechBubbleMarkerOptions(request.getClient(),
					iconGenerator, SpeechBubble.ColorType.BLACK));
			markerRequestMap.put(marker, request);
		}
	}

	@Override
	public void onModelError(Throwable e) {
		RequestMapActivity.this.onError(e);
	}

	@Override
	protected void onStart() {
		super.onStart();
		subscription = UserUpdateChannel.subscribe(this, this);
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
	public void onUserUpdated(User user) {
		if (user == null)
			return;
		if (user.getObjectId().equals(User.getCurrentUser().getObjectId()))
			return;
		if (!user.getType().equals(User.Type.CLIENT))
			return;
		user.getUnfinishedRequest(this);
	}

	@Override
	public void onUserUpdateError(Throwable t) {
		onError(t);
	}

	@Override
	protected void onGooglePlayServicesReady() {
		SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		map = fragment.getMap();
		map.setMyLocationEnabled(true);
		map.setOnMarkerClickListener(new OnMarkerClick());

		User user = (User) ParseUser.getCurrentUser();
		user.findNearbyRequests(Request.State.OPEN, this);

		mapReadyToPan = true;

	}

	/**
	 * TODO We should probably encapsulate this behavior in other classes that use a map.
	 * See the comment on OnMarkerClick (below) about GoogleMapActivity
	 */
	@Override
	public void onLocationChanged(Location location) {
		super.onLocationChanged(location);
		LatLng position = MapUtil.locationToLatLng(location);
		if (mapReadyToPan) {
			mapReadyToPan = false;
			MapUtil.panAndZoomToLocation(map, location, MapUtil.DEFAULT_ZOOM_LEVEL);
			marker = map.addMarker(MapUtil.getSpeechBubbleMarkerOptions(position,
					getResources().getString(R.string.you), iconGenerator, SpeechBubble.ColorType.PURPLE));
		} else if (marker != null && !marker.getPosition().equals(position)) {
			marker.setPosition(position);
		}
	}

	private class OnMarkerClick implements GoogleMap.OnMarkerClickListener {
		@Override
		public boolean onMarkerClick(Marker marker) {

			final Request request = markerRequestMap.get(marker);
			if (request == null)
				return true;

			BitmapDescriptor bitmapDescriptor = SpeechBubble.generateMarkerBitmap(
					iconGenerator,
					request.getDisplayDollars(),
					SpeechBubble.ColorType.BLUE
			);
			marker.setIcon(bitmapDescriptor);

			actionBar.setTitle(request.getDisplayDollars() + " | " + request.getTitle());
			actionBar.setBackgroundColor(getResources().getColor(R.color.dark_blue));
			actionBar.setRightArrowAction(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					launchDetailActivityIntent(request);
				}
			});
			return true;
		}
	}

	public void launchDetailActivityIntent(Request request) {
		Intent i = new Intent(RequestMapActivity.this, RequestDetailActivity.class);
		i.putExtra(REQUEST_ID, request.getObjectId());
		startActivityForResult(i, DETAIL_REQUEST);
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
