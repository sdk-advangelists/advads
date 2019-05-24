package com.advangelists.android.advadssample;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.advangelists.android.compoundviews.ADVEditText;
import com.advangelists.android.compoundviews.ADVEditTextListener;

import java.util.ArrayList;
import java.util.Arrays;

public class SettingsActivity extends AppCompatActivity implements ADVEditTextListener {

    private ArrayList<Integer> mEditTextCountArr;
    private ArrayList<View> contentViews;
    private final int count = 5;
    private Menu mMenu;
    LinearLayout mUserProfileLinearlayout;
    static boolean isVisible;
    final static String sharedPreferencesFileName = "advSettings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mUserProfileLinearlayout = (LinearLayout) findViewById(R.id.userProfile_Layout);
        LayoutInflater inflater = getLayoutInflater();
        String screenName = "settings";
        mEditTextCountArr = new ArrayList<Integer>(Arrays.asList(new Integer[count]));
        contentViews = new ArrayList<View>(Arrays.asList(new View[count]));

        for (int i =0; i<count; i++){
            View view = inflater.inflate(R.layout.adv_edittext_listcell, mUserProfileLinearlayout, false);
            ADVEditText rowView = (ADVEditText) view.findViewById(R.id.advEditText_cell);
            rowView.setTag(i);
            rowView.initialise(i, false, true, this, screenName);
            EditText editText = (EditText) rowView.findViewById(R.id.editText);
            TextView textView = rowView.findViewById(R.id.textView);
            switch (i){
                case 0:
                    editText.setHint("Enter your partner Id");
                    textView.setText("Partner Id");
                    break;
                case 1:
                    editText.setHint("Enter your bundle Id");
                    textView.setText("Bundle Id");
                    break;
                case 2:
                    editText.setHint("Enter app category");
                    textView.setText("App category");
                    break;
                case 3:
                    editText.setHint("Enter test url");
                    textView.setText("");
                    break;
                case 4:
                    editText.setHint("Enter ad placement Id");
                    textView.setText("");
                    break;
            }
            mUserProfileLinearlayout.addView(view);
            contentViews.add(i, view);
            mEditTextCountArr.set(i, 0);
        }

        if (savedInstanceState!=null){
            setEditTextValue(0, savedInstanceState.getString("partnerId"));
            setEditTextValue(1, savedInstanceState.getString("bundleId"));
            setEditTextValue(2, savedInstanceState.getString("cat"));
            setEditTextValue(3, savedInstanceState.getString("testUrl"));
            setEditTextValue(4, savedInstanceState.getString("placementId"));
        }else {
            SharedPreferences sharedPreferences = getSharedPreferences(sharedPreferencesFileName, Context.MODE_PRIVATE);
            setEditTextValue(0, sharedPreferences.getString("partnerId", ""));
            setEditTextValue(1, sharedPreferences.getString("bundleId", ""));
            setEditTextValue(2, sharedPreferences.getString("cat", ""));
            setEditTextValue(3, sharedPreferences.getString("testUrl", ""));
            setEditTextValue(4, sharedPreferences.getString("placementId", ""));
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("partnerId", getEditTextValue(0));
        outState.putString("bundleId", getEditTextValue(1));
        outState.putString("cat", getEditTextValue(2));
        outState.putString("testUrl", getEditTextValue(3));
        outState.putString("placementId", getEditTextValue(4));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.ok).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    private void checkSaveButtonVisibility(){
        if (mMenu!=null){
            if (mEditTextCountArr==null){
                mMenu.findItem(R.id.ok).setVisible(false);
            }else{
                mMenu.findItem(R.id.ok).setVisible(true);
            }
        }
    }

    @Override
    public void onTextCleared(ADVEditText advEditText) {
        mEditTextCountArr.set(advEditText.getIndex(), 0);
        checkSaveButtonVisibility();
    }

    public String getEditTextValue(int index){
        ADVEditText rowView = (ADVEditText) contentViews.get(index).findViewById(R.id.advEditText_cell);
        return rowView.getText();
    }

    public void setEditTextValue(int index, String text){
        ADVEditText rowView = (ADVEditText) contentViews.get(index).findViewById(R.id.advEditText_cell);
        rowView.setText(text);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.ok:
                if (checkValidations()){
                    SharedPreferences sharedPreferences = getSharedPreferences(sharedPreferencesFileName, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("partnerId", getEditTextValue(0));
                    editor.putString("bundleId", getEditTextValue(1));
                    editor.putString("cat", getEditTextValue(2));
                    editor.putString("testUrl", getEditTextValue(3));
                    editor.putString("placementId", getEditTextValue(4));
                    editor.putBoolean("isSettingsSaved", true);
                    editor.commit();
                    finish();
                }

        }

        return super.onOptionsItemSelected(item);
    }

    private boolean checkValidations(){
        return true;
    }


    @Override
    public void beforeTextChanged(ADVEditText advEditText, CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(ADVEditText advEditText, CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(ADVEditText advEditText, Editable editable) {
        mEditTextCountArr.set(advEditText.getIndex(), editable.length());
        checkSaveButtonVisibility();
    }




    @Override
    public void onFocusChange(ADVEditText advEditText, View view, boolean b) {

    }
}
