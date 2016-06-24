package com.claudiawu.nytimessearch;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.claudiawu.nytimessearch.activities.ArticleActivity;
import com.claudiawu.nytimessearch.models.Article;

import org.parceler.Parcels;

import java.util.ArrayList;

public class ArticleArrayAdapter extends RecyclerView.Adapter<ArticleArrayAdapter.ViewHolder> {

    private ArrayList<Article> articles;

    public ArticleArrayAdapter(ArrayList<Article> articles) {
        this.articles = articles;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tvTitle;
        public ImageView ivImage;

        public ViewHolder(View itemView) {

            super(itemView);

            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
            itemView.setOnClickListener(this);
        }

        @ Override
        public void onClick(View view) {
            int position = getLayoutPosition(); // gets item position

            //Toast.makeText(view.getContext(), article.getHeadline(), Toast.LENGTH_SHORT).show();
            Intent i = new Intent(view.getContext(), ArticleActivity.class);

            Article article = articles.get(position);

            i.putExtra("article", Parcels.wrap(article));

            view.getContext().startActivity(i);
        }
    }

    @Override
    public ArticleArrayAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_article_result, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ArticleArrayAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Article article = articles.get(position);

        // Set item views based on the data model
        TextView tvTitle = viewHolder.tvTitle;
        tvTitle.setText(article.getHeadline());

        ImageView imageView = viewHolder.ivImage;
        String thumbnail = article.getThumbNail();
        if (!TextUtils.isEmpty(thumbnail)) {
            //Picasso.with(imageView.getContext()).load(thumbnail).into(imageView);
            Glide.with(imageView.getContext()).load(thumbnail).into(imageView);
        }
    }

//    // Return the total count of items
//    @Override
//    public int getItemCount() {
//        return articles.size();
//    }

    @Override
    public int getItemCount() {
        if(articles == null)
            return 0;
        else
            return articles.size();
    }
}
