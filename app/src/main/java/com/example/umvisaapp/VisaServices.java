

package com.example.umvisaapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class VisaServices extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.visa_services);

        Button studentButton = findViewById(R.id.buttonStudent);
        Button adminButton = findViewById(R.id.buttonAdmin);

        studentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Student clicked", Toast.LENGTH_SHORT).show();
                openLoginClass(false); // Pass false for student
            }
        });

        adminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Admin clicked", Toast.LENGTH_SHORT).show();
                openLoginClass(true); // Pass true for admin
            }
        });
    }

    private void openLoginClass(boolean isAdmin) {
        Intent intent = new Intent(this, login.class);
        intent.putExtra("isAdmin", isAdmin);
        startActivity(intent);
    }
}
