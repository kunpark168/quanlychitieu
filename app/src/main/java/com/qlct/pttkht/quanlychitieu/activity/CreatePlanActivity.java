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
import com.qlct.pttkht.quanlychitieu.model.Kehoach;
import com.qlct.pttkht.quanlychitieu.utils.Constan;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

public class CreatePlanActivity extends BaseActivity {

    private ImageView imgBack;
    private EditText edtNote, edtAmount;
    private String note = "", amount = "", date="";
    private TextView txtAddPlan;
    private DatabaseReference mData;
    private TextView txtDate;
    private Button btnChooseDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_plan);
        initView ();
    }

    private void initView() {
        edtNote = findViewById(R.id.edtNotePlan);
        edtAmount = findViewById(R.id.edtAmountCP);
        imgBack = findViewById(R.id.imgBackCP);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtAddPlan = findViewById(R.id.txtAddPlan);
        txtDate = findViewById(R.id.txtDateCP);
        btnChooseDate = findViewById(R.id.btnChooseDateCP);
        txtAddPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPlan ();
            }
        });
        mData = FirebaseDatabase.getInstance().getReference();
        btnChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             showDatePicker();
            }
        });

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

    private void addPlan() {
        boolean isValid = true;
        note = edtNote.getText().toString().trim();
        amount = edtAmount.getText().toString();
        if (TextUtils.isEmpty(note)){
            edtNote.setError("Bắt buộc");
            isValid = false;
        }

        if (TextUtils.isEmpty(amount)){
            edtAmount.setError("Bắt buộc");
            isValid = false;
        }

        if (date.equals("")){
            Toast.makeText(this, "Please choose date!", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        if (isValid){
            showProgress();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    long timeStamp = System.currentTimeMillis();
                    String idKeHoach = mData.push().getKey();
                    Calendar now = Calendar.getInstance();
                    int mounth = now.get(Calendar.MONTH) + 1;
                    final DatabaseReference mRef = mData.child(Constan.KEHOACH).child(idUser).child(String.valueOf(mounth)).child(idKeHoach);
                    final Kehoach kehoach = new Kehoach(mounth, idKeHoach, timeStamp, Double.parseDouble(amount), note, date);
                    mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                         if (dataSnapshot.getValue() == null){
                             hideProgress();
                             mRef.setValue(kehoach);
                             Toast.makeText(CreatePlanActivity.this, "Tạo kế hoạch thành công!", Toast.LENGTH_SHORT).show();
                             Intent intent = new Intent(CreatePlanActivity.this, MainActivity.class);
                             intent.putExtra(Constan.TYPEMAIN, 2);
                             startActivity(intent);
                             finish();
                         }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            hideProgress();
                            Toast.makeText(CreatePlanActivity.this, databaseError.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }, 3000);
        }
    }
}
