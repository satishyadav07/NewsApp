package com.satish.newsapp.RecyclerViewClass;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.satish.newsapp.R;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {
    TextView text_title, text_source;
    ImageView img_headline;
    CardView cardView;
    public RecyclerViewHolder(@NonNull View itemView) {
        super(itemView);

        text_title=itemView.findViewById(R.id.text_title);
        text_source=itemView.findViewById(R.id.text_source);
        img_headline=itemView.findViewById(R.id.img_headline);
        cardView=itemView.findViewById(R.id.main_container);
    }
}
