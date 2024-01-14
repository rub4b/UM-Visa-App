package com.example.umvisaapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Signup extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private EditText editTextName, editTextEmail, editTextPassword, editTextConfirmPassword, editTextPhone, editTextAddress, editTextNationality;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        editTextName = findViewById(R.id.InputName);
        editTextEmail = findViewById(R.id.InputEmail);
        editTextPassword = findViewById(R.id.InputPassword);
        editTextConfirmPassword = findViewById(R.id.ConfirmPassword);
        editTextPhone = findViewById(R.id.InputPhone);
        editTextAddress = findViewById(R.id.InputAddress);
        editTextNationality = findViewById(R.id.InputNationality);

        Button signupButton = findViewById(R.id.button);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString();
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                String confirmPassword = editTextConfirmPassword.getText().toString();
                String phone = editTextPhone.getText().toString();
                String address = editTextAddress.getText().toString();
                String nationality = editTextNationality.getText().toString();

                if (password.equals(confirmPassword)) {
                    signUpUser(name, email, password, phone, address, nationality);
                } else {
                    Toast.makeText(Signup.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void signUpUser(String name, String email, String password, String phone, String address, String nationality) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                            Map<String, Object> user = new HashMap<>();
                            user.put("name", name);
                            user.put("email", email);
                            user.put("phone", phone);
                            user.put("address", address);
                            user.put("nationality", nationality);

                            db.collection("users").document(userId)
                                    .set(user)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                checkAdminStatus(email);
                                            } else {
                                                Toast.makeText(Signup.this, "Data failed: " + task.getException().getMessage(),
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(Signup.this, "Authentication failed: " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // ...

    private void checkAdminStatus(String email) {
        // You need to implement your logic here to determine if the user is an admin
        // For simplicity, I'm assuming here that the admin email is "admin@example.com"
        if (email.equals("admin@example.com")) {
            navigateToAdminPanel();
        } else {
            navigateToMain();
        }
    }

    private void navigateToMain() {
        Intent intent = new Intent(Signup.this, MainActivity.class);
        startActivity(intent);
        finish(); // Optional: Close the current activity
    }

    private void navigateToAdminPanel() {
        Intent intent = new Intent(Signup.this, AdminPanel.class);
        startActivity(intent);
        finish(); // Optional: Close the current activity
    }

}

