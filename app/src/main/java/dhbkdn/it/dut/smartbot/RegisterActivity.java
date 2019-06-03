package dhbkdn.it.dut.smartbot;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

import dhbkdn.it.dut.smartbot.models.Equipment;

import static android.text.TextUtils.isEmpty;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    protected FirebaseAuth auth;
    protected FirebaseUser user;

    EditText edtEmail, edtPass, edtConfirm;
    Button btnRegister, btnLogin;
    String email, password, confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inits();
        setWidgets();
        addListener();

    }

    private void inits() {
        auth = FirebaseAuth.getInstance();
    }

    private void setWidgets() {
        edtEmail = findViewById(R.id.edt_email);
        edtPass = findViewById(R.id.edt_pass);
        edtConfirm = findViewById(R.id.edtConfirm);
        btnRegister = findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnLogin);
    }

    private void addListener() {
        btnRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

    }

    private void getWidgets() {
        email = edtEmail.getText().toString().trim();
        password = edtPass.getText().toString().trim();
        confirmPassword = edtConfirm.getText().toString().trim();


        if (isEmpty(email)) {
            edtEmail.setError(getResources().getString(R.string.email_requried));
            edtEmail.requestFocus();
            return;
        }

        if (isEmpty(password)) {
            edtPass.setError(getResources().getString(R.string.password_requried));
            edtPass.requestFocus();
            return;
        }

        if (isEmpty(confirmPassword)) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.password_confirm_requried), Toast.LENGTH_SHORT).show();
            return;
        }

        if (!confirmPassword.equals(password)) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.password_same_requried), Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.input_again_pass), Toast.LENGTH_SHORT).show();
            return;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRegister: {
                getWidgets();
                final ProgressDialog mProgressDialog = ProgressDialog.show(RegisterActivity.this, getResources().getString(R.string.wait), getResources().getString(R.string.processing), true);
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                mProgressDialog.dismiss();
                                if (task.isSuccessful()) {
                                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.register), Toast.LENGTH_SHORT).show();
                                    user = auth.getCurrentUser();
                                    createStore();
                                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                } else {
                                    //Log.e("ERROR", task.getException().toString());
                                    FirebaseAuthException e = (FirebaseAuthException) task.getException();
                                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.register_faile) + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    //Toast.makeText(RegisterActivity.this, "Register failed!", Toast.LENGTH_SHORT).show();
                                }
                            }

                            private void createStore() {
                                Equipment equipment = new Equipment("Fan", "OFF", "OFF/23:00", "0", "OFF", "1");
                            }
                        });
                break;
            }
            case R.id.btnLogin: {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                break;
            }
        }
    }
}
