package com.example.buddyapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ChangePassword extends AppCompatActivity {

    EditText etOld, etNew, etConfirm;
    Button btnChange;
    DatabaseHelper db;

    String currentUsername = "admin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        etOld = findViewById(R.id.etOldPassword);
        etNew = findViewById(R.id.etNewPassword);
        etConfirm = findViewById(R.id.etConfirmPassword);
        btnChange = findViewById(R.id.btnChangePassword);

        db = new DatabaseHelper(this);

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String oldPass = etOld.getText().toString();
                String newPass = etNew.getText().toString();
                String confirmPass = etConfirm.getText().toString();

                if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
                    Toast.makeText(ChangePassword.this,
                            "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!newPass.equals(confirmPass)) {
                    Toast.makeText(ChangePassword.this,
                            "New passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!db.checkOldPassword(currentUsername, oldPass)) {
                    Toast.makeText(ChangePassword.this,
                            "Old password is incorrect", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean updated = db.updatePassword(currentUsername, newPass);

                if (updated) {
                    Toast.makeText(ChangePassword.this,
                            "Password changed successfully :)", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(ChangePassword.this,
                            "Failed to update password :(", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}