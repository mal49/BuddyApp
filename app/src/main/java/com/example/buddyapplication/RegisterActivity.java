package com.example.buddyapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RegisterActivity extends AppCompatActivity {

    EditText etUser, etPass, etConfirm;
    Button btnRegister;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUser = findViewById(R.id.etRegUsername);
        etPass = findViewById(R.id.etRegPassword);
        etConfirm = findViewById(R.id.etRegConfirm);
        btnRegister = findViewById(R.id.btnRegister);

        db = new DatabaseHelper(this);

        btnRegister.setOnClickListener(v -> {
            String u = etUser.getText().toString().trim();
            String p = etPass.getText().toString().trim();
            String c = etConfirm.getText().toString().trim();

            if (u.isEmpty() || p.isEmpty() || c.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!p.equals(c)) {
                Toast.makeText(this, "Password not match", Toast.LENGTH_SHORT).show();
                return;
            }

            if (db.registerUser(u, p)) {
                Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Username already existed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}