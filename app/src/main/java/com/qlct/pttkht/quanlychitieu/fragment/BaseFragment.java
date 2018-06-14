package com.qlct.pttkht.quanlychitieu.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.qlct.pttkht.quanlychitieu.R;
import com.qlct.pttkht.quanlychitieu.activity.SignInActivity;
import com.syd.oden.circleprogressdialog.core.CircleProgressDialog;


public class BaseFragment extends Fragment {

    private CircleProgressDialog mProgressDialog;
    private FirebaseAuth mAuth;
    public static String emaiUser;
    public static String idUser;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProgressDialog = new CircleProgressDialog(getContext());
        mAuth = FirebaseAuth.getInstance();
        isAuthencated();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isAuthencated()){
            Toast.makeText(getContext(), "Đã đăng xuất", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getContext(), SignInActivity.class));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base, container, false);
        // Inflate the layout for this fragment
        return view;
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

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
