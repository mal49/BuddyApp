package com.example.buddyapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {

    Button btnSearchFriend, btnComingSoonCrud, btnComingSoonReport, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        btnSearchFriend = findViewById(R.id.btnSearchFriend);
        btnComingSoonCrud = findViewById(R.id.btnManage);
        btnComingSoonReport = findViewById(R.id.btnReport);
        btnLogout = findViewById(R.id.btnLogout);

        btnSearchFriend.setOnClickListener(v ->
                startActivity(new Intent(this, SearchActivity.class))
        );

        btnComingSoonCrud.setOnClickListener(v ->
                Toast.makeText(this, "Coming soon: CRUDS screen", Toast.LENGTH_SHORT).show()
        );

        btnComingSoonReport.setOnClickListener(v ->
                Toast.makeText(this, "Coming soon: Graph report screen", Toast.LENGTH_SHORT).show()
        );

        btnLogout.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}