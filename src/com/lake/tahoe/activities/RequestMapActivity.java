package com.lake.tahoe.activities;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
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
import com.lake.tahoe.utils.ErrorUtil;
import com.lake.tahoe.utils.HandlesErrors;
import com.lake.tahoe.utils.MapUtil;
import com.lake.tahoe.utils.PushUtil;
import com.lake.tahoe.views.DynamicActionBar;
import com.lake.tahoe.widgets.SpeechBubble;
import com.parse.ParseUser;

import java.util.HashMap;

public class RequestMapActivity extends GoogleLocationServiceActivity implements
	UserUpdateChannel.HandlesUserUpdates,
	HandlesErrors {
	
	GoogleMap map;
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

		actionBar = new DynamicActionBar(this, getResources().getColor(R.color.black));
		actionBar.setTitle(getResources().getString(R.string.select_client));
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
		//TODO: show the updated user on the map!
		Log.d("USER UPDATE", user.getLocation().toString());
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

		ModelCallback<Request> markerFactoryCallback = new ModelCallback<Request>() {

			@Override
			public void onModelFound(Request request) {

				Marker marker = map.addMarker(MapUtil.getSpeechBubbleMarkerOptions(request.getClient(),
						iconGenerator, SpeechBubble.ColorType.BLACK));
				markerRequestMap.put(marker, request);

			}

			@Override
			public void onModelError(Throwable e) {
				RequestMapActivity.this.onError(e);
			}
		};

		User user = (User) ParseUser.getCurrentUser();
		user.findNearbyRequests(Request.State.OPEN, markerFactoryCallback);

		map.addMarker(MapUtil.getSpeechBubbleMarkerOptions(user.getGoogleMapsLocation(),
				getResources().getString(R.string.you), iconGenerator, SpeechBubble.ColorType.PURPLE));

		MapUtil.panAndZoomToCurrentUser(map, MapUtil.DEFAULT_ZOOM_LEVEL);
	}

	/**
	 * TODO We should probably encapsulate this behavior in other classes that use a map.
	 *  See the comment on OnMarkerClick (below) about GoogleMapActivity
	 */
	@Override
	public void onLocationChanged(Location location) {
		super.onLocationChanged(location);
		if (mapReadyToPan) {
			LatLng point = new LatLng(location.getLatitude(), location.getLongitude());
			MapUtil.panAndZoomToPoint(map, point, MapUtil.DEFAULT_ZOOM_LEVEL);
			mapReadyToPan = false;
		}
	}

	// TODO probably move this up to the google location service parent class
	//      ^ maybe make a subclass of GoogleLocationServiceActivity called GoogleMapActivity that encapsulates this
	//        stuff. The GoogleLocationServiceActivity only tracks geo, which may not imply it has a map.
	// TODO go through other markers and make them non blue when one is clicked
	private class OnMarkerClick implements GoogleMap.OnMarkerClickListener {
		@Override
		public boolean onMarkerClick(Marker marker) {
			final Request request = markerRequestMap.get(marker);

			if (request == null) {
				Log.d("debug", "Ignoring tag since it's not in the Hash...must be clicking on You");
				return true;
			}

			BitmapDescriptor bitmapDescriptor = SpeechBubble.generateMarkerBitmap(
					iconGenerator,
					request.getDisplayDollars(),
					SpeechBubble.ColorType.BLUE
			);
			marker.setIcon(bitmapDescriptor);

			actionBar.setTitle(request.getDisplayDollars() + " | " + request.getTitle());
			actionBar.setBackgroundColor(getResources().getColor(R.color.dark_blue));
			actionBar.setRightArrowVisibility(View.VISIBLE, new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					RequestMapActivity.this.launchDetailActivityIntent(request);
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

	@Override
	public void onError(Throwable t) {
		ErrorUtil.log(this, t);
	}
}
