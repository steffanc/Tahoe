package com.lake.tahoe.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created on 10/30/13.
 */
public class SimpleDialogFragment extends DialogFragment {

	private Dialog mDialog;

	public SimpleDialogFragment() {
		super();
		mDialog = null;
	}

	public void setDialog(Dialog dialog) {
		mDialog = dialog;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return mDialog;
	}

}
