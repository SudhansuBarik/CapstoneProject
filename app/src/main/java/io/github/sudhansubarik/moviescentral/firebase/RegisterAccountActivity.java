package io.github.sudhansubarik.moviescentral.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import io.github.sudhansubarik.moviescentral.R;

public class RegisterAccountActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText, confirmPasswordEditText;
    private CheckBox declarationCheckBox;
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account);

        //To hide AppBar for fullscreen.
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.hide();

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.register_email_editText);
        passwordEditText = findViewById(R.id.register_password_editText);
        confirmPasswordEditText = findViewById(R.id.register_confirm_password_editText);
        declarationCheckBox = findViewById(R.id.register_declaration_checkBox);
        TextView loginLinkTextView = findViewById(R.id.register_login_link_textView);
        Button registerButton = findViewById(R.id.register_button);
        progressBar = findViewById(R.id.profile_progressBar);

        loginLinkTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String confirmPassword = confirmPasswordEditText.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), R.string.enter_email, Toast.LENGTH_SHORT).show();
                }
                // Check if password field is populated
                else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), R.string.enter_password, Toast.LENGTH_SHORT).show();
                }
                // Check if password entered is minimum 6 characters long
                else if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), getString(R.string.password_too_short), Toast.LENGTH_SHORT).show();
                }
                // Check if both passwords are equal
                else if (!password.equals(confirmPassword)) {
                    Toast.makeText(getApplicationContext(), getString(R.string.passwords_do_not_match), Toast.LENGTH_SHORT).show();
                }
                // Make sure user should check the declaration checkbox
                else if (!declarationCheckBox.isChecked()) {
                    Toast.makeText(getApplicationContext(), getString(R.string.accept_terms_and_policy), Toast.LENGTH_SHORT).show();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    //create user
                    auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(RegisterAccountActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
//                                    Toast.makeText(RegisterAccountActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(RegisterAccountActivity.this, getString(R.string.registration_failed) + task.getException(),
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        startActivity(new Intent(RegisterAccountActivity.this, CreateProfileActivity.class));
                                        finish();
                                    }
                                }
                            });
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }
}
