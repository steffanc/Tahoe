package com.lake.tahoe.activities;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import com.lake.tahoe.utils.*;
import com.lake.tahoe.widgets.SpeechBubble;

import java.util.HashMap;

public class RequestMapActivity extends GoogleLocationServiceActivity implements
	UserUpdateChannel.HandlesUserUpdates,
	HandlesErrors {
	
	GoogleMap map;
	ActionBar actionBar;
	BroadcastReceiver subscription;
	HashMap<Marker, Request> markerRequestMap = new HashMap<Marker, Request>();
	IconGenerator iconGenerator = new IconGenerator(this);
	boolean mapReadyToPan = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_request_map);
		actionBar = getActionBar();
		if (actionBar != null) {
			actionBar.setDisplayShowHomeEnabled(false);
			actionBar.setTitle(R.string.select_client);
		}
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.vendor_menu, menu);
		return true;
	}

	public void onConfirmClick(MenuItem menuItem) {
		// TODO go to next activity
	}

	@Override
	protected void onGooglePlayServicesReady() {
		SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		map = fragment.getMap();
		Request request = Helpers.createMockRequest();
		MarkerOptions markerOptions = MapUtil.getSpeechBubbleMarkerOptions(
				request, iconGenerator, SpeechBubble.ColorType.BLACK);
		Marker marker = map.addMarker(markerOptions);
		markerRequestMap.put(marker, request);
		map.setMyLocationEnabled(true);
		map.setOnMarkerClickListener(new OnMarkerClick());
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
					marker.getTitle(),
					SpeechBubble.ColorType.BLUE
			);
			marker.setIcon(bitmapDescriptor);

			if (actionBar != null) {
				actionBar.setTitle(marker.getTitle() + " | " + request.getTitle());
				actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(4, 46, 60)));
			}
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
