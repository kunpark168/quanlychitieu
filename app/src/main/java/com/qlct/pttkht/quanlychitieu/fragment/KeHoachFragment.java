package com.qlct.pttkht.quanlychitieu.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.qlct.pttkht.quanlychitieu.R;
import com.qlct.pttkht.quanlychitieu.activity.CreatePlanActivity;
import com.qlct.pttkht.quanlychitieu.adapter.KehoachAdapter;
import com.qlct.pttkht.quanlychitieu.model.Chitieu;
import com.qlct.pttkht.quanlychitieu.model.Kehoach;
import com.qlct.pttkht.quanlychitieu.utils.Constan;
import com.qlct.pttkht.quanlychitieu.utils.DialogViewMoreUtil;

import java.util.ArrayList;
import java.util.Calendar;

public class KeHoachFragment extends BaseFragment {

    private ImageView imgAdd;
    private RecyclerView recyclerKeHoach;
    private DatabaseReference mData;
    private ArrayList<Kehoach> arrKeHoach;
    private KehoachAdapter mAdapter;
    private TextView txtNonePlan;
    public KeHoachFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ke_hoach, container, false);
        initView (view);
        getData();
        return view;
    }

    @Override
    public void onResume() {
        getData();
        super.onResume();
    }

    private void initView(View view) {
        txtNonePlan = view.findViewById(R.id.txtNonePlan);
        imgAdd = view.findViewById(R.id.imgAddPlan);
        recyclerKeHoach = view.findViewById(R.id.recyclerKeHoach);
        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), CreatePlanActivity.class));
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mData = FirebaseDatabase.getInstance().getReference();
        arrKeHoach = new ArrayList<>();
        mAdapter = new KehoachAdapter(arrKeHoach, getContext());
        recyclerKeHoach.setLayoutManager(layoutManager);
        recyclerKeHoach.setAdapter(mAdapter);

        mAdapter.setmListener(new KehoachAdapter.IClick() {
            @Override
            public void onClick(final Kehoach kehoach) {
                final DialogViewMoreUtil dialogViewMoreUtil = new DialogViewMoreUtil(getActivity());
                dialogViewMoreUtil.setText("Đã hoàn thành", "Đã bị huỷ");
                dialogViewMoreUtil.setDialogViewMoreOnClick(new DialogViewMoreUtil.DialogViewMoreOnClick() {
                    @Override
                    public void firstViewClick() {
                        dialogViewMoreUtil.dismiss();
                        showProgress();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                updateKeHoach (kehoach);
                            }
                        }, 3000);
                    }

                    @Override
                    public void secondViewClick() {
                        dialogViewMoreUtil.dismiss();
                    }
                });
                dialogViewMoreUtil.show();
            }
        });
    }

    private void updateKeHoach(final Kehoach kehoach) {
        Calendar now = Calendar.getInstance();
        final int mounth = now.get(Calendar.MONTH) + 1;
        final DatabaseReference mRef = mData.child(Constan.KEHOACH).child(idUser).child(String.valueOf(mounth)).child(kehoach.getId());
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    mRef.removeValue();
                    if (arrKeHoach.contains(kehoach)){
                    arrKeHoach.remove(kehoach);
                    mAdapter.updateList(arrKeHoach);
                    }

                    if (arrKeHoach.size() > 0) {
                        txtNonePlan.setVisibility(View.GONE);
                        recyclerKeHoach.setVisibility(View.VISIBLE);
                    }else {
                        txtNonePlan.setVisibility(View.VISIBLE);
                        recyclerKeHoach.setVisibility(View.GONE);
                    }
                    final String idChitieu = mData.push().getKey();
                    Calendar now = Calendar.getInstance();
                    final int mounth = now.get(Calendar.MONTH) + 1;
                    final DatabaseReference mChitieu = mData.child(Constan.CHITIEU).child(idUser).child(String.valueOf(mounth)).child(idChitieu);
                    mChitieu.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() == null){
                                hideProgress();
                                long timeStamp = System.currentTimeMillis();
                                Chitieu chitieu = new Chitieu(mounth, idChitieu, kehoach.getNote(), timeStamp, 1, kehoach.getAmount(), kehoach.getDate());
                                mChitieu.setValue(chitieu);
                                Toast.makeText(getContext(), "Update thành công!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            if (databaseError != null && databaseError.getMessage() != null)
                            Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (databaseError != null && databaseError.getMessage() != null)
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getData() {
        Calendar now = Calendar.getInstance();
        final int mounth = now.get(Calendar.MONTH) + 1;
        mData.child(Constan.KEHOACH).child(idUser).child(String.valueOf(mounth)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrKeHoach.clear();
                if (dataSnapshot.getValue() != null){
                    for (DataSnapshot data : dataSnapshot.getChildren()){
                        Kehoach kehoach = data.getValue(Kehoach.class);
                        arrKeHoach.add(kehoach);
                    }
                    if (arrKeHoach.size() > 0) {
                        txtNonePlan.setVisibility(View.GONE);
                        recyclerKeHoach.setVisibility(View.VISIBLE);
                    }else {
                        txtNonePlan.setVisibility(View.VISIBLE);
                        recyclerKeHoach.setVisibility(View.GONE);
                    }
                    mAdapter.updateList(arrKeHoach);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (databaseError != null && databaseError.getMessage() != null)
                Toast.makeText(getContext(), databaseError.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
