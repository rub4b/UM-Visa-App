package com.example.umvisaapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class HomeFragment extends Fragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ImageButton imageButton1 = view.findViewById(R.id.imageButton);
        ImageButton imageButton2 = view.findViewById(R.id.imageButton2);
        ImageButton imageButton3 = view.findViewById(R.id.imageButton3);
        ImageButton imageButton5 = view.findViewById(R.id.imageButton5);

        imageButton1.setOnClickListener(this);
        imageButton2.setOnClickListener(this);
        imageButton3.setOnClickListener(this);
        imageButton5.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        // Handle button clicks
        if (v.getId() == R.id.imageButton) {
            openFragment(new CampusMap());
        } else if (v.getId() == R.id.imageButton2) {
            openFragment(new Socials());
        } else if (v.getId() == R.id.imageButton3) {
            openFragment(new FAQ());
        } else if (v.getId() == R.id.imageButton5) {
            openFragment(new Hotline());
        }
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
