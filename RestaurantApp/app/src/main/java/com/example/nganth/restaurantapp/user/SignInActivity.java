package com.example.nganth.restaurantapp.user;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nganth.restaurantapp.BaseActivity;
import com.example.nganth.restaurantapp.R;
import com.example.nganth.restaurantapp.restaurant.RestaurantActivity;
import com.example.nganth.restaurantapp.restaurant.WalkthoughtFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends BaseActivity implements
        View.OnClickListener {

    private static final String TAG = "EmailPassword";

    private TextView mStatusTextView;
    private TextView mDetailTextView;
    private EditText mEmailField;
    private EditText mPasswordField;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        // Views
        mEmailField = findViewById(R.id.email_field);
        mPasswordField = findViewById(R.id.password_field);
        mDetailTextView = findViewById(R.id.detail);

        // Buttons
        findViewById(R.id.btnSignIn).setOnClickListener(this);
        findViewById(R.id.verify_email_button).setOnClickListener(this);
        findViewById(R.id.signup_link).setOnClickListener(this);
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
        updateUI(currentUser);
    }
    // [END on_start_check_user]

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //
                            if (user.isEmailVerified()) {
                                //Open profile user
//                                    android.content.Intent intent = new android.content.Intent(getApplicationContext(), ProfileActivity.class);
                                android.content.Intent intent = new android.content.Intent(getApplicationContext(), RestaurantActivity.class);
                                startActivity(intent);
                            } else {
                                updateUI(user);
                                Toast.makeText(com.example.nganth.restaurantapp.user.SignInActivity.this, "Please veriry your email.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(com.example.nganth.restaurantapp.user.SignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                            mDetailTextView.setText(R.string.auth_failed);

                            updateUI(null);
                        }

                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Error", e.getMessage());
            }
        });
        // [END sign_in_with_email]
    }

    private void signOut() {
        mAuth.signOut();
        updateUI(null);
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
                        // Re-enable button
                        findViewById(R.id.verify_email_button).setEnabled(true);

                        if (task.isSuccessful()) {
                            Toast.makeText(SignInActivity.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(SignInActivity.this,
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
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }

    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {
            String user_detail = getString(R.string.emailpassword_status_fmt,
                    user.getEmail(), user.isEmailVerified());
            mDetailTextView.setText(user_detail);

            findViewById(R.id.btnSignIn).setVisibility(View.INVISIBLE);
            if (user.isEmailVerified()) {
                findViewById(R.id.verify_email_button).setVisibility(View.INVISIBLE);
            } else {
                findViewById(R.id.verify_email_button).setVisibility(View.VISIBLE);
            }
        } else {
            mDetailTextView.setText(null);

            findViewById(R.id.btnSignIn).setVisibility(View.VISIBLE);
            findViewById(R.id.verify_email_button).setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btnSignIn) {
            signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
        } else if (i == R.id.signup_link) {
            //open activity signup
            android.content.Intent intent = new android.content.Intent(getApplicationContext(), SignUpActivity.class);
            startActivity(intent);
        } else if (i == R.id.verify_email_button) {
            sendEmailVerification();
        }
    }
}
