package com.example.newsfeed;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(@NonNull Context context, @NonNull List<News> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) convertView = LayoutInflater
                .from(getContext())
                .inflate(R.layout.list_item, parent, false);

        final News news = getItem(position);

        if (news != null) {
            final ImageView liveIV = convertView.findViewById(R.id.live_iv);
            final TextView sectionNameTV = convertView.findViewById(R.id.sectionName_tv);
            final TextView webTitleTV = convertView.findViewById(R.id.webTitle_tv);
            final TextView authorNameTV = convertView.findViewById(R.id.authorName_tv);
            final TextView webPubDateTV = convertView.findViewById(R.id.webPubDate_tv);

            liveIV.setVisibility(news.isLive() ? View.VISIBLE : View.GONE);
            sectionNameTV.setText(news.getSectionName());
            webTitleTV.setText(news.getWebTitle());
            if (news.getAuthorName() == null) authorNameTV.setVisibility(View.GONE);
            else authorNameTV.setText(news.getAuthorName());
            webPubDateTV.setText(news.getWebPubDate());

            convertView.setOnClickListener(event -> getContext().startActivity(
                    new Intent(Intent.ACTION_VIEW).setData(Uri.parse(news.getWebUrl()))));
        }

        return convertView;
    }
}
