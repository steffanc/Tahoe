package com.lake.tahoe.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.facebook.widget.ProfilePictureView;
import com.lake.tahoe.R;
import com.lake.tahoe.models.Request;
import com.lake.tahoe.models.User;
import com.lake.tahoe.utils.Helpers;
import com.lake.tahoe.views.DynamicActionBar;

/**
 * Created by steffan on 11/4/13.
 */
public class VendorPaymentActivity extends Activity {
	DynamicActionBar bar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.payment_pending);

		// TODO make this use real intents
//		Intent i = getIntent();
//		Request request = i.getSerializableExtra("request");

		User user = User.getCurrentUser();
		Request request = Helpers.createMockRequest();
		request.setVendor(user);

		TextView tvDescription = (TextView) findViewById(R.id.tvDescription);
		String description = request.getClient().getName() + " Owes You";
		tvDescription.setText(description);

		TextView tvCost = (TextView) findViewById(R.id.tvCost);
		String cost = request.getDisplayDollars();
		tvCost.setText(cost);

		bar = new DynamicActionBar(this, getResources().getColor(R.color.light_blue));
		bar.setLeftArrowVisibility(View.VISIBLE, null);
		bar.setCheckMarkVisibility(View.VISIBLE, null);
	}
}
