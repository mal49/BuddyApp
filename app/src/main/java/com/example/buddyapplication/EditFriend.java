package com.example.buddyapplication;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EditFriend extends AppCompatActivity {

    EditText etName, etPhone, etEmail, etAddr1, etAddr2, etAddr3, etAddr4;
    RadioGroup rgGender;
    Button btnUpdate, btnCancel;

    ImageView imgProfile;

    DatabaseHelper db;
    int friendId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_friend);

        db = new DatabaseHelper(this);

        friendId = getIntent().getIntExtra("friend_id", -1);
        if (friendId == -1) {
            Toast.makeText(this, "Invalid Friend",Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        loadFriend();
        setupButtons();
    }

    private void initViews(){
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        etAddr1 = findViewById(R.id.etAddr1);
        etAddr2 = findViewById(R.id.etAddr2);
        etAddr3 = findViewById(R.id.etAddr3);
        etAddr4 = findViewById(R.id.etAddr4);
//        etState = findViewById(R.id.etState);

        rgGender = findViewById(R.id.rgGender);

        btnUpdate = findViewById(R.id.btnUpdate);
        btnCancel = findViewById(R.id.btnCancel);

        imgProfile = findViewById(R.id.imgProfile);
    }

    private void loadFriend() {
        friend f = db.getFriendById(friendId);

        if (f == null){
            Toast.makeText(this, "Friend not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        etName.setText(f.name);
        etPhone.setText(f.phone);
        etEmail.setText(f.email);
        etAddr1.setText(f.addr1);
        etAddr2.setText(f.addr2);
        etAddr3.setText(f.addr3);
        etAddr4.setText(f.addr4);
//        etState.setText(f.state);

        if ("Male".equalsIgnoreCase(f.gender)){
            rgGender.check(R.id.rbMale);
        } else {
            rgGender.check(R.id.rbFemale);
        }

        if (f.photo_uri != null) {
            imgProfile.setImageURI(Uri.parse(f.photo_uri));
        } else {
            imgProfile.setImageResource(R.drawable.default_profile);
        }
    }

    private void setupButtons(){
        btnUpdate.setOnClickListener(v -> updateFriend());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void updateFriend() {
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Name and Phone required", Toast.LENGTH_SHORT).show();
            return;
        }

        int genderId = rgGender.getCheckedRadioButtonId();
        if (genderId == -1) {
            Toast.makeText(this, "Select gender", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton rb = findViewById(genderId);

        friend f = new friend();
        f.id = friendId;
        f.name = name;
        f.gender = rb.getText().toString();
        f.phone = phone;
        f.email = etEmail.getText().toString().trim();
        f.addr1 = etAddr1.getText().toString().trim();
        f.addr2 = etAddr2.getText().toString().trim();
        f.addr3 = etAddr3.getText().toString().trim();
        f.addr4 = etAddr4.getText().toString().trim();
//        f.state = etState.getText().toString().trim();

        if (db.updateFriend(f)) {
            Toast.makeText(this, "Friend updated", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
        }
    }
}