package com.example.umvisaapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ProfileFragment extends Fragment {

    private ImageButton settingButton;
    private EditText nameEditText, emailEditText, phoneEditText, addressEditText, nationalityEditText;
    private Button saveButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        settingButton = view.findViewById(R.id.SettingButton);
        nameEditText = view.findViewById(R.id.Name);
        emailEditText = view.findViewById(R.id.EmailAddress);
        phoneEditText = view.findViewById(R.id.Phone);
        addressEditText = view.findViewById(R.id.Address);
        nationalityEditText = view.findViewById(R.id.Nationality);
        saveButton = view.findViewById(R.id.button4);

        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayout, new ProfileSettingsFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfileData();
            }
        });

        fetchAndDisplayProfileData();

        return view;
    }

    private void saveProfileData() {
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        String name = nameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String address = addressEditText.getText().toString();
        String nationality = nationalityEditText.getText().toString();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference profileRef = db.collection("users").document(uid); // Change the collection name to "users"

        Map<String, Object> profileData = new HashMap<>();
        profileData.put("name", name);
        profileData.put("email", email);
        profileData.put("phone", phone);
        profileData.put("address", address);
        profileData.put("nationality", nationality);

        profileRef.set(profileData, SetOptions.merge())
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getActivity(), "Profile data saved", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getActivity(), "Failed to save profile data", Toast.LENGTH_SHORT).show();
                });
    }

    private void fetchAndDisplayProfileData() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference profileRef = db.collection("users").document(uid); // Change the collection name to "users"

        profileRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String name = documentSnapshot.getString("name");
                        String email = documentSnapshot.getString("email");
                        String phone = documentSnapshot.getString("phone");
                        String address = documentSnapshot.getString("address");
                        String nationality = documentSnapshot.getString("nationality");

                        nameEditText.setText(name);
                        emailEditText.setText(email);
                        phoneEditText.setText(phone);
                        addressEditText.setText(address);
                        nationalityEditText.setText(nationality);

                        // Make the EditText fields not editable
                        nameEditText.setEnabled(false);
                        emailEditText.setEnabled(false);
                        phoneEditText.setEnabled(false);
                        addressEditText.setEnabled(false);
                        nationalityEditText.setEnabled(false);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getActivity(), "Failed to fetch profile data", Toast.LENGTH_SHORT).show();
                });
    }
}
