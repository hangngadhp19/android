package com.example.nganth.restaurantapp.user;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nganth.restaurantapp.BaseActivity;
import com.example.nganth.restaurantapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class ProfileActivity extends BaseActivity {

    public  String TAG = "ProfileActivity";

    private TextView mChangePasswordLink;
    private TextView mNameField;
    private TextView mEmailField;
    private ImageView imgEditProfile;
    private Button btnSave;

    private EditText mEditNameField;
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        mNameField = findViewById(R.id.name_field);
        mEmailField = findViewById(R.id.email_field);
        mEditNameField = findViewById(R.id.edit_name_field);
        btnSave = findViewById(R.id.btnSave);

        mChangePasswordLink = findViewById(R.id.password_change_link);

        mChangePasswordLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.content.Intent intent = new android.content.Intent(getApplicationContext(), ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        imgEditProfile = findViewById(R.id.edit_profile_icon);

        imgEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNameField.setVisibility(View.INVISIBLE);
                mEditNameField.setVisibility(View.VISIBLE);
                btnSave.setVisibility(View.VISIBLE);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Update profile
                showProgressDialog();
                updateProfile();
            }
        });
    }
    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    public void openFavoriteActivity(android.view.View view) {
        android.content.Intent intent = new android.content.Intent(getApplicationContext(), FavoriteActivity.class);
        startActivity(intent);
    }

    public void updateUI(FirebaseUser user){
        mNameField.setText(user.getDisplayName());
        mEmailField.setText(user.getEmail());
        mEditNameField.setText(user.getDisplayName());
    }

    public void updateProfile(){
        final String userName = mEditNameField.getText().toString();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(userName)
                //.setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                            mNameField.setVisibility(View.VISIBLE);

                            mNameField.setText(userName);
                            mEditNameField.setVisibility(View.INVISIBLE);
                            btnSave.setVisibility(View.INVISIBLE);
                        }

                        hideProgressDialog();
                    }
                });
    }
}
