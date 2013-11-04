package com.lake.tahoe.activities;

import android.content.BroadcastReceiver;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;
import com.lake.tahoe.R;
import com.lake.tahoe.channels.UserUpdateChannel;
import com.lake.tahoe.models.Request;
import com.lake.tahoe.models.User;
import com.lake.tahoe.utils.ErrorUtil;
import com.lake.tahoe.utils.HandlesErrors;
import com.lake.tahoe.utils.Helpers;
import com.lake.tahoe.utils.MapUtil;
import com.lake.tahoe.utils.PushUtil;
import com.lake.tahoe.views.DynamicActionBar;
import com.lake.tahoe.widgets.SpeechBubble;

import java.util.HashMap;

public class RequestMapActivity extends GoogleLocationServiceActivity implements
	UserUpdateChannel.HandlesUserUpdates,
	HandlesErrors {
	
	GoogleMap map;
	DynamicActionBar bar;
	BroadcastReceiver subscription;
	HashMap<Marker, Request> markerRequestMap = new HashMap<Marker, Request>();
	IconGenerator iconGenerator = new IconGenerator(this);
	boolean mapReadyToPan = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_request_map);

		bar = new DynamicActionBar(this, getResources().getColor(R.color.black));
		bar.setTitle(getResources().getString(R.string.select_client));
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

		Request request = Helpers.createMockRequest();
		MarkerOptions markerOptions = MapUtil.getRequestSpeechBubbleMarkerOptions(
				request,
				iconGenerator,
				SpeechBubble.ColorType.BLACK
		);
		Marker marker = map.addMarker(markerOptions);
		markerRequestMap.put(marker, request);

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
			MapUtil.panAndZoomToCurrentUser(map, MapUtil.DEFAULT_ZOOM_LEVEL);
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
			Request request = markerRequestMap.get(marker);

			BitmapDescriptor bitmapDescriptor = SpeechBubble.generateMarkerBitmap(
					iconGenerator,
					request.getDisplayDollars(),
					SpeechBubble.ColorType.BLUE
			);
			marker.setIcon(bitmapDescriptor);

			bar.setTitle(request.getDisplayDollars() + " | " + request.getTitle());
			bar.setBackgroundColor(getResources().getColor(R.color.dark_blue));
			bar.setCheckMarkVisibility(View.VISIBLE, null);
			return true;
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

	@Override
	public void onError(Throwable t) {
		ErrorUtil.log(this, t);
	}
}
