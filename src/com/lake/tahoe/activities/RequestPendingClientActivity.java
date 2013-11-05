package com.lake.tahoe.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.lake.tahoe.R;
import com.lake.tahoe.models.Request;
import com.lake.tahoe.utils.ManifestReader;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import java.security.InvalidParameterException;

/**
 * Created on 11/5/13.
 */
public class RequestPendingClientActivity extends RequestPendingActivity {

	private static final int PAYPAL_PAYMENT_REQUEST_CODE = 12345;

	Bundle metadata;
	Request pendingRequest;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		metadata = ManifestReader.getPackageMetaData(this);

		// start the PayPal service
		Intent intent = new Intent(this, PayPalService.class);
		intent.putExtra(PaymentActivity.EXTRA_CLIENT_ID, metadata.getString("com.paypal.sdk.CLIENT_ID"));
		intent.putExtra(PaymentActivity.EXTRA_PAYPAL_ENVIRONMENT, metadata.getString("com.paypal.sdk.ENVIRONMENT"));
		startService(intent);

		tvPay.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				startPaypalActivity();
			}
		});

		ivCheck.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				finish();
			}
		});

	}

	@Override
	protected void onDestroy() {

		// stop the PayPal service
		stopService(new Intent(this, PayPalService.class));
		super.onDestroy();

	}

	@Override
	protected void onPendingRequest(Request request) {
		pendingRequest = request;
		String format = getString(R.string.you_owe);
		String name = request.getVendor().getName();
		tvSurText.setText(String.format(format, name));
		tvPay.setVisibility(View.VISIBLE);
	}

	public void startPaypalActivity() {
		Intent intent = new Intent(this, PaymentActivity.class);
		intent.putExtra(PaymentActivity.EXTRA_CLIENT_ID, metadata.getString("com.paypal.sdk.CLIENT_ID"));
		intent.putExtra(PaymentActivity.EXTRA_PAYPAL_ENVIRONMENT, metadata.getString("com.paypal.sdk.ENVIRONMENT"));
		intent.putExtra(PaymentActivity.EXTRA_PAYER_ID, pendingRequest.getClient().getEmail());
		intent.putExtra(PaymentActivity.EXTRA_RECEIVER_EMAIL, pendingRequest.getVendor().getEmail());
		intent.putExtra(PaymentActivity.EXTRA_PAYMENT, pendingRequest.getPaypalPayment());
		startActivityForResult(intent, PAYPAL_PAYMENT_REQUEST_CODE);
	}

	@Override
	protected void onActivityResult (int requestCode, int resultCode, Intent data) {

		if (requestCode != PAYPAL_PAYMENT_REQUEST_CODE)
			return;

		if (resultCode == PaymentActivity.RESULT_PAYMENT_INVALID) {
			onError(new InvalidParameterException("An invalid payment was submitted"));
			return;
		}

		// let the user try to pay again if we didn't get success
		if (resultCode != Activity.RESULT_OK)
			return;

		// ensure that we can get a payment confirmation
		PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
		if (confirm == null)
			return;

		// TODO
		// at this point we would send the "confirm" object to our server (confirm.toJSONObject()), independently
		// verify its authenticity, and change the state of the Request off-device.
		// https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment

		// for now, we'll just assume its valid and update the view
		fulfillRequest();

	}

	public void fulfillRequest() {
		pendingRequest.setState(Request.State.FULFILLED);
		pendingRequest.saveEventually();
		tvPay.setVisibility(View.INVISIBLE);
		tvSurText.setText("");
		tvSubText.setText("Payment Complete!");
		ivCheck.setVisibility(View.VISIBLE);
	}

}
