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

import com.claudiawu.nytimessearch.activities.ArticleActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ArticleArrayAdapter extends RecyclerView.Adapter<ArticleArrayAdapter.ViewHolder> {

    private ArrayList<Article> articles;

    public ArticleArrayAdapter(ArrayList<Article> articles) {
        this.articles = articles;
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView tvTitle;
        public ImageView ivImage;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
            itemView.setOnClickListener(this);
        }

        @ Override
        public void onClick(View view) {
            int position = getLayoutPosition(); // gets item position
            // We can access the data within the views
            //Toast.makeText(view.getContext(), article.getHeadline(), Toast.LENGTH_SHORT).show();
            Intent i = new Intent(view.getContext(), ArticleActivity.class);
            // get the article to display
            Article article = articles.get(position);
            // pass in that article into intent
            i.putExtra("article",article);
            // launch the activity
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
            Picasso.with(imageView.getContext()).load(thumbnail).into(imageView);
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
