package com.satish.newsapp.RecyclerViewClass;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.satish.newsapp.Models.Article;
import com.satish.newsapp.OnSelectListener.SelectListener;
import com.satish.newsapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    private Context context;
    private List<Article> headlinesList;
    private SelectListener listener;

    public RecyclerViewAdapter(Context context, List<Article> headlinesList, SelectListener listener) {
        this.context = context;
        this.headlinesList = headlinesList;
        this.listener=listener;
    }


    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.headline_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.text_title.setText(headlinesList.get(position).getTitle());
        holder.text_source.setText(headlinesList.get(position).getSource().getName());
        if (headlinesList.get(position).getUrlToImage()!=null){
            Picasso.get().load(headlinesList.get(position).getUrlToImage()).into(holder.img_headline);
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               listener.OnNewsClicked(headlinesList.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return  headlinesList.size();
    }
}
