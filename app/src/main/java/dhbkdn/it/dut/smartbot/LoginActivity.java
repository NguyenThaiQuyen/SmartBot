package dhbkdn.it.dut.smartbot;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    protected FirebaseAuth mAuth;
    protected FirebaseUser mUser;

    EditText edtEmail, edtPass;
    Button btnRegister, btnForgot, btnLogin;
    String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inits();
        setWidgets();
        addListener();
    }

    private void inits() {
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    }

    private void setWidgets() {
        edtEmail = findViewById(R.id.edt_email);
        edtPass = findViewById(R.id.edt_pass);
        btnRegister = findViewById(R.id.btnRegister);
        btnForgot = findViewById(R.id.btnForgot);
        btnLogin = findViewById(R.id.btnLogin);
    }

    private String getEmail() {
        email = edtEmail.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            return null;
        }
        return email;
    }

    private void requireEnterEmail() {
        email = edtEmail.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.input_email), Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private void requireEnterPassword() {
        password = edtPass.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.input_password), Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 6) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.input_again_pass), Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private void getWidgets() {
        requireEnterEmail();
        requireEnterPassword();
    }

    private void addListener() {
        btnRegister.setOnClickListener(this);
        btnForgot.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin: {
                getWidgets();
                final ProgressDialog mProgressDialog = ProgressDialog.show(LoginActivity.this, getResources().getString(R.string.wait), getResources().getString(R.string.processing), true);
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                mProgressDialog.dismiss();
                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.login_successfully), Toast.LENGTH_SHORT).show();
                                    mUser = mAuth.getCurrentUser();
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                } else {
                                    Log.e("ERROR", task.getException().toString());
                                    Toast.makeText(LoginActivity.this, getString(R.string.login_fail), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                break;
            }
            case R.id.btnRegister: {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
            }
            case R.id.btnForgot: {
                String email = getEmail();
                if ( email == null ) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.input_email), Toast.LENGTH_SHORT).show();
                } else {
                    final ProgressDialog mProgressDialog = ProgressDialog.show(LoginActivity.this, getResources().getString(R.string.wait), getResources().getString(R.string.processing), true);
                    mAuth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.message), Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.message_faile), Toast.LENGTH_SHORT).show();
                                    }
                                    mProgressDialog.dismiss();
                                }
                            });
                }
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {

    }
}
