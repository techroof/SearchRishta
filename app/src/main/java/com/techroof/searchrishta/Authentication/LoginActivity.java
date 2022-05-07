package com.techroof.searchrishta.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.techroof.searchrishta.HomeActivity;
import com.techroof.searchrishta.R;

public class LoginActivity extends AppCompatActivity {

    private TextView toolbarTitle;
    private TextInputLayout emailEt, passEt;
    private Button createAccBtn, loginBtn;
    private FirebaseAuth mAuth;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        toolbarTitle = findViewById(R.id.create_acc_text);

        emailEt = findViewById(R.id.login_email_et);
        passEt = findViewById(R.id.login_password_et);
        createAccBtn = findViewById(R.id.reg_btn);
        loginBtn = findViewById(R.id.login_btn);

        toolbarTitle.setText("Login Into Your Account");

        mAuth = FirebaseAuth.getInstance();

        pd = new ProgressDialog(this);
        pd.setCanceledOnTouchOutside(false);
        pd.setMessage("Logging in...");

        createAccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent reg = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(reg);
                finish();


            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email, password;

                email = emailEt.getEditText().getText().toString();
                password = passEt.getEditText().getText().toString();

                if (TextUtils.isEmpty(email)) {
                    emailEt.setError("Enter email");
                }

                if (TextUtils.isEmpty(password)) {
                    passEt.setError("Enter password");

                }

                if (!TextUtils.isEmpty(email)
                        && !TextUtils.isEmpty(password)) {

                    login(email, password);

                }

            }
        });

    }

    private void login(String email, String password) {

        pd.show();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            if (mAuth.getCurrentUser().isEmailVerified()) {

                                pd.dismiss();

                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                Toast.makeText(getApplicationContext(), "Logged in", Toast.LENGTH_LONG).show();

                                intent.putExtra("email", mAuth.getCurrentUser().getEmail());
                                intent.putExtra("uid", mAuth.getCurrentUser().getUid());
                                startActivity(intent);

                            } else {

                                verificationEmail();
                                pd.dismiss();
                                mAuth.signOut();
                                Toast.makeText(getApplicationContext(), "Your Email is not Verified", Toast.LENGTH_SHORT).show();
                            }


                           /* Intent home = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(home);
                            finish();

                            pd.dismiss();
                            Toast.makeText(LoginActivity.this, "logged in", Toast.LENGTH_SHORT).show();
    */                    }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void verificationEmail() {

        final FirebaseUser user = mAuth.getCurrentUser();
        final String mail=emailEt.getEditText().toString();

        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        // Re-enable button
                        //findViewById(R.id.verify_email_button).setEnabled(true);

                        if (task.isSuccessful()) {

                            Toast.makeText(getApplicationContext(),
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();

                            /*Name = edtName.getText().toString();
                            Email = edtEmail.getText().toString();
                            PhoneNumber = etPhoneNumber.getText().toString();
                            addData(Name, Email, PhoneNumber);*/
                            Intent home = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(home);
                            finish();

                        } else {
                            // Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(getApplicationContext(),
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

}