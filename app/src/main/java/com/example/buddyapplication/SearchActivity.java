package com.example.buddyapplication;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

    FriendAdapter adapter;

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

        adapter = new FriendAdapter(this, friendResults);
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

        friendResults.clear();

        if (keyword.isEmpty()) {
            friendResults.addAll(db.getAllFriend());
        } else {
            friendResults.addAll(db.searchFriends(keyword));
        }

        adapter.notifyDataSetChanged();
        tvResultCount.setText("Results: " + friendResults.size());
    }

    private void loadAllFriends() {
        friendResults.clear();
        friendResults.addAll(db.getAllFriend());
        adapter.notifyDataSetChanged();
        tvResultCount.setText("Results: " + friendResults.size());
    }

    private void showFriendDetails(friend f) {
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        View view = getLayoutInflater().inflate(R.layout.dialog_friend_details, null);
        dialog.setView(view);

        TextView tvName = view.findViewById(R.id.tvName);
        TextView tvGender = view.findViewById(R.id.tvGender);
        TextView tvPhone = view.findViewById(R.id.tvPhone);
        TextView tvEmail = view.findViewById(R.id.tvEmail);
        TextView tvAddress = view.findViewById(R.id.tvAddress);

        tvName.setText("Full Name\n" + f.name);
        tvGender.setText("Gender\n" + f.gender);
        tvPhone.setText("Phone\n" + f.phone);
        tvEmail.setText("Email Address\n" + f.email);

        String address = f.addr1 + ", " + f.addr2 + "\n" +
                         f.addr3 + " " + f.addr4;

        tvAddress.setText("Residental Address\n" + address);

        view.findViewById(R.id.btnEdit).setOnClickListener(v -> {
           dialog.dismiss();
           Intent intent = new Intent(this, EditFriend.class);
           intent.putExtra("friend_id", f.id);
           editFriendLauncher.launch(intent);
        });

        view.findViewById(R.id.btnDelete).setOnClickListener(v -> {
            dialog.dismiss();
            confirmDeleteFriend(f);
        });

        view.findViewById(R.id.btnOk).setOnClickListener(v -> dialog.dismiss());

        dialog.show();
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