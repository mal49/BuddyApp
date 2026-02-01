package com.example.buddyapplication;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class FriendAdapter extends ArrayAdapter<friend> {

    public FriendAdapter(Context context, ArrayList<friend> list) {
        super(context, 0, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.row_friend_card, parent, false);
        }

        friend f = getItem(position);

        TextView tvName = convertView.findViewById(R.id.tvName);
        TextView tvPhone = convertView.findViewById(R.id.tvPhone);
        TextView tvState = convertView.findViewById(R.id.tvState);
        TextView tvGender = convertView.findViewById(R.id.tvGender);
        ImageView img = convertView.findViewById(R.id.imgProfile);

        tvName.setText(f.name);
        tvPhone.setText(f.phone);
        tvState.setText(f.addr4);
        tvGender.setText(f.gender.toUpperCase());

        if (f.photo_uri != null && !f.photo_uri.isEmpty()) {
            try {
                img.setImageURI(Uri.parse(f.photo_uri));
            } catch (Exception e) {
                img.setImageResource(R.drawable.default_profile);
            }
        } else {
            img.setImageResource(R.drawable.default_profile);
        }

        return convertView;
    }
}
