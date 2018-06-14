package com.qlct.pttkht.quanlychitieu.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.qlct.pttkht.quanlychitieu.R;
import com.qlct.pttkht.quanlychitieu.utils.Constan;

public class SignInActivity extends BaseActivity implements View.OnClickListener {

    private EditText edtEmail, edtPassword;
    private TextView txtForgotPassword, txtSignIn, txtSignUp;
    private FirebaseAuth mAuth;
    private int isExist = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        initView();
    }

    private void initView() {
        edtEmail = findViewById(R.id.edtEmailSI);
        edtPassword = findViewById(R.id.edtPasswordSI);
        txtForgotPassword = findViewById(R.id.txtForgotPassword);
        txtSignIn = findViewById(R.id.txtSignIn);
        txtSignUp = findViewById(R.id.txtSignUpSI);

        txtSignIn.setOnClickListener(this);
        txtForgotPassword.setOnClickListener(this);
        txtSignUp.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.txtSignIn:
                signIn();
                break;
            case R.id.txtForgotPassword:
                startActivity(new Intent(SignInActivity.this, ForgotPassword.class));
                break;
            case R.id.txtSignUpSI:
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
                break;
            default:
                break;
        }
    }

    private void signIn() {
        String email = edtEmail.getText().toString().trim();
        final String password = edtPassword.getText().toString().trim();
        boolean isValid = true;
        //Check is email empty
        if (TextUtils.isEmpty(email)) {
            edtEmail.setError("Bắt buộc");
            edtEmail.requestFocus();
            isValid = false;
        } else {
            edtEmail.setError(null);
        }

        //Check password empty

        if (TextUtils.isEmpty(password)) {
            edtPassword.setError("Bắt buộc");
            edtPassword.requestFocus();
            isValid = false;
        } else {
            edtPassword.setError(null);
        }

        //Check email invalid

        if (!isEmailValid(email)) {
            edtEmail.setError("Email invalid!");
            edtEmail.requestFocus();
            isValid = false;
        } else {
            edtEmail.setError(null);
        }

        if (isValid) {
            showProgress();
            //SignIn with email and password
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    hideProgress();
                    if (task.isSuccessful()) {
                        Toast.makeText(SignInActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                        intent.putExtra(Constan.PASSWORD, password);
                        startActivity(intent);
                        finish();
                    } else {
                        if (task != null && task.getException() != null && task.getException().getMessage() != null)
                            Toast.makeText(SignInActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    hideProgress();
                    if (e != null && e.getMessage() != null)
                        Toast.makeText(SignInActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        if (isExist == 0 || isExist == 1) {
            isExist += 1;
            Toast.makeText(this, "Please press back again to exist!", Toast.LENGTH_SHORT).show();
        } else if (isExist == 3) {
            System.exit(0);
        }
    }
}
