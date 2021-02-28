package com.example.nganth.restaurantapp.user;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class ChangePasswordActivity extends BaseActivity {

    private static final String TAG = "ChangePassword";

    private Button btnChangePass;
    private EditText mNewPasswordField;
    private EditText mConfirmPasswordField;
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        mNewPasswordField = findViewById(R.id.new_password_field);
        mConfirmPasswordField = findViewById(R.id.confirm_password_field);

        btnChangePass = findViewById(R.id.btnChangePass);

        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = mNewPasswordField.getText().toString();

                changePassword(password);
            }
        });
    }
    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null)
        {
            //Open login screen
        }
    }

    private void changePassword(String newPassword) {

        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.updatePassword(newPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User password updated.");
                            mAuth.signOut();
                            android.content.Intent intent = new android.content.Intent(getApplicationContext(), SignInActivity.class);
                            startActivity(intent);
                        }else{
                            Log.d(TAG, task.getException().getMessage());
                        }

                        hideProgressDialog();
                    }
                });
    }
    private boolean validateForm() {
        boolean valid = true;

        String password = mNewPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mNewPasswordField.setError("Required.");
            valid = false;
        } else {
            mNewPasswordField.setError(null);
        }

        String confirm_password = mConfirmPasswordField.getText().toString();
        if (TextUtils.isEmpty(confirm_password)) {
            mConfirmPasswordField.setError("Required.");
            valid = false;
        }else if( ! password.equals(confirm_password)){
            mConfirmPasswordField.setError("Confirm password not match.");
            valid = false;
        }else {
            mConfirmPasswordField.setError(null);
        }

        return valid;
    }
}
