package com.claudiawu.nytimessearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.claudiawu.nytimessearch.ArticleArrayAdapter;
import com.claudiawu.nytimessearch.EndlessRecyclerViewScrollListener;
import com.claudiawu.nytimessearch.R;
import com.claudiawu.nytimessearch.models.Article;
import com.claudiawu.nytimessearch.models.Filter;
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

public class SearchActivity extends AppCompatActivity {

    //EditText etQuery;
    //Button btnSearch;
    RecyclerView rvResults;

    ArrayList<Article> articles;
    ArticleArrayAdapter adapter;
    String searchWord;
    //MenuItem miActionProgressItem;

    @BindView(R.id.toolbar) Toolbar toolbar;
    //@BindView(R.id.rvResults) RecyclerView rvResults;
    private final int REQUEST_CODE = 50;
    boolean hasFilter = false;
    Filter filter;
    //ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        pd = new ProgressDialog(this);
//        pd.setTitle("Loading...");
//        pd.setMessage("Please wait.");
//        pd.setCancelable(false);
        setupViews();
    }

    public void setupViews() {
        //etQuery = (EditText) findViewById(R.id.etQuery);
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
                if (hasFilter) {
                    customLoadMoreDataFromApi(page,filter);
                }
                customLoadMoreDataFromApi(page);
            }
        });
    }

    // Append more data into the adapter
    public void customLoadMoreDataFromApi(int page) {
        onSearch(page);
        int curSize = adapter.getItemCount();
        adapter.notifyItemRangeInserted(curSize, articles.size() - 1);
    }

    public void customLoadMoreDataFromApi(int page, Filter filter) {
        fetchArticles(page,filter);
        int curSize = adapter.getItemCount();
        adapter.notifyItemRangeInserted(curSize, articles.size() - 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
                searchWord = query;
                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                onSearch(0);
                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_filter) {
            Intent i = new Intent(SearchActivity.this, FilterActivity.class);
            startActivityForResult(i,REQUEST_CODE);
        }

        return super.onOptionsItemSelected(item);
    }

    public void onArticleSearch(View view) {
        onSearch(0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            filter = (Filter) data.getSerializableExtra("filter");
            if (filter != null) {
                hasFilter = true;
            }
            setupViews();
            // network request for results - would do adapter stuff for you
            //rvResults.setAdapter(adapter);
            //adapter.notifyDataSetChanged();
        }
    }

    public void onSearch(final int page) {
        //String query = etQuery.getText().toString();

        //showProgressBar();
        //pd.show();
        //Toast.makeText(this, "Searching for" + query, Toast.LENGTH_SHORT).show();
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://api.nytimes.com/svc/search/v2/articlesearch.json";

        RequestParams params = new RequestParams();
        params.put("api-key","121eaa96660c4f1a85c295140fd9d8df");
        params.put("page",page);
        params.put("q",searchWord);

        client.get(url,params,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //Log.d("DEBUG",response.toString());
                JSONArray articleJsonResults = null;
                //hideProgressBar();
                //pd.hide();
                try {
                    if (page == 0) {
                        articles.clear();
                    }
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

    public void fetchArticles(final int page, Filter filter) {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://api.nytimes.com/svc/search/v2/articlesearch.json";

        //pd.show();
        RequestParams params = new RequestParams();
        params.put("api-key","121eaa96660c4f1a85c295140fd9d8df");
        params.put("page",page);
        params.put("q",searchWord);
        if (filter != null) {
            params.put("fq","news_desk: (" + filter.getNews_desk() + ")");
        }

                client.get(url,params,new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        //Log.d("DEBUG",response.toString());
                        JSONArray articleJsonResults = null;
                        //pd.dismiss();
                        //hideProgressBar();
                        try {
                            if (page == 0) {
                                articles.clear();
                            }
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
