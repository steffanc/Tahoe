package com.lake.tahoe.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.lake.tahoe.models.Request;
import com.lake.tahoe.utils.ErrorUtil;
import com.lake.tahoe.utils.HandlesErrors;
import com.lake.tahoe.utils.Helpers;
import com.lake.tahoe.utils.ManifestReader;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import java.security.InvalidParameterException;

public class PendingPaymentActivity extends Activity implements HandlesErrors {

	private static final int PAYPAL_PAYMENT_REQUEST_CODE = 12345;

	Bundle metadata;
	Request currentRequest;

	public void startPaypalActivity() {
		Intent intent = new Intent(this, PaymentActivity.class);
		intent.putExtra(PaymentActivity.EXTRA_CLIENT_ID, metadata.getString("com.paypal.sdk.CLIENT_ID"));
		intent.putExtra(PaymentActivity.EXTRA_PAYPAL_ENVIRONMENT, metadata.getString("com.paypal.sdk.ENVIRONMENT"));
		intent.putExtra(PaymentActivity.EXTRA_PAYER_ID, currentRequest.getClient().getEmail());
		intent.putExtra(PaymentActivity.EXTRA_RECEIVER_EMAIL, currentRequest.getVendor().getEmail());
		intent.putExtra(PaymentActivity.EXTRA_PAYMENT, currentRequest.getPaypalPayment());
		startActivityForResult(intent, PAYPAL_PAYMENT_REQUEST_CODE);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		metadata = ManifestReader.getPackageMetaData(this);

		// start the PayPal service
		Intent intent = new Intent(this, PayPalService.class);
		intent.putExtra(PaymentActivity.EXTRA_CLIENT_ID, metadata.getString("com.paypal.sdk.CLIENT_ID"));
		intent.putExtra(PaymentActivity.EXTRA_PAYPAL_ENVIRONMENT, metadata.getString("com.paypal.sdk.ENVIRONMENT"));
		startService(intent);

		// TODO: This activity should (eventually) be the blue screen that tells
		// A client that he needs to pay, tells a vendor that payment is pending,
		// and shows the status of the aforementioned transactions. When a Client
		// opts to pay, he can click a button which should call the following
		// method, starting the paypal payment flow
		// Sandbox mode uses this account: jazoff-facilitator@gmail.com / codepath
		currentRequest = Helpers.createMockRequest();
		startPaypalActivity();

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
		// TODO: maybe show something
		if (resultCode != Activity.RESULT_OK)
			return;

		// ensure that we can get a payment confirmation
		PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
		if (confirm == null)
			return;

		// at this point we would send the "confirm" object to our server (confirm.toJSONObject()), independently
		// verify its authenticity, and change the state of the Request off-device.
		// https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment

		// for now, we'll just assume its valid and kick back to the beginning
		startActivity(new Intent(this, DelegateActivity.class));


	}

	@Override
	protected void onDestroy() {

		// stop the PayPal service
		stopService(new Intent(this, PayPalService.class));
		super.onDestroy();

	}

	@Override
	public void onError(Throwable t) {
		ErrorUtil.log(this, t);
	}
}
