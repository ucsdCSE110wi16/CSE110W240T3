package edu.fe.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wang.avi.AVLoadingIndicatorView;

import edu.fe.R;

public class LoadingDialog extends LinearLayout {

    AVLoadingIndicatorView mLoadingIndicator;
    TextView mInfoDump;

    public LoadingDialog(Context context) {
        super(context);
        this.init();
    }

    public LoadingDialog(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.init();
    }

    public LoadingDialog(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.init();
    }

    private void init() {
        mLoadingIndicator = (AVLoadingIndicatorView) findViewById(R.id.avl_loading_indicator);
        mInfoDump = (TextView) findViewById(R.id.loading_dialog_text);
    }

    public LoadingDialog start() {
        mLoadingIndicator.setVisibility(View.VISIBLE);
        return this;
    }

    public LoadingDialog stop() {
        mLoadingIndicator.setVisibility(View.GONE);
        return this;
    }

    public void setLoadingText(CharSequence sequence) {
        mInfoDump.setText(sequence);
    }
}
