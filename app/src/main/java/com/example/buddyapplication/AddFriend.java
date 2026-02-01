package com.example.buddyapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class AddFriend extends AppCompatActivity {

    private ImageView imgProfile;
    private EditText etName, etPhone, etEmail, etAddr1, etAddr2, etAddr3, etAddr4;
    private RadioGroup rgGender;
    private RadioButton rbMale, rbFemale;
    private Button btnChoosePhoto, btnSave, btnCancel;

    private DatabaseHelper dbHelper;
    private Uri selectedPhotoUri = null;
    private ActivityResultLauncher<String[]> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        dbHelper = new DatabaseHelper(this);

        // Initialize image picker launcher
            imagePickerLauncher = registerForActivityResult(
                    new ActivityResultContracts.OpenDocument(),
                    uri -> {
                        if (uri != null) {
                            selectedPhotoUri = uri;
                            imgProfile.setImageURI(uri);

                            getContentResolver().takePersistableUriPermission(
                                    uri,
                                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                            );
                        }
                    }
            );

        initializeViews();
        setupListeners();
    }

    private void initializeViews() {
        imgProfile = findViewById(R.id.imgProfile);
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        etAddr1 = findViewById(R.id.etAddr1);
        etAddr2 = findViewById(R.id.etAddr2);
        etAddr3 = findViewById(R.id.etAddr3);
        etAddr4 = findViewById(R.id.etAddr4);
        rgGender = findViewById(R.id.rgGender);
        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        // Set default profile image
        imgProfile.setImageResource(R.drawable.default_profile);
    }

    private void setupListeners() {
        imgProfile.setOnClickListener(v -> showPhotoOptions());

        btnSave.setOnClickListener(v -> saveFriend());

        btnCancel.setOnClickListener(v -> finish());
    }

    private void showPhotoOptions() {
        if (selectedPhotoUri == null) {
            String[] options = {"Choose Photo from Device", "Cancel"};
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Profile Photo")
                    .setItems(options, (dialog, which) -> {
                        if (which == 0) {
                            imagePickerLauncher.launch(new String[]{"image/*"});
                        } else {
                            dialog.dismiss();
                        }
                    })
                    .show();
        } else {
            String[] options = {"Choose Photo from Device", "Delete Photo", "Cancel"};
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Profile Photo")
                    .setItems(options, (dialog, which) -> {
                        if (which == 0) {
                            imagePickerLauncher.launch(new String[]{"image/*"});
                        } else if (which == 1) {
                            deletePhoto();
                        } else {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }
    }

    private void deletePhoto() {
        selectedPhotoUri = null;
        imgProfile.setImageResource(R.drawable.default_profile);
        Toast.makeText(this, "Photo removed", Toast.LENGTH_SHORT).show();
    }


    private void saveFriend() {
        // Validate required fields
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        if (name.isEmpty()) {
            Toast.makeText(this, "Name is required", Toast.LENGTH_SHORT).show();
            etName.requestFocus();
            return;
        }

        if (phone.isEmpty()) {
            Toast.makeText(this, "Phone is required", Toast.LENGTH_SHORT).show();
            etPhone.requestFocus();
            return;
        }

        int selectedGenderId = rgGender.getCheckedRadioButtonId();
        if (selectedGenderId == -1) {
            Toast.makeText(this, "Gender is required", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create new friend object
        friend newFriend = new friend();
        newFriend.name = name;
        newFriend.phone = phone;
        newFriend.email = etEmail.getText().toString().trim();
        newFriend.addr1 = etAddr1.getText().toString().trim();
        newFriend.addr2 = etAddr2.getText().toString().trim();
        newFriend.addr3 = etAddr3.getText().toString().trim();
        newFriend.addr4 = etAddr4.getText().toString().trim();

        RadioButton selectedGender = findViewById(selectedGenderId);
        newFriend.gender = selectedGender.getText().toString();

        if (selectedPhotoUri != null) {
            newFriend.photo_uri = selectedPhotoUri.toString();
        } else {
            newFriend.photo_uri = null;
        }

        // Insert into database
        boolean result = dbHelper.insertFriend(newFriend);

        if (result) {
            Toast.makeText(this, "Friend added successfully", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "Failed to add friend", Toast.LENGTH_SHORT).show();
        }
    }
}