package com.example.buddyapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ForgotPassword extends AppCompatActivity {

    EditText etUsername, etSecurityAnswer, etNewPassword, etConfirmPassword;
    Button btnResetPassword;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        etUsername = findViewById(R.id.etFpUsername);
        etSecurityAnswer = findViewById(R.id.etFpSecurity);
        etNewPassword = findViewById(R.id.etFpNewPassword);
        etConfirmPassword = findViewById(R.id.etFpConfirmPassword);
        btnResetPassword = findViewById(R.id.btnResetPassword);

        db = new DatabaseHelper(this);

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = etUsername.getText().toString().trim();
                String answer = etSecurityAnswer.getText().toString().trim();
                String newPass = etNewPassword.getText().toString().trim();
                String confirmPass = etConfirmPassword.getText().toString().trim();

                // 1- nak check kosong ke x
                if (username.isEmpty() || answer.isEmpty() ||
                        newPass.isEmpty() || confirmPass.isEmpty()) {
                    Toast.makeText(ForgotPassword.this,
                            "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 2- nak check password match ke x
                if (!newPass.equals(confirmPass)) {
                    Toast.makeText(ForgotPassword.this,
                            "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 3- kene verify security ans.
                boolean validAnswer = db.checkSecurityAnswer(username, answer);

                if (!validAnswer) {
                    Toast.makeText(ForgotPassword.this,
                            "Invalid username or security answer", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 4- line update password kat db
                boolean updated = db.updatePassword(username, newPass);

                if (updated) {
                    Toast.makeText(ForgotPassword.this,
                            "Password reset successful", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(ForgotPassword.this,
                            "Failed to reset password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
