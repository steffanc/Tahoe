package com.lake.tahoe.views;

import android.text.Editable;
import android.text.TextWatcher;

import java.text.NumberFormat;

/**
 * Created by rhu on 11/8/13.
 */
// From Android Recipes
// http://books.google.com/books?id=6aGS01sC33gC&pg=PA149&lpg=PA149&dq=currency+textwatcher+android&source=bl&ots=gdYxKISfs7&sig=_Qazxd4cBnc5x6LD5bJne5wWRgE&hl=en&sa=X&ei=W618UsX3GIaMigLIyoGACA&ved=0CH0Q6AEwCQ#v=onepage&q=currency%20textwatcher%20android&f=false

public class CurrencyTextWatcher implements TextWatcher {
	boolean mEditing;

	public CurrencyTextWatcher() {
		mEditing = false;
	}

	public synchronized void afterTextChanged(Editable s) {
		if (mEditing)
			return;

		mEditing = true;
		String digits = s.toString().replaceAll("\\D", "");
		NumberFormat nf = NumberFormat.getCurrencyInstance();
		try {
			String formatted = nf.format(Double.parseDouble(digits)/100);
			s.replace(0, s.length(), formatted);
		} catch (NumberFormatException nfe) {
			s.clear();
		}
		mEditing = false;
	}

	public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

	public void onTextChanged(CharSequence s, int start, int count, int after) {}

}
