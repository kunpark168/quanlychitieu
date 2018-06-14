package com.qlct.pttkht.quanlychitieu.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
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
import com.qlct.pttkht.quanlychitieu.model.ThuNhap;
import com.qlct.pttkht.quanlychitieu.utils.Constan;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

public class TaiChinhActivity extends BaseActivity {

    private ImageView imgBack;
    private EditText edtAmount, edtNote;
    private TextView txtAdd;
    String amount, note;
    private DatabaseReference mData;
    private TextView txtDate;
    private Button btnChoose;
    private String date = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tai_chinh);
        txtDate = findViewById(R.id.txtDateTC);
        btnChoose = findViewById(R.id.btnChooseDateTC);
        imgBack = findViewById(R.id.imgBackTN);
        edtAmount = findViewById(R.id.edtAmountTN);
        edtNote = findViewById(R.id.edtNoteTN);
        txtAdd = findViewById(R.id.txtAddTN);
        mData = FirebaseDatabase.getInstance().getReference();

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TaiChinhActivity.this, MainActivity.class));
                finish();
            }
        });

        txtAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createThuNhap ();
            }
        });
        btnChoose.setOnClickListener((event) -> showDatePicker());
    }

    private void createThuNhap() {
        boolean isValid = true;
        note = edtNote.getText().toString().trim();
        amount = edtAmount.getText().toString().trim();
        if (TextUtils.isEmpty(note)){
            isValid = false;
            edtNote.setError("Bắt buộc");
        }

        if (TextUtils.isEmpty(amount)){
            isValid = false;
            edtAmount.setError("Bắt buộc");
        }
        if (date.equals("")){
            isValid = false;
            Toast.makeText(this, "Please choose date!", Toast.LENGTH_SHORT).show();
        }

        if (isValid){
            showProgress();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    final String id = mData.push().getKey();
                    Calendar now = Calendar.getInstance();
                    final int mounth = now.get(Calendar.MONTH) + 1;
                    final DatabaseReference mRef = mData.child(Constan.THUNHAP).child(idUser).child(String.valueOf(mounth) ).child(Constan.THUNHAP_DOTXUAT).child(id);
                    mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            hideProgress();
                            if (dataSnapshot.getValue() == null){
                                long timeStamp = System.currentTimeMillis();
                                ThuNhap thuNhap = new ThuNhap(mounth, edtNote.getText().toString(), id, Double.parseDouble(amount), 1, timeStamp, date);
                                mRef.setValue(thuNhap);
                                Toast.makeText(TaiChinhActivity.this, "Tạo thu nhập thành công!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(TaiChinhActivity.this, MainActivity.class);
                                intent.putExtra(Constan.TYPEMAIN, 0);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            hideProgress();
                            Toast.makeText(TaiChinhActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }, 3000);
        }
    }

    private void showDatePicker() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        int mouth = monthOfYear  + 1;
                        date = dayOfMonth + "/" + mouth + "/" + year;
                        txtDate.setText(date);
                    }
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }
}
