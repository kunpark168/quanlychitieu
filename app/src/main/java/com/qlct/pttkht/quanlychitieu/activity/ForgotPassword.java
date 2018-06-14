package com.qlct.pttkht.quanlychitieu.activity;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.qlct.pttkht.quanlychitieu.R;

public class ForgotPassword extends BaseActivity {

    private EditText edtEmail;
    private TextView txtReset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        findViewById(R.id.imgBackFP).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        edtEmail = findViewById(R.id.edtEmailFP);
        txtReset = findViewById(R.id.txtResetFP);
        txtReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress();
                boolean flag = true;
                final String email = edtEmail.getText().toString();
                if (TextUtils.isEmpty(email)){
                    edtEmail.setError("Bắt buộc!");
                    flag = false;
                } else edtEmail.setError(null);

                if (!isEmailValid(email)){
                    edtEmail.setError("Email invalid!");
                    flag = false;
                } else edtEmail.setError(null);

                if (flag){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            hideProgress();
                                            if (task.isSuccessful()) {
                                                edtEmail.setText("");
                                                Toast.makeText(ForgotPassword.this, "We sent a message to your email,Please check again!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    hideProgress();
                                    if (e!= null && e.getMessage() != null)
                                        Toast.makeText(ForgotPassword.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }, 3000);
                }else hideProgress();
            }
        });
    }
}
