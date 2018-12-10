package io.github.sudhansubarik.moviescentral.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import io.github.sudhansubarik.moviescentral.R;
import io.github.sudhansubarik.moviescentral.activities.MainActivity;

public class CreateProfileActivity extends AppCompatActivity {

    private static final String TAG = CreateProfileActivity.class.getSimpleName();

    private EditText nameEditText, mobileEditText;
    private ProgressBar progressBar;

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        //To hide AppBar for fullscreen.
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.hide();

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        // If the user is not logged in then start LoginActivity
        if (auth.getCurrentUser() == null) {
            // close this activity
            finish();
            // start login activity
            startActivity(new Intent(this, LoginActivity.class));
        }

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        // get reference to 'users' node
        databaseReference = firebaseDatabase.getReference("users");

        // store app title to 'app_title' node
        firebaseDatabase.getReference("app_title").setValue("Movies Central");

        // app_title change listener
        firebaseDatabase.getReference("app_title").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e(TAG, "App title updated");

                String appTitle = dataSnapshot.getValue(String.class);

                // update toolbar title
                Objects.requireNonNull(getSupportActionBar()).setTitle(appTitle);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read app title value.", error.toException());
            }
        });

        nameEditText = findViewById(R.id.create_profile_name_editText);
        mobileEditText = findViewById(R.id.create_profile_mobile_editText);
        Button createProfileButton = findViewById(R.id.save_profile_button);
        progressBar = findViewById(R.id.create_profile_progressBar);

        createProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = nameEditText.getText().toString();
                String mobile = mobileEditText.getText().toString();

                // If any field doesn't have a response then create a toast to prompt the user to enter value
                if (name.equals("")) {
                    Toast.makeText(CreateProfileActivity.this, "Please enter your Name", Toast.LENGTH_SHORT).show();
                } else if (mobile.equals("")) {
                    Toast.makeText(CreateProfileActivity.this, "Please enter your Mobile Number", Toast.LENGTH_SHORT).show();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    saveUserInformation();
                    progressBar.setVisibility(View.GONE);
                    startActivity(new Intent(CreateProfileActivity.this, MainActivity.class));
                    finish();
                }
            }
        });
    }

    private void saveUserInformation() {
        String name = nameEditText.getText().toString().trim();
        String mobile = mobileEditText.getText().toString().trim();

        FirebaseUser user = auth.getCurrentUser();
        String email = null;
        if (user != null) {
            email = user.getEmail();
        }
        UserInformation userInformation = new UserInformation(name, email, mobile);
        assert user != null;
        databaseReference.child(user.getUid()).setValue(userInformation);
        Toast.makeText(this, "Profile created successfully", Toast.LENGTH_SHORT).show();
    }
}
