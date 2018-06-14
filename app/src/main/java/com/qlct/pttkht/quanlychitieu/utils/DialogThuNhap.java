package com.qlct.pttkht.quanlychitieu.utils;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.qlct.pttkht.quanlychitieu.R;

public class DialogThuNhap extends Dialog {

    private Context mContext;
    private TextView txtSave, txtCancel;
    private EditText edtAmount;
    private IClick mListener;
    public DialogThuNhap(@NonNull Context context) {
        super(context);
        this.mContext = context;
        setContentView(R.layout.dialog_edittaichinh);
        initView();
    }

    private void initView() {
        txtCancel = findViewById(R.id.txtCancleET);
        txtSave = findViewById(R.id.txtSaveET);
        edtAmount = findViewById(R.id.edtAmountET);

        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCancle();
            }
        });

        txtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = edtAmount.getText().toString();
                if (TextUtils.isEmpty(amount))
                    edtAmount.setError("Bắt buộc");
                else mListener.editAmount(Double.parseDouble(amount));
            }
        });


    }

    public interface IClick {
        void editAmount (double amount);

        void onCancle ();
    }

    public void setmListener(IClick mListener) {
        this.mListener = mListener;
    }
}
