package com.qlct.pttkht.quanlychitieu.utils;


import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.qlct.pttkht.quanlychitieu.R;

/**
 * Created by thaopt on 9/27/17.
 */

public class DialogViewMoreUtil extends Dialog {
    private TextView firstTextView, secondTexView, threeTextview;
    private ImageView firstImageView, secondImageView, threeImageView;
    private View threeView;
    private Context mContext;

    public DialogViewMoreUtil(@NonNull Context context) {
        super(context);
        this.mContext = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_view_more);
        initView();
    }
    public void setText(String first, String second) {
        firstTextView.setText(first);
        secondTexView.setText(second);
    }


    private void initView() {
        firstTextView = findViewById(R.id.moreFirstTextView);
        secondTexView = findViewById(R.id.moreSecondTextView);
        firstImageView = findViewById(R.id.moreFirstImageView);
        secondImageView = findViewById(R.id.moreSecondImageView);

        findViewById(R.id.moreFirstView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.firstViewClick();
                dismiss();
            }
        });
        findViewById(R.id.moreSecondView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.secondViewClick();
                dismiss();
            }
        });
    }


    //call back

    public interface DialogViewMoreOnClick {
        void firstViewClick();

        void secondViewClick();

    }

    private DialogViewMoreOnClick mListener;

    public void setDialogViewMoreOnClick(DialogViewMoreOnClick buttonOnClick) {
        this.mListener = buttonOnClick;
    }
}
