package com.example.umvisaapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class NewApplicationFragment extends Fragment {

    private static final int PICK_FILE_REQUEST = 1;
    private ImageButton downloadButton;
    private ImageButton uploadButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_application, container, false);

        downloadButton = view.findViewById(R.id.imageButton5);
        uploadButton = view.findViewById(R.id.imageButton);

        downloadButton.setOnClickListener(v -> downloadFile());
        uploadButton.setOnClickListener(v -> openFilePicker());

        return view;
    }

    private void downloadFile() {
        // Replace "APPLICATION FOR NEW STUDENT IN MALAYSIA.pdf" with the actual file name
        String fileName = "APPLICATION FOR NEW STUDENT IN MALAYSIA.pdf";

        FirebaseStorage storage = FirebaseStorage.getInstance();
        // Replace "Application files" with the folder where you store user files
        StorageReference fileRef = storage.getReference().child("Application files").child(fileName);

        final long ONE_MEGABYTE = 1024 * 1024;
        fileRef.getBytes(ONE_MEGABYTE)
                .addOnSuccessListener(bytes -> {
                    // Save the downloaded file to a local location
                    saveFileLocally(fileName, bytes);

                    // Handle other download logic if needed
                    Toast.makeText(requireContext(), "Download successful", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    Toast.makeText(requireContext(), "Download failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void saveFileLocally(String fileName, byte[] bytes) {
        try {
            File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            File file = new File(directory, fileName);

            // Use FileOutputStream with try-with-resources to automatically close the stream
            try (FileOutputStream outputStream = new FileOutputStream(file)) {
                outputStream.write(bytes);
            }

            // Notify the system that a new file was created
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(Uri.fromFile(file));
            requireContext().sendBroadcast(intent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, PICK_FILE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri fileUri = data.getData();
            uploadFile(fileUri);
        }
    }

    private void uploadFile(Uri fileUri) {
        // Replace "Application files" with the folder where you want to store user files
        String userFolder = "User upload/" + Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        String fileName = "APPLICATION FOR NEW STUDENT IN MALAYSIA.pdf";

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference fileRef = storage.getReference().child(userFolder).child(fileName);

        fileRef.putFile(fileUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // File uploaded successfully
                    Toast.makeText(requireContext(), "Upload successful", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    Toast.makeText(requireContext(), "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
