package com.example.buddyapplication;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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

    ActivityResultLauncher<Intent> editFriendLauncher;

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

        loadAllFriends();

        editFriendLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        doSearch();
                    }
                }
        );

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
            loadAllFriends();
            return;
        }

        friendResults = db.searchFriends(keyword);

        displayList.clear();
        for (friend f : friendResults) {
            // Simple one-line display
            displayList.add(
                    f.name + " | " + f.phone + " | " + f.gender + " | " + f.addr4
            );
        }

        adapter.notifyDataSetChanged();
        tvResultCount.setText("Results: " + friendResults.size());
    }

    private void loadAllFriends() {
        friendResults = db.getAllFriend();

        displayList.clear();
        for (friend f : friendResults) {
            displayList.add(
                    f.name + " | " + f.phone + " | " + f.gender + " | " + f.addr4
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
                        "Address:\n" + address;
//                        "State: " + f.state;

        new AlertDialog.Builder(this)
                .setTitle("Friend Details")
                .setMessage(msg)
                .setPositiveButton("EDIT", (dialog, which) -> {
                    Intent intent = new Intent(SearchActivity.this, EditFriend.class);
                    intent.putExtra("friend_id", f.id);
                    editFriendLauncher.launch(intent);
                })
                .setNeutralButton("DELETE", (dialog, which) -> {
                    confirmDeleteFriend(f);
                })
                .setNegativeButton("OK", null)
                .show();
    }
    private void confirmDeleteFriend(friend f) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Friend")
                .setMessage("Are you sure you want to delete " + f.name + "?")
                .setPositiveButton("DELETE", (d, w) -> {
                    boolean ok = db.deleteFriend(f);

                    if (ok) {
                        Toast.makeText(this, "Friend deleted", Toast.LENGTH_SHORT).show();
                        doSearch(); // refresh list (keeps current keyword)
                    } else {
                        Toast.makeText(this, "Failed to delete friend", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("CANCEL", null)
                .show();
    }

}