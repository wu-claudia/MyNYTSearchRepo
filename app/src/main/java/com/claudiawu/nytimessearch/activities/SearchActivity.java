package com.claudiawu.nytimessearch.activities;

import android.app.ProgressDialog;
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
import org.parceler.Parcels;

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
    //boolean hasFilter = false;
    Filter filter;
    String API_KEY = "121eaa96660c4f1a85c295140fd9d8df";
    ProgressDialog pd;
    boolean settingsChanged = false;
    int numCols;
    StaggeredGridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        filter = new Filter();
        numCols = 4; //default value
        fetchArticles(0,filter);
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
        if (numCols != 0) {
            gridLayoutManager = new StaggeredGridLayoutManager(numCols, StaggeredGridLayoutManager.VERTICAL);
        } else {
            gridLayoutManager = new StaggeredGridLayoutManager(Math.max(4,numCols), StaggeredGridLayoutManager.VERTICAL);
        }
        rvResults.setLayoutManager(gridLayoutManager);
        rvResults.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                customLoadMoreDataFromApi(page,filter);
            }
        });
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

                fetchArticles(0,filter);
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
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            settingsChanged = true;
            Intent i = new Intent(SearchActivity.this, SettingsActivity.class);
            startActivityForResult(i,REQUEST_CODE);
        }

        if (id == R.id.action_filter) {
            Intent i = new Intent(SearchActivity.this, FilterActivity.class);
            startActivityForResult(i,REQUEST_CODE);
        }

        return super.onOptionsItemSelected(item);
    }

    public void onArticleSearch(View view) {
        fetchArticles(0,filter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // filter = (Filter) data.getSerializableExtra("filter");
            filter = Parcels.unwrap(data.getParcelableExtra("filter"));
            numCols = data.getExtras().getInt("setting");
            setupViews();
            Log.d("onActivityResult",String.valueOf(numCols));
            //Toast.makeText(this,filter.getBeginDate(),Toast.LENGTH_SHORT).show();
            articles.clear();
            adapter.notifyDataSetChanged();
            fetchArticles(0,filter);
        }
    }

    public void fetchArticles(final int page, Filter filter) {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://api.nytimes.com/svc/search/v2/articlesearch.json";

        final RequestParams params = new RequestParams();
        params.put("api-key",API_KEY);
        params.put("page",page);

        if (filter != null && filter.getArrayNewsDesk() != null) {
            params.put("fq",filter.getNewsDesk());
        }

        if (filter != null && filter.getSpinnerVal() != null) {
            params.put("sort",filter.getSpinnerVal());
        }

        if (filter != null && filter.getBeginDate() != null) {
            params.put("begin_date",filter.getBeginDate());
        }
        if (searchWord != null) {
            params.put("q",searchWord);
        } else {
            params.put("callback","callbackTopStories");
        }

        if (page == 0) {
            pd = new ProgressDialog(SearchActivity.this);
            pd.setTitle("Loading your articles...");
            pd.setMessage("Please wait.");
            pd.setCancelable(false);
            pd.show();
        }

        //Toast.makeText(this,params.toString(), Toast.LENGTH_SHORT).show();
        //Log.d("url",url+params);
                client.get(url,params,new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        //Log.d("DEBUG",response.toString());
                        JSONArray articleJsonResults = null;
                        pd.dismiss();
                        try {
                            if (page == 0) {
                                articles.clear();
                            }
                            articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                            articles.addAll(Article.fromJSONArray(articleJsonResults));
                            //pd.dismiss();
                            adapter.notifyDataSetChanged();
                            Log.d("DEBUG",articles.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response) {
                        Log.d("On Failure:",params.toString());
                    }
                });
    }
}
