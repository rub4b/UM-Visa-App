package com.example.umvisaapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

public class ProfileSettingsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_settings, container, false);

        // Assuming you have a TextView with the id textView11 in fragment_profile_settings.xml
        TextView signOutTextView = view.findViewById(R.id.textView11);

        signOutTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sign out from Firebase Authentication
                FirebaseAuth.getInstance().signOut();

                // Redirect to the login screen or any other appropriate screen
                Intent intent = new Intent(requireContext(), login.class);
                startActivity(intent);
                requireActivity().finish(); // Optional: Close the current activity
            }
        });

        return view;
    }
}
