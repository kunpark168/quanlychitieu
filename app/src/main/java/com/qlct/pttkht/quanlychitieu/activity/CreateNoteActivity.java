package com.qlct.pttkht.quanlychitieu.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.qlct.pttkht.quanlychitieu.R;
import com.qlct.pttkht.quanlychitieu.model.Chitieu;
import com.qlct.pttkht.quanlychitieu.utils.Constan;
import com.syd.oden.circleprogressdialog.core.CircleProgressDialog;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

public class CreateNoteActivity extends BaseActivity implements View.OnClickListener {

    private Spinner spinnerDanhMuc;
    private EditText edtAmountCN, edtNotes;
    private TextView txtSave;
    private RadioGroup rdgCN;
    private String date = "", time = "", category = "", note= "", amount = "";
    private int type = 0;
    private DatabaseReference mData;
    private TextView txtDate;
    private Button btnChooseDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        initView();
        addEvent ();
    }

    private void addEvent() {
        txtSave.setOnClickListener(this);
        findViewById(R.id.layoutGhichu).setOnClickListener(this);

        btnChooseDate.setOnClickListener(this);
    }

    private void initView() {

        findViewById(R.id.imgBackAN).setOnClickListener(this);
        rdgCN = findViewById(R.id.rdgCN);
        spinnerDanhMuc = findViewById(R.id.spinnerDanhMuc);
        edtAmountCN = findViewById(R.id.edtAmountCN);
        edtNotes = findViewById(R.id.edtNote);
        txtSave = findViewById(R.id.txtAddNote);
        txtDate = findViewById(R.id.txtDateCN);
        btnChooseDate = findViewById(R.id.btnChooseDateCN);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.arrCategory, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerDanhMuc.setAdapter(adapter);
        rdgCN.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rdCodinh ) {
                    spinnerDanhMuc.setEnabled(true);
                    type = 0;
                }
                else if (checkedId == R.id.rbDoXuat) {
                    spinnerDanhMuc.setEnabled(false);
                    type = 1;
                }
            }
        });
        spinnerDanhMuc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = spinnerDanhMuc.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mData = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.imgBackAN) {
            startActivity(new Intent(CreateNoteActivity.this, MainActivity.class));
            finish();
        }
        else if (id == R.id.txtAddNote) {
            taoghichu();
        } else if (id == R.id.layoutGhichu){
            edtNotes.requestFocus();
        } else if (id == R.id.btnChooseDateCN){
            showDatePicker ();
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

    private void taoghichu() {
        note = edtNotes.getText().toString();
        amount = edtAmountCN.getText().toString();
        boolean isValid = true;
        if (TextUtils.isEmpty(note)){
            edtNotes.setError("Bắt buộc");
            isValid = false;
        }

        if (TextUtils.isEmpty(amount)){
            edtAmountCN.setError("Bắt buộc");
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
                    Calendar now = Calendar.getInstance();
                    long timeStamp = System.currentTimeMillis();
                    String idNote = mData.push().getKey();
                    int mounth = now.get(Calendar.MONTH) + 1;
                    final DatabaseReference mRef = mData.child(Constan.CHITIEU).child(idUser).child(String.valueOf(mounth)).child(idNote);
                    final Chitieu chitieu = new Chitieu(mounth, idNote, note, timeStamp, type, Double.parseDouble(amount), date);
                    if (type == 0)
                        chitieu.setCategory(category);
                    mRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            hideProgress();
                            if (dataSnapshot.getValue() == null){
                                mRef.setValue(chitieu);
                                Toast.makeText(CreateNoteActivity.this, "Tạo ghi chú thành công!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(CreateNoteActivity.this, MainActivity.class);
                                intent.putExtra(Constan.TYPEMAIN, 1);
                                startActivity(intent);
                                finish();
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            hideProgress();
                            Toast.makeText(CreateNoteActivity.this, databaseError.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }, 1000);
        }

    }
}
