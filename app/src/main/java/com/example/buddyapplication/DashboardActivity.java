package com.example.buddyapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {

    Button btnSearchFriend, addFriend, btnComingSoonReport, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        btnSearchFriend = findViewById(R.id.btnSearchFriend);
        addFriend = findViewById(R.id.btnManage);
        btnComingSoonReport = findViewById(R.id.btnReport);
        btnLogout = findViewById(R.id.btnLogout);

        btnSearchFriend.setOnClickListener(v ->
                startActivity(new Intent(this, SearchActivity.class))
        );

        addFriend.setOnClickListener(v ->
                startActivity(new Intent(this, AddFriend.class))
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