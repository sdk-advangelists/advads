package com.advangelists.android.compoundviews;


import android.text.Editable;
import android.view.View;

/**
 * Created by arungupta on 04/07/16.
 */

public interface ADVEditTextListener {

    public void onTextCleared(ADVEditText advEditText);
    public void beforeTextChanged(ADVEditText advEditText, CharSequence charSequence, int i, int i1, int i2);
    public void onTextChanged(ADVEditText advEditText, CharSequence charSequence, int i, int i1, int i2);
    public void afterTextChanged(ADVEditText advEditText, Editable editable);
    public void onFocusChange(ADVEditText advEditText, View view, boolean b);

}
