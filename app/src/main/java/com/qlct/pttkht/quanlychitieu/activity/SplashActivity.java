package com.qlct.pttkht.quanlychitieu.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.qlct.pttkht.quanlychitieu.R;
import com.syd.oden.circleprogressdialog.core.CircleProgressDialog;

public class SplashActivity extends BaseActivity {

    private FirebaseAuth mAuth;
    private CircleProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mAuth = FirebaseAuth.getInstance();
        mProgressDialog = new CircleProgressDialog(this);
        mProgressDialog.setCancelable(false);
    }

    @Override
    protected void onResume() {
        checkAuthencation ();
        super.onResume();
    }

    private void checkAuthencation() {
        showProgress();
        //Waiting 3s then move to Main or SignIn
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //If user != null
                if (mAuth.getCurrentUser() != null){
                    hideProgress();
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }else {
                    hideProgress();
                    startActivity(new Intent(SplashActivity.this, SignInActivity.class));
                    finish();
                }
            }
        }, 3000);
    }
}
