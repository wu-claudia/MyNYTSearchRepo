package com.claudiawu.nytimessearch.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.claudiawu.nytimessearch.ArticleArrayAdapter;
import com.claudiawu.nytimessearch.EndlessRecyclerViewScrollListener;
import com.claudiawu.nytimessearch.R;
import com.claudiawu.nytimessearch.models.Article;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class HomeActivity extends AppCompatActivity{

    @BindView(R.id.toolbar) Toolbar toolbar;
    RecyclerView rvResults;
    ArrayList<Article> articles;
    ArticleArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupViews();
    }

    public void setupViews() {
        rvResults = (RecyclerView) findViewById(R.id.rvResults);
        //btnSearch = (Button) findViewById(R.id.btnSearch);
        articles = new ArrayList<>();
        adapter = new ArticleArrayAdapter(articles);
        rvResults.setItemAnimator(new SlideInUpAnimator());
        rvResults.setAdapter(adapter);
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        rvResults.setLayoutManager(gridLayoutManager);
        rvResults.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                customLoadMoreDataFromApi(page);
            }
        });
    }

    public void customLoadMoreDataFromApi(int page) {
        onSearch(page);
        int curSize = adapter.getItemCount();
        adapter.notifyItemRangeInserted(curSize, articles.size() - 1);
    }


    // Use top stories API
    public void onSearch(final int page) {
        //String query = etQuery.getText().toString();

        //showProgressBar();
        //pd.show();
        //Toast.makeText(this, "Searching for" + query, Toast.LENGTH_SHORT).show();
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://api.nytimes.com/svc/topstories/v1/articlesearch.json";

        RequestParams params = new RequestParams();
        params.put("api-key","121eaa96660c4f1a85c295140fd9d8df");
        params.put("page",page);
        //params.put("q",searchWord);


        client.get(url,params,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //Log.d("DEBUG",response.toString());
                JSONArray articleJsonResults = null;
                //hideProgressBar();
                //pd.hide();
                try {
                    articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                    articles.addAll(Article.fromJSONArray(articleJsonResults));
                    adapter.notifyDataSetChanged();
                    Log.d("DEBUG",articles.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
