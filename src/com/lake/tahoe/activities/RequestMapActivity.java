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
import com.lake.tahoe.utils.Helpers;
import com.lake.tahoe.widgets.SpeechBubble;

import java.util.HashMap;

public class RequestMapActivity extends GoogleLocationServiceActivity implements HandlesErrors {
	GoogleMap map;
	HashMap<Marker, Request> markerRequestMap = new HashMap<Marker, Request>();
	IconGenerator iconGenerator = new IconGenerator(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_request_map);
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

		Request request1 = Helpers.createMockRequest();

		MarkerOptions markerOptions =
				new MarkerOptions()
						.position(request1.getGoogleMapsLocation());

		String title = "$" + Integer.toString(request1.getCents());
		BitmapDescriptor bitmapDescriptor = SpeechBubble.generateMarkerBitmap(
				iconGenerator,
				title,
				SpeechBubble.ColorType.BLACK
		);
		markerOptions.title(title);
		markerOptions.icon(bitmapDescriptor);

		Marker marker = map.addMarker(markerOptions);
		markerRequestMap.put(marker,request1);
		map.setOnMarkerClickListener(new OnMarkerClick());
	}

	// TODO probably move this up to the google location service parent class
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

			getActionBar().setTitle(marker.getTitle() + " | " + request.getTitle());
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
