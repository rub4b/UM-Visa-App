package com.example.umvisaapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class ApplicationFragment extends Fragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_application, container, false);

        Button newButton = view.findViewById(R.id.New);
        Button renewButton = view.findViewById(R.id.renew);
        Button cancelButton = view.findViewById(R.id.cancel);
        Button specialButton = view.findViewById(R.id.special);

        newButton.setOnClickListener(this);
        renewButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        specialButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        // Handle button clicks using if-else statements
        if (v.getId() == R.id.New) {
            openFragment(new NewApplicationFragment());
        } else if (v.getId() == R.id.renew) {
            openFragment(new RenewalFragment());
        } else if (v.getId() == R.id.cancel) {
            openFragment(new VisaCancellationFragment());
        } else if (v.getId() == R.id.special) {
            openFragment(new SpecialPassFragment());
        }
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
