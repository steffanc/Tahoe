package com.lake.tahoe.activities;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.ui.IconGenerator;
import com.lake.tahoe.R;
import com.lake.tahoe.models.Request;
import com.lake.tahoe.models.User;
import com.lake.tahoe.utils.ErrorUtil;
import com.lake.tahoe.utils.HandlesErrors;
import com.lake.tahoe.widgets.SpeechBubble;

public class RequestMapActivity extends GoogleLocationServiceActivity implements HandlesErrors {
	TextView tvTitle;
	GoogleMap map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_request_map);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
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
		request1.setClient(client1);
		request1.setCents(100);

		map.addMarker(
				new SpeechBubble(
						"$" + Integer.toString(request1.getCents()),
						request1.getGoogleMapsLocation(),
						SpeechBubble.ColorType.BLACK)
						.generateMarker(new IconGenerator(this))
		);

		map.setOnMarkerClickListener(new OnMarkerClick());
	}

	private class OnMarkerClick implements GoogleMap.OnMarkerClickListener {
		@Override
		public boolean onMarkerClick(Marker marker) {
			// TODO start new activity here
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
