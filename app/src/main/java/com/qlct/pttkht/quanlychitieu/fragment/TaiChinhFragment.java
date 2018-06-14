package com.qlct.pttkht.quanlychitieu.fragment;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.qlct.pttkht.quanlychitieu.R;
import com.qlct.pttkht.quanlychitieu.activity.TaiChinhActivity;
import com.qlct.pttkht.quanlychitieu.adapter.ThuNhapAdapter;
import com.qlct.pttkht.quanlychitieu.model.ThuNhap;
import com.qlct.pttkht.quanlychitieu.utils.Constan;
import com.qlct.pttkht.quanlychitieu.utils.DialogThuNhap;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaiChinhFragment extends BaseFragment {


    private RecyclerView recyclerTaiChinh;
    private ImageView imgAdd, imgEditAmount;
    private ThuNhapAdapter mAdapter;
    private ArrayList<ThuNhap> arrThuNhap;
    private DatabaseReference mData;
    private TextView edtThuNhapCoDinh;
    private TextView txtnoneThuNhap;
    private boolean isEdit = false;
    private String money;

    public TaiChinhFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tai_chinh, container, false);
        initView(view);
        getData();
        return view;
    }


    private void getData() {
        Calendar now = Calendar.getInstance();
        final int mounth = now.get(Calendar.MONTH) + 1;
        //Get thu nhập cố định
        mData.child(Constan.THUNHAP).child(idUser).child(String.valueOf(mounth)).child(Constan.THUNHAP_CODINH).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot data : dataSnapshot.getChildren()){
                        ThuNhap thuNhap = data.getValue(ThuNhap.class);
                        money = thuNhap.getAmount() + "";
                        money = money.replace(".", ",");
                        if (money.endsWith("0")) {
                            money = money.substring(0, money.length() - 2);
                        }
                        edtThuNhapCoDinh.setText(money + ".000 vnd");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (databaseError != null && databaseError.getMessage() != null)
                    Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //Get thu nhập đột xuất
        mData.child(Constan.THUNHAP).child(idUser).child(String.valueOf(mounth)).child(Constan.THUNHAP_DOTXUAT).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrThuNhap.clear();
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        ThuNhap thuNhap = data.getValue(ThuNhap.class);
                        arrThuNhap.add(thuNhap);
                    }
                    mAdapter.updateList(arrThuNhap);
                    if (arrThuNhap.size() > 0) {
                        txtnoneThuNhap.setVisibility(View.GONE);
                        recyclerTaiChinh.setVisibility(View.VISIBLE);
                    } else {
                        txtnoneThuNhap.setVisibility(View.VISIBLE);
                        recyclerTaiChinh.setVisibility(View.GONE);
                    }
                } else {
                    txtnoneThuNhap.setVisibility(View.VISIBLE);
                    recyclerTaiChinh.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (databaseError != null && databaseError.getMessage() != null)
                    Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView(View view) {
        hideKeyboard(getActivity());
        txtnoneThuNhap = view.findViewById(R.id.txtnoneThuNhap);
        imgEditAmount = view.findViewById(R.id.imgEditAmount);
        edtThuNhapCoDinh = view.findViewById(R.id.edtThuNhapCoDinh);
        recyclerTaiChinh = view.findViewById(R.id.recyclerTaiChinh);
        imgAdd = view.findViewById(R.id.imgAddThuNhap);
        arrThuNhap = new ArrayList<>();
        mAdapter = new ThuNhapAdapter(arrThuNhap, getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerTaiChinh.setLayoutManager(layoutManager);
        recyclerTaiChinh.setAdapter(mAdapter);
        mData = FirebaseDatabase.getInstance().getReference();


        imgEditAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogEdit ();
            }
        });

        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), TaiChinhActivity.class));
            }
        });
    }

    private void showDialogEdit() {
        final DialogThuNhap dialogThuNhap = new DialogThuNhap(getContext());
        dialogThuNhap.setCancelable(false);
        dialogThuNhap.setmListener(new DialogThuNhap.IClick() {
            @Override
            public void editAmount(final double amount) {
                dialogThuNhap.dismiss();
                showProgress();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        editThuNhapCoDinh(amount);
                    }
                }, 3000);
            }

            @Override
            public void onCancle() {
                dialogThuNhap.dismiss();
            }
        });
        dialogThuNhap.show();
    }

    private void editThuNhapCoDinh(double amount) {
        final double thunhap = amount;
        Calendar now = Calendar.getInstance();
        final int mounth = now.get(Calendar.MONTH) + 1;
        String date = now.get(Calendar.YEAR) + "/" + now.get(Calendar.MONTH) + 1 + "/" +  now.get(Calendar.DAY_OF_MONTH) ;
        final DatabaseReference mRef = mData.child(Constan.THUNHAP).child(idUser).child(String.valueOf(mounth)).child(Constan.THUNHAP_CODINH).child(Constan.ID_THUNHAPCODINH);
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                hideProgress();
                String id = mData.push().getKey();
                long timeStamp = System.currentTimeMillis();
                ThuNhap thuNhap = new ThuNhap(mounth, "Thu Nhap Co Dinh", id, thunhap, 0, timeStamp, date);
                mRef.setValue(thuNhap);
                String amountHienThi = String.valueOf(thunhap).replace(".", ",");
                if (amountHienThi.endsWith("0")) {
                    amountHienThi = amountHienThi.substring(0, amountHienThi.length() - 2);
                }
                edtThuNhapCoDinh.setText(amountHienThi + ".000 vnd");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                hideProgress();
                if (databaseError != null && databaseError.getMessage() != null)
                    Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
