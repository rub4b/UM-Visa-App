package com.example.umvisaapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText editTextUsername, editTextPassword;
    private TextView textViewSignup;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mAuth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences("userPrefs", MODE_PRIVATE);

        editTextUsername = findViewById(R.id.editTextText);
        editTextPassword = findViewById(R.id.editTextTextPassword);

        Button loginButton = findViewById(R.id.Login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();

                loginUser(username, password);
            }
        });

        Button signupButton = findViewById(R.id.register);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToSignup();
            }
        });

        textViewSignup = findViewById(R.id.textView3);
        textViewSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToSignup();
            }
        });

        // Check if the user was previously signed in
        if (mAuth.getCurrentUser() != null) {
            // Check if the user is an admin
            checkAdminStatus();
        }
    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Save the "keep signed in" preference
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("keepSignedIn", true);
                            editor.apply();

                            // Login success, check if the user is an admin
                            checkAdminStatus();
                        } else {
                            // If login fails, display a message to the user.
                            Toast.makeText(login.this, "Authentication failed: " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void checkAdminStatus() {
        // Check if the logged-in user is an admin
        // You need to implement your logic here to determine if the user is an admin
        // For simplicity, I'm assuming here that all users with "admin@example.com" are admins.
        if (mAuth.getCurrentUser() != null && mAuth.getCurrentUser().getEmail().equals("admin@example.com")) {
            navigateToAdminPanel();
        } else {
            navigateToMain();
        }
    }

    private void navigateToSignup() {
        Intent intent = new Intent(login.this, Signup.class);
        startActivity(intent);
    }

    private void navigateToMain() {
        Intent intent = new Intent(login.this, MainActivity.class);
        startActivity(intent);
        finish(); // Optional: Close the current activity
    }

    private void navigateToAdminPanel() {
        Intent intent = new Intent(login.this, AdminPanel.class);
        startActivity(intent);
        finish(); // Optional: Close the current activity
    }
}
