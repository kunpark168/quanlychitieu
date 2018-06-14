package com.qlct.pttkht.quanlychitieu.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.qlct.pttkht.quanlychitieu.R;
import com.qlct.pttkht.quanlychitieu.model.User;
import com.qlct.pttkht.quanlychitieu.utils.Constan;

public class SignUpActivity extends BaseActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mData;
    private String TAG = SignUpActivity.class.getName();
    private EditText edtEmail, edtPassword, edtConfirmPassword;
    private TextView txtSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initView ();
    }

    private void initView() {

        edtEmail = findViewById(R.id.edtEmailSU);
        edtPassword = findViewById(R.id.edtPasswordSU);
        edtConfirmPassword = findViewById(R.id.edtConfirmPasswordSU);
        txtSignUp = findViewById(R.id.txtSignUp);
        mAuth = FirebaseAuth.getInstance();
        mData = FirebaseDatabase.getInstance().getReference();

        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             signUp ();
            }
        });
    }

    private void signUp() {
        boolean isValid = true;
        final String email = edtEmail.getText().toString();
        final String password = edtPassword.getText().toString().trim();
        String confirmPassword = edtConfirmPassword.getText().toString().trim();
        if (TextUtils.isEmpty(email)){
            edtEmail.setError("Bắt buộc");
            edtEmail.requestFocus();
            isValid = false;
        } else {
            edtEmail.setError(null);
        }

        if (TextUtils.isEmpty(password)){
            edtPassword.setError("Bắt buộc");
            edtPassword.requestFocus();
            isValid = false;
        } else {
            edtPassword.setError(null);
        }

        if (TextUtils.isEmpty(confirmPassword)){
            edtConfirmPassword.setError("Bắt buộc");
            edtConfirmPassword.requestFocus();
            isValid = false;
        } else {
            edtEmail.setError(null);
        }

        if (!password.equals(confirmPassword)){
            isValid = false;
            edtPassword.requestFocus();
            edtConfirmPassword.setText("");
            Toast.makeText(this, "Password not match!", Toast.LENGTH_SHORT).show();
        }

        if (!isEmailValid(email)){
            edtEmail.setError("Email invalid!");
            edtEmail.requestFocus();
            isValid = false;
        }

        if (isValid){
            showProgress();
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    hideProgress();
                    if (task.isSuccessful()){
                        User user = new User(email, task.getResult().getUser().getUid().toString());
                        writeDataBase (user, password);
                    }else {
                        if (task != null && task.getException() != null &&task.getException().getMessage() != null)
                        Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    hideProgress();
                    if (e!= null && e.getMessage() != null)
                        Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void writeDataBase(final User user, final String password) {
        final DatabaseReference mRef = mData.child(Constan.USERS).child(user.getId());
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null){
                    mRef.setValue(user);
                    Toast.makeText(SignUpActivity.this, "Đăng kí thành công!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    intent.putExtra(Constan.PASSWORD, password);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(SignUpActivity.this, "Đăng kí thành công!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    intent.putExtra(Constan.PASSWORD, password);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, databaseError.getMessage().toString());
                Toast.makeText(SignUpActivity.this, databaseError.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
