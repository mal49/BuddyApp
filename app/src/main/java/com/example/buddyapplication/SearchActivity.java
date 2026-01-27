package com.example.buddyapplication;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    EditText etKeyword;
    Button btnSearch, btnBack;
    ListView listResults;
    TextView tvResultCount;

    DatabaseHelper db;

    ArrayList<friend> friendResults = new ArrayList<>();
    ArrayList<String> displayList = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        etKeyword = findViewById(R.id.etKeyword);
        btnSearch = findViewById(R.id.btnSearch);
        btnBack = findViewById(R.id.btnBack);
        listResults = findViewById(R.id.listResults);
        tvResultCount = findViewById(R.id.tvResultCount);

        db = new DatabaseHelper(this);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, displayList);
        listResults.setAdapter(adapter);

        btnSearch.setOnClickListener(v -> doSearch());

        btnBack.setOnClickListener(v -> finish());

        listResults.setOnItemClickListener((parent, view, position, id) -> {
            friend f = friendResults.get(position);
            showFriendDetails(f);
        });
    }

    private void doSearch() {
        String keyword = etKeyword.getText().toString().trim();

        if (keyword.isEmpty()) {
            Toast.makeText(this, "Enter name or phone to search", Toast.LENGTH_SHORT).show();
            return;
        }

        friendResults = db.searchFriends(keyword);

        displayList.clear();
        for (friend f : friendResults) {
            // Simple one-line display
            displayList.add(
                    f.name + " | " + f.phone + " | " + f.gender + " | " + f.state
            );
        }

        adapter.notifyDataSetChanged();
        tvResultCount.setText("Results: " + friendResults.size());
    }

    private void showFriendDetails(friend f) {
        String address =
                f.addr1 + "\n" +
                        f.addr2 + "\n" +
                        f.addr3 + "\n" +
                        f.addr4;

        String msg =
                "Name: " + f.name + "\n" +
                        "Gender: " + f.gender + "\n" +
                        "Phone: " + f.phone + "\n" +
                        "Email: " + f.email + "\n\n" +
                        "Address:\n" + address + "\n\n" +
                        "State: " + f.state;

        new AlertDialog.Builder(this)
                .setTitle("Friend Details")
                .setMessage(msg)
                .setPositiveButton("OK", null)
                .show();
    }
}
