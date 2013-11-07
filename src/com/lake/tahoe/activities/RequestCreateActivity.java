package com.lake.tahoe.activities;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.ui.IconGenerator;
import com.lake.tahoe.R;
import com.lake.tahoe.channels.UserUpdateChannel;
import com.lake.tahoe.models.Request;
import com.lake.tahoe.models.User;
import com.lake.tahoe.utils.Currency;
import com.lake.tahoe.utils.ErrorUtil;
import com.lake.tahoe.utils.HandlesErrors;
import com.lake.tahoe.utils.MapUtil;
import com.lake.tahoe.views.DynamicActionBar;
import com.lake.tahoe.widgets.SpeechBubble;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import org.json.JSONException;

public class RequestCreateActivity extends GoogleLocationServiceActivity implements HandlesErrors {

	GoogleMap map;
	Marker marker;
	Request request;

	public static final int NEW_REQUEST = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_request_create);
		DynamicActionBar actionBar = new DynamicActionBar(RequestCreateActivity.this, getResources().getColor(R.color.black));
		actionBar.setTitle("Create a Request");
		actionBar.setCheckMarkVisibility(View.VISIBLE, new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				createRequest();

			}
		});

		actionBar.setButtonText(User.Type.VENDOR.toString());

	}


	public void createRequest() {
		TextView title = (TextView) findViewById(R.id.wantText);
		TextView amt = (TextView) findViewById(R.id.rewardText);
		TextView description = (TextView) findViewById(R.id.anythingElseText);

		request = new Request(Request.State.OPEN);
		request.setTitle(title.getText().toString());
		request.setDescription(description.getText().toString());

		String amtText = amt.getText().toString();

		Float amount = Currency.getAmount(amtText);

		if (amount > 0) {
			request.setCents((int) (amount * 100));
		}

		User client = (User) ParseUser.getCurrentUser();

		request.setClient(client);
		request.saveEventually(new OnRequestCreated());
	}

	class OnRequestCreated extends SaveCallback {
		@Override
		public void done(ParseException e) {
			//TODO: startRequestOpenActivity
			if (e == null) {
				try {
					UserUpdateChannel.publish(RequestCreateActivity.this, User.getCurrentUser());
				} catch (JSONException e1) {
					onError(e1);
				}
				Intent i = new Intent(RequestCreateActivity.this, RequestOpenActivity.class);
				startActivityForResult(i, NEW_REQUEST);
			} else onError(e);

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == NEW_REQUEST && resultCode == RESULT_CANCELED) {
			Log.d("debug", "Cancelling the request");
			request.setState(Request.State.CANCELLED);
			request.saveEventually();
		}
	}

	protected void onGooglePlayServicesReady() {
		SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		map = fragment.getMap();
		map.setMyLocationEnabled(true);
		map.getUiSettings().setZoomControlsEnabled(false);
		map.getUiSettings().setMyLocationButtonEnabled(false);
	}

	@Override
	public void onLocationChanged(Location location) {
		super.onLocationChanged(location);
		IconGenerator iconGenerator = new IconGenerator(this);
		LatLng position = MapUtil.locationToLatLng(location);
		if (marker == null) {
			marker = map.addMarker(MapUtil.getSpeechBubbleMarkerOptions(
				position,
				getResources().getString(R.string.you),
				iconGenerator, SpeechBubble.ColorType.PURPLE
			));
		} else {
			marker.setPosition(position);
		}
		MapUtil.panAndZoomToLocation(map, location, MapUtil.DEFAULT_ZOOM_LEVEL);
	}

	@Override
	public void onError(Throwable t) {
		ErrorUtil.log(this, t);
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
