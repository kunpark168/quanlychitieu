package com.qlct.pttkht.quanlychitieu.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogBuilder;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.qlct.pttkht.quanlychitieu.R;
import com.qlct.pttkht.quanlychitieu.activity.MainActivity;
import com.qlct.pttkht.quanlychitieu.activity.SignInActivity;
import com.qlct.pttkht.quanlychitieu.activity.SplashActivity;
import com.qlct.pttkht.quanlychitieu.model.Chitieu;
import com.qlct.pttkht.quanlychitieu.model.ThuNhap;
import com.qlct.pttkht.quanlychitieu.utils.Constan;

import java.util.Calendar;

public class AccountFragment extends BaseFragment implements View.OnClickListener {


    private TextView txtEmail, txtPasswordSetting, txtLogout, txtTongThu, txtTongChi, txtSoDu;
    private FirebaseAuth mAuth;
    private DatabaseReference mData;
    private double thunhapcodinh = 0, thunhapdotxuat = 0, chitieu = 0;
    private FirebaseUser user;
    private String password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        initView(view);
        getData();
        return view;
    }

    private void getData() {
        password = getActivity().getIntent().getStringExtra(Constan.PASSWORD);
        Calendar now = Calendar.getInstance();
        int mounth = now.get(Calendar.MONTH) + 1;
        //Get thu nhập cố định
        mData.child(Constan.THUNHAP).child(idUser).child(String.valueOf(mounth)).child(Constan.THUNHAP_CODINH).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                thunhapcodinh = 0;
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    ThuNhap thuNhap = data.getValue(ThuNhap.class);
                    thunhapcodinh = thuNhap.getAmount();
                }
                setData();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Get thu nhập đột xuất
        mData.child(Constan.THUNHAP).child(idUser).child(String.valueOf(mounth)).child(Constan.THUNHAP_DOTXUAT).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                thunhapdotxuat = 0;
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    ThuNhap thuNhap = data.getValue(ThuNhap.class);
                    thunhapdotxuat += thuNhap.getAmount();
                }
                setData();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Get chi tiêu
        mData.child(Constan.CHITIEU).child(idUser).child(String.valueOf(mounth)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chitieu = 0;
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Chitieu ct = data.getValue(Chitieu.class);
                    chitieu += ct.getAmount();
                }
                setData();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setData() {
        String tongthu = thunhapcodinh + thunhapdotxuat + "";
        tongthu = tongthu.replace(".", ",");
        if (tongthu.endsWith("0")) {
            tongthu = tongthu.substring(0, tongthu.length() - 2);
        }

        if (tongthu.equals("0"))
            txtTongThu.setText("0 vnd");
        else
            txtTongThu.setText(tongthu + ".000 vnd");

        String tongchi = chitieu + "";
        tongchi = tongchi.replace(".", ",");
        if (tongchi.endsWith("0")) {
            tongchi = tongchi.substring(0, tongchi.length() - 2);
        }
        if (tongchi.equals("0"))
            txtTongChi.setText("0 vnd");
        else
            txtTongChi.setText(tongchi + ".000 vnd");

        String sodu = thunhapcodinh + thunhapdotxuat - chitieu + "";
        sodu = sodu.replace(".", ",");
        if (sodu.endsWith("0")) {
            sodu = sodu.substring(0, sodu.length() - 2);
        }
        if (sodu.equals("0"))
            txtSoDu.setText("0 vnd");
        else
            txtSoDu.setText(sodu + ".000 vnd");
    }

    private void initView(View view) {
        txtEmail = view.findViewById(R.id.txtEmailUser);
        txtLogout = view.findViewById(R.id.txtLogout);
        txtPasswordSetting = view.findViewById(R.id.txtPasswordSetting);
        txtTongChi = view.findViewById(R.id.txtTongChi);
        txtTongThu = view.findViewById(R.id.txtTongThu);
        txtSoDu = view.findViewById(R.id.txtSodu);

        mAuth = FirebaseAuth.getInstance();

        txtLogout.setOnClickListener(this);
        txtPasswordSetting.setOnClickListener(this);
        txtEmail.setText(emaiUser);
        mData = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == txtLogout.getId()) {
            signout();
        } else {
            resetPassword ();
        }
    }

    private void resetPassword() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.custom);
        dialog.setTitle("Title...");
        final EditText edtNewPassword = dialog.findViewById(R.id.edtNewPassword);
        final EditText edtConfirmNewPassword = dialog.findViewById(R.id.edtConfirmNewPassword);
        TextView txtCancle = dialog.findViewById(R.id.txtCancleCP);
        TextView txtSave = dialog.findViewById(R.id.txtSaveCP);
        dialog.setCancelable(false);
        txtCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        txtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress();
                final String password = edtNewPassword.getText().toString();
                String confrimPassword = edtConfirmNewPassword.getText().toString();
                boolean flag = true;
                if (TextUtils.isEmpty(password)){
                    edtNewPassword.setError("Bắt buộc!");
                    flag = false;
                } else edtNewPassword.setError(null);

                if (TextUtils.isEmpty(confrimPassword)){
                    edtConfirmNewPassword.setError("Bắt buộc!");
                    flag = false;
                } else edtConfirmNewPassword.setError(null);

                if (!password.equals(confrimPassword)){
                    edtConfirmNewPassword.setError("Password not match!");
                    edtConfirmNewPassword.setText("");
                    flag = false;
                } else edtConfirmNewPassword.setError(null);


                if (flag){
                    dialog.dismiss();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            changePassword (password);
                        }
                    }, 2000);
                }else hideProgress();
            }
        });
        dialog.show();

    }

    private void changePassword(final String newPass) {
        AuthCredential credential = EmailAuthProvider
                .getCredential(emaiUser, password);
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        hideProgress();
                        if (task.isSuccessful()) {
                            user.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getContext(), "Password updated", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getContext(), "Error password not updated", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(getContext(), "Error auth failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error auth failed", Toast.LENGTH_SHORT).show();
                hideProgress();
            }
        });
    }

    private void signout() {
        AlertDialog.Builder mAlert = new AlertDialog.Builder(getContext());
        mAlert.setCancelable(false);
        mAlert.setTitle("Do you want to sign out?");
        mAlert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                showProgress();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mAuth.signOut();
                        getContext().startActivity(new Intent(getContext(), SignInActivity.class));
                        getActivity().finish();
                        hideProgress();
                    }
                }, 3000);
            }
        });
        mAlert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        mAlert.show();
    }
}
