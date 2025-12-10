package com.example.lzlq;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class FavoritesAdapter extends ArrayAdapter<SignManager.SignData> {

    public FavoritesAdapter(Context context, List<SignManager.SignData> favorites) {
        super(context, 0, favorites);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SignManager.SignData sign = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_favorite_simple, parent, false);
        }

        TextView tvTitle = convertView.findViewById(R.id.tv_fav_simple_title);
        TextView tvType = convertView.findViewById(R.id.tv_fav_simple_type);

        tvTitle.setText(sign.getId() + ". " + sign.getTitle());
        tvType.setText("【" + sign.getType() + "】");

        return convertView;
    }
}
