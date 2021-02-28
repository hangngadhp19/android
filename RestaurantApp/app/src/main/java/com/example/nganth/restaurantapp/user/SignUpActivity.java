package com.example.nganth.restaurantapp.user;

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

import com.example.nganth.restaurantapp.BaseActivity;
import com.example.nganth.restaurantapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
public class SignUpActivity extends BaseActivity implements
        View.OnClickListener {

        private static final String TAG = "EmailPassword";

        private EditText mEmailField;
        private EditText mPasswordField;
        private EditText mConfirmPasswordField;

        // [START declare_auth]
        private FirebaseAuth mAuth;
        // [END declare_auth]

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.sign_up);

            // Views

            mEmailField = findViewById(R.id.email_field);
            mPasswordField = findViewById(R.id.password_field);
            mConfirmPasswordField = findViewById(R.id.confirm_password_field);

            // Buttons
            findViewById(R.id.btnSignUp).setOnClickListener(this);
            findViewById(R.id.signin_link).setOnClickListener(this);

            // [START initialize_auth]
            mAuth = FirebaseAuth.getInstance();
            // [END initialize_auth]
        }

        // [START on_start_check_user]
        @Override
        public void onStart() {
            super.onStart();
            // Check if user is signed in (non-null) and update UI accordingly.
            FirebaseUser currentUser = mAuth.getCurrentUser();
            //updateUI(currentUser);
        }
        // [END on_start_check_user]

        private void createAccount(String email, String password) {
            Log.d(TAG, "createAccount:" + email);
            if (!validateForm()) {
                return;
            }

            showProgressDialog();

            // [START create_user_with_email]
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                //Send email verification
                                sendEmailVerification();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }

                            // [START_EXCLUDE]
                            hideProgressDialog();
                            // [END_EXCLUDE]
                        }
                    });
            // [END create_user_with_email]
        }

        private void sendEmailVerification() {
            // Disable button
            findViewById(R.id.verify_email_button).setEnabled(false);

            // Send verification email
            // [START send_email_verification]
            final FirebaseUser user = mAuth.getCurrentUser();
            user.sendEmailVerification()
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            // [START_EXCLUDE]
                            if (task.isSuccessful()) {
                                Toast.makeText(SignUpActivity.this,
                                        "Verification email sent to " + user.getEmail(),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Log.e(TAG, "sendEmailVerification", task.getException());
                                Toast.makeText(SignUpActivity.this,
                                        "Failed to send verification email.",
                                        Toast.LENGTH_SHORT).show();
                            }
                            // [END_EXCLUDE]
                        }
                    });
            // [END send_email_verification]
        }

        private boolean validateForm() {
            boolean valid = true;

            String email = mEmailField.getText().toString();
            if (TextUtils.isEmpty(email)) {
                mEmailField.setError("Required.");
                valid = false;
            } else {
                mEmailField.setError(null);
            }

            String password = mPasswordField.getText().toString();
            String confirm_password = mConfirmPasswordField.getText().toString();
            if (TextUtils.isEmpty(password)) {
                mPasswordField.setError("Required.");
                valid = false;
            }else if( ! password.equals(confirm_password)){
                mConfirmPasswordField.setError("Confirm password not match.");
                valid = false;
            }else {
                mPasswordField.setError(null);
            }

            return valid;
        }

       /* private void updateUI(FirebaseUser user) {
            hideProgressDialog();
            if (user != null) {
                mStatusTextView.setText(getString(R.string.emailpassword_status_fmt,
                        user.getEmail(), user.isEmailVerified()));
                mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));
            } else {
                mStatusTextView.setText(R.string.signed_out);
                mDetailTextView.setText(null);
            }
        }*/

        @Override
        public void onClick(View v) {
            int i = v.getId();
            if (i == R.id.btnSignUp) {
                createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());
            }
            else if (i == R.id.signin_link) {
                android.content.Intent intent = new android.content.Intent(getApplicationContext(), SignInActivity.class);
                startActivity(intent);
            }
        }
    }