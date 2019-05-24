package com.advangelists.android.compoundviews;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.advangelists.android.advadssample.R;

/**
 * Created by arungupta on 28/06/16.
 */

public class ADVEditText extends RelativeLayout implements View.OnClickListener, TextWatcher, EditText.OnFocusChangeListener {

    private ImageView iconImageView;
    private TextView hintTextView;
    private EditText editText;
    private TextView optionalLabel;
    private Button clearButton;
    private View unHighlightedView;
    private View highlightedView;
    private boolean isHighlighted;
    private boolean showIcon;
    private boolean showClearButton;
    private boolean showOptional;
    private int index;
    private String screenName;
    private ADVEditTextListener listener;

    public ADVEditText(Context context) {
        this(context, null);
    }

    public ADVEditText(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.advEditTextStyle);
    }

    public ADVEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.compound_adv_edittext, this, true);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        iconImageView = (ImageView) this.findViewById(R.id.imageView);
        hintTextView = (TextView) this.findViewById(R.id.textView);
        editText = (EditText) this.findViewById(R.id.editText);
        optionalLabel = (TextView) this.findViewById(R.id.optionalTextView);
        clearButton = (Button) this.findViewById(R.id.button);
        unHighlightedView = (View) this.findViewById(R.id.unhighlightIndicatorView);
        highlightedView = (View) this.findViewById(R.id.highlightIndicatorView);
        clearButton.setVisibility(INVISIBLE);
        optionalLabel.setVisibility(INVISIBLE);
        clearButton.setOnClickListener(this);
        editText.setOnFocusChangeListener(this);
        editText.addTextChangedListener(this);
    }

    protected void configureView() {
        if (showOptional)
            showOptionalLabel();
//        editText.setHint(getResources().getString(getResources().getIdentifier(screenName + "_textViewHint_" + index, "string", getContext().getPackageName())));
//        hintTextView.setText(getResources().getString(getResources().getIdentifier(screenName + "_textViewLabel_" + index, "string", getContext().getPackageName())));
        if (editText.getText().length() > 0) {
            highlight();
        } else {
            unHighlight();
        }

    }

    public void highlight() {
        isHighlighted = true;
        highlightImageIcon();
        highlightedView.setVisibility(VISIBLE);
        unHighlightedView.setVisibility(INVISIBLE);
    }

    private void highlightImageIcon(){
//        iconImageView.setImageDrawable(getResources().getDrawable(getResources().getIdentifier(screenName.toLowerCase() + "_highlightedicon_" + index, "drawable", getContext().getPackageName())));

    }

    public void unHighlight() {
        isHighlighted = false;
        if (editText.getText().length() == 0) {
            unhighlightImageIcon();
            hintTextView.setVisibility(INVISIBLE);
        }
        highlightedView.setVisibility(INVISIBLE);
        unHighlightedView.setVisibility(VISIBLE);
    }

    private void unhighlightImageIcon() {
//        iconImageView.setImageDrawable(getResources().getDrawable(getResources().getIdentifier(screenName.toLowerCase() + "_icon_" + index, "drawable", getContext().getPackageName())));
    }

    public void showOptionalLabel() {
        if (showOptional && editText.getText().length() == 0) {
            optionalLabel.setVisibility(VISIBLE);
        }
    }

    public void hideOptionalLabel() {
        if (editText.getText().length() > 0) {
            optionalLabel.setVisibility(GONE);
        }
    }

    public void hideClearButton() {
        clearButton.setVisibility(GONE);
    }

    public void showClearButton() {
        if (editText.getText().length() > 0 && showClearButton) {
            clearButton.setVisibility(VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        editText.getText().clear();
        hideClearButton();
        showOptionalLabel();
        unhighlightImageIcon();
        listener.onTextCleared(this);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        listener.beforeTextChanged(this, charSequence, i, i1, i2);
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        listener.onTextChanged(this, charSequence, i, i1, i2);
    }

    @Override
    public void afterTextChanged(Editable editable) {
        afterTextChanged(editable.length());
        listener.afterTextChanged(this, editable);
    }

    private void afterTextChanged(int length){
        if (length == 0) {
            hideClearButton();
            showOptionalLabel();
            hintTextView.setVisibility(INVISIBLE);
        } else {
            hintTextView.setVisibility(VISIBLE);
            showClearButton();
            hideOptionalLabel();
        }
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (hasFocus) {
            highlight();
            showClearButton();
            hideOptionalLabel();
        } else {
            unHighlight();
        }
        listener.onFocusChange(this, view, hasFocus);
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getText() {
        return editText.getText().toString();
    }

    public void setText(String text) {
        editText.setText(text);
        afterTextChanged(text.length());
        if (text.length()>0){
            highlightImageIcon();
        }else{
            unhighlightImageIcon();
        }
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isShowOptional() {
        return showOptional;
    }

    public void setShowOptional(boolean showOptional) {
        this.showOptional = showOptional;
    }

    public boolean isShowClearButton() {
        return showClearButton;
    }

    public void setShowClearButton(boolean showClearButton) {
        this.showClearButton = showClearButton;
    }

    public boolean isShowIcon() {
        return showIcon;
    }

    public void setShowIcon(boolean showIcon) {
        this.showIcon = showIcon;
    }

    public boolean isHighlighted() {
        return isHighlighted;
    }

    public void setHighlighted(boolean highlighted) {
        isHighlighted = highlighted;
    }

    public ADVEditTextListener getListener() {
        return listener;
    }

    public void setListener(ADVEditTextListener listener) {
        this.listener = listener;
    }

    public void initialise(int index, boolean showOptional, boolean showClearButton, ADVEditTextListener listener, String screenName) {
        setIndex(index);
        setShowOptional(showOptional);
        setShowClearButton(showClearButton);
        setListener(listener);
        setScreenName(screenName);
        configureView();
    }

    public ImageView getIconImageView() {
        return iconImageView;
    }

    public TextView getHintTextView() {
        return hintTextView;
    }

    public EditText getEditText() {
        return editText;
    }

    public TextView getOptionalLabel() {
        return optionalLabel;
    }

    public Button getClearButton() {
        return clearButton;
    }

    public View getUnHighlightedView() {
        return unHighlightedView;
    }

    public View getHighlightedView() {
        return highlightedView;
    }
}
