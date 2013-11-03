package com.lake.tahoe.activities;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;
import com.lake.tahoe.R;
import com.lake.tahoe.models.Request;
import com.lake.tahoe.models.User;
import com.lake.tahoe.utils.ErrorUtil;
import com.lake.tahoe.utils.HandlesErrors;
import com.lake.tahoe.widgets.SpeechBubble;

import java.util.HashMap;

public class RequestMapActivity extends GoogleLocationServiceActivity implements HandlesErrors {
	TextView tvTitle;
	GoogleMap map;
	HashMap<Marker, Request> markerRequestMap = new HashMap<Marker, Request>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_request_map);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setTitle(R.string.select_client);
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
		map.setMyLocationEnabled(true);
	}

	@Override
	public void onConnected(Bundle dataBundle) {
		super.onConnected(dataBundle);
		zoomToUser(map);

		// Sample data for now
		// TODO remove this sample data in the future
		// TODO move this initialization to the constructor
		User client1 = new User();
		client1.setLocation(37.7583, -122.4275);
		Request request1 = new Request();
		request1.setTitle("Beer Wanted");
		request1.setClient(client1);
		request1.setCents(100);

		SpeechBubble speechBubble = new SpeechBubble(
				"$" + Integer.toString(request1.getCents()),
				request1.getGoogleMapsLocation(),
				SpeechBubble.ColorType.BLACK
		);

		MarkerOptions markerOptions = speechBubble.generateMarker(new IconGenerator(this));

		Marker marker = map.addMarker(markerOptions);

		markerRequestMap.put(marker,request1);

		map.setOnMarkerClickListener(new OnMarkerClick());
	}

	// TODO probably move this up to the google location service parent class
	// TODO consider making speech bubble a subclass of marker
	// TODO go through other markers and make them non blue when one is clicked
	private class OnMarkerClick implements GoogleMap.OnMarkerClickListener {
		@Override
		public boolean onMarkerClick(Marker marker) {
			Request request = markerRequestMap.get(marker);
			SpeechBubble speechBubble = new SpeechBubble(
					"$" + Integer.toString(request.getCents()),
					request.getGoogleMapsLocation(),
					SpeechBubble.ColorType.BLUE
			);

			Bitmap bitmap = speechBubble.generateBitmap(new IconGenerator(getBaseContext()));
			BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);
			marker.setIcon(bitmapDescriptor);

			getActionBar().setTitle("$" + request.getCents() + " | " + request.getTitle());
			getActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(4, 46, 60)));
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
