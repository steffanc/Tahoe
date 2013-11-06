package com.lake.tahoe.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import android.widget.Toast;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.maps.android.ui.IconGenerator;
import com.lake.tahoe.R;
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

public class RequestCreateActivity extends GoogleLocationServiceActivity implements HandlesErrors {

	GoogleMap map;
	Request request;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_request_create);

		DynamicActionBar actionBar = new DynamicActionBar(RequestCreateActivity.this, getResources().getColor(R.color.black));

		actionBar.setTitle("Create a Request");
		actionBar.setCheckMarkVisibility(View.VISIBLE, new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// String comparisons are ugly,
				if (v.getTag().toString().equals(getString(R.string.checkMark))) {
					createRequest();
				}
			}
		});
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
		@Override public void done(ParseException e) {
			//TODO: startRequestOpenActivity
			if (e == null) Toast.makeText(
					RequestCreateActivity.this,
					"Request " + request.getObjectId() + " Created!",
					Toast.LENGTH_SHORT).show();
			else onError(e);

		}
	}

	protected void onGooglePlayServicesReady() {

		SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		map = fragment.getMap();
	    map.setMyLocationEnabled(true);
		map.getUiSettings().setZoomControlsEnabled(false);
		map.getUiSettings().setMyLocationButtonEnabled(false);

		User user = User.getCurrentUser();
		MapUtil.panAndZoomToUser(map, user, MapUtil.DEFAULT_ZOOM_LEVEL);

		IconGenerator iconGenerator = new IconGenerator(this);
		map.addMarker(MapUtil.getSpeechBubbleMarkerOptions(user.getGoogleMapsLocation(),
						getResources().getString(R.string.you), iconGenerator, SpeechBubble.ColorType.PURPLE));
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
