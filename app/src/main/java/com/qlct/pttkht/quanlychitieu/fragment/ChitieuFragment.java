package com.qlct.pttkht.quanlychitieu.fragment;


import android.content.Intent;
import android.os.Bundle;
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
import com.qlct.pttkht.quanlychitieu.activity.CreateNoteActivity;
import com.qlct.pttkht.quanlychitieu.adapter.ChitieuAdapter;
import com.qlct.pttkht.quanlychitieu.model.Chitieu;
import com.qlct.pttkht.quanlychitieu.utils.Constan;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChitieuFragment extends BaseFragment {


    private ImageView imgAdd;
    private TextView txtNoneNotes;
    private RecyclerView recyclerChitieu;
    private DatabaseReference mData;
    private ChitieuAdapter mAdapter;
    private ArrayList<Chitieu> arrChitieu;
    public ChitieuFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chitieu, container, false);
        initView (view);
        getData ();
        addEvents ();
        return view;
    }

    private void addEvents() {
        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addGhiChu ();
            }
        });
    }

    private void addGhiChu() {
        startActivity(new Intent(getContext(), CreateNoteActivity.class));
        getActivity().finish();
    }

    private void getData() {
        Calendar now = Calendar.getInstance();
        final int mounth = now.get(Calendar.MONTH) + 1;
        mData.child(Constan.CHITIEU).child(idUser).child(String.valueOf(mounth)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrChitieu.clear();
                if (dataSnapshot.getValue() != null){
                    for (DataSnapshot data : dataSnapshot.getChildren()){
                        Chitieu chitieu = data.getValue(Chitieu.class);
                        arrChitieu.add(chitieu);
                    }
                    if (arrChitieu.size() > 0) {
                        txtNoneNotes.setVisibility(View.GONE);
                        recyclerChitieu.setVisibility(View.VISIBLE);
                    }else {
                        txtNoneNotes.setVisibility(View.VISIBLE);
                        recyclerChitieu.setVisibility(View.GONE);
                    }
                    mAdapter.updateList(arrChitieu);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
               // if (databaseError != null && databaseError.getMessage() != null)
                //Toast.makeText(getContext(), databaseError.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView(View view) {
        imgAdd = view.findViewById(R.id.imgAddChitieu);
        txtNoneNotes = view.findViewById(R.id.txtNoneNotes);
        recyclerChitieu = view.findViewById(R.id.recyclerChitieu);
        
        arrChitieu = new ArrayList<>();
        mData = FirebaseDatabase.getInstance().getReference();
        
        mAdapter = new ChitieuAdapter(arrChitieu, getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerChitieu.setLayoutManager(layoutManager);
        recyclerChitieu.setAdapter(mAdapter);
    }

}
