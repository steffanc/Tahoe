package com.lake.tahoe.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import com.lake.tahoe.R;

/**
 * Created by rhu on 11/9/13.
 */
public class BlockerDialog extends Dialog {

	public BlockerDialog(Context ctx) {
		super(ctx);
	}

	public BlockerDialog(Context ctx, int style) {
		super(ctx, style);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_blocker); // progress dialog without text
	}
}
