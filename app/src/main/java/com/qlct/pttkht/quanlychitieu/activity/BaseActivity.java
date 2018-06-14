package com.qlct.pttkht.quanlychitieu.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.qlct.pttkht.quanlychitieu.R;
import com.syd.oden.circleprogressdialog.core.CircleProgressDialog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseActivity extends AppCompatActivity {

    private CircleProgressDialog mProgressDialog;
    private FirebaseAuth mAuth;
    public static String emaiUser;
    public static String idUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        initView ();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isAuthencated();
    }

    private void initView() {
        mProgressDialog = new CircleProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mAuth = FirebaseAuth.getInstance();
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public void showProgress (){
        if (mProgressDialog.isShowing())
            mProgressDialog.dismiss();
        mProgressDialog.showDialog();
    }

    public void hideProgress (){
        mProgressDialog.dismiss();
    }

    public boolean isAuthencated (){
        if (mAuth.getCurrentUser() != null) {
            emaiUser = mAuth.getCurrentUser().getEmail().toString();
            idUser = mAuth.getCurrentUser().getUid().toString();
            return true;
        }
        else return false;
    }
}
