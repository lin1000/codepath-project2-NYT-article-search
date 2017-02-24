package com.codepath.week2assignment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import com.codepath.week2assignment.adapters.ArticleArrayAdapter;
import com.codepath.week2assignment.fragments.FilterDialogueFragment;
import com.codepath.week2assignment.fragments.FilterDialogueFragment.FilterDialogueFragmentCallBackInterface;
import com.codepath.week2assignment.fragments.FilterDialogueFragmentFactory;
import com.codepath.week2assignment.model.Meta;
import com.codepath.week2assignment.model.NYTArticle;
import com.codepath.week2assignment.model.UIFilter;
import com.codepath.week2assignment.net.NYTClient;
import com.codepath.week2assignment.uilistener.EndlessScrollListener;
import com.facebook.stetho.Stetho;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class SearchActivity extends AppCompatActivity implements FilterDialogueFragmentCallBackInterface {

    public enum UIAction {
        RELOAD, ADD
    }


    @BindView(R.id.gvResults) GridView gvResults;
    @BindView(R.id.toolbar) Toolbar tlToolbar;

    SearchView svSearchView;
    ArrayList<NYTArticle> nytArticles;
    ArrayAdapter<NYTArticle> nytArticleArrayAdapter;

    //lazy loading
    UIFilter uiFilter;
    FilterDialogueFragment filterDialogueFragment;
    String currentQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Stetho.initializeWithDefaults(this);
        ButterKnife.bind(this);

        //Toolbar
        setSupportActionBar(tlToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_earth);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        //Async task
        //new LoadNYTActivity().execute("https://api.nytimes.com/svc/search/v2/articlesearch.json?begin_date=20160112&sort=oldest&fq=news_desk:(%22Education%22%20%22Health%22)&api-key=193fa1402e104f3887dd022db531078f");

        //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        //        .setAction("Action", null).show();
        nytArticles = new ArrayList<>();
        nytArticleArrayAdapter = new ArticleArrayAdapter(this, nytArticles);
        gvResults.setAdapter(nytArticleArrayAdapter);
        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //create an intent to display the aricle
                Intent intent = new Intent(SearchActivity.this, ArticleWebViewActivity.class);
                //get the article to display
                String url = nytArticles.get(position).getWebUrl();
                //pass in that article into intent
                intent.putExtra("url",url);
                //launch the activity
                startActivity(intent);
            }
        });
        // Attach the listener to the AdapterView onCreate
        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                Log.d(this.getClass().getName(),"page="+page);
                Log.d(this.getClass().getName(),"totalItemsCount="+totalItemsCount);
                searchArticle(page,currentQuery,uiFilter,UIAction.ADD);
                // or loadNextDataFromApi(totalItemsCount);
                return true; // ONLY if more data is actually being loaded; false otherwise.
            }
        });

        uiFilter = new UIFilter();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        svSearchView =  searchView;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                searchView.clearFocus();
                Log.d(this.getClass().getName(),"query:"+ query);
                currentQuery = query;
                Toast.makeText(SearchActivity.this,"Searching for " + query + (uiFilter.isActivated()?" (Advanced Search)":""), Toast.LENGTH_LONG).show();
                searchArticle(0, query, uiFilter,UIAction.RELOAD);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(this.getClass().getName(),"onQueryTextChange:"+ newText);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Log.d(this.getClass().getName(), "item.getItemId()="+ item.getItemId());

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_filter) {
            Log.d(this.getClass().getName(), " R.id.action_filter Selected");
            showEditDialog();
            return true;
        } else if ( id == R.id.action_search){

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showEditDialog(){
        FragmentManager fm = getSupportFragmentManager();
        filterDialogueFragment = FilterDialogueFragmentFactory.getInstance();
        filterDialogueFragment.show(fm, "filter_fragment");
    }

    public GridView getGvResults() {
        return gvResults;
    }

    public void searchArticle(int page, String query, UIFilter filter, final UIAction uiAction) {
        NYTClient nytClient = new NYTClient(this);
        Request request = nytClient.requestBuilderByUIFilter(page, query, filter);
        nytClient.getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(this.getClass().getName(), e.getMessage());
                // Run view-related code back on the main thread
                SearchActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SearchActivity.this, "Network is Disconnected", Toast.LENGTH_LONG).show();
                    }

                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                // ... check for failure using `isSuccessful` before proceeding
                Log.d(this.getClass().getName(), "onResponse");
                final ArrayList<NYTArticle> localNYTArticles;

                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                // Read data on the worker thread
                final String jsonData = response.body().string();

                try {
                    JSONObject jsonObject = new JSONObject(jsonData);
                    JSONObject responseObject = jsonObject.getJSONObject("response");
                    JSONObject metaObject = responseObject.getJSONObject("meta");
                    Meta meta = new Meta(metaObject);
                    String status = jsonObject.getString("status");
                    JSONArray docsArray = responseObject.getJSONArray("docs");
                    localNYTArticles = NYTArticle.fromJSONArray(docsArray);
                    Log.d(this.getClass().getName(), "meta.getHits()=" + meta.getHits());
                    Log.d(this.getClass().getName(), "meta.getOffset()=" + meta.getOffset());
                    Log.d(this.getClass().getName(), "meta.getTime()=" + meta.getTime());
                    Log.d(this.getClass().getName(), "Status=" + jsonObject.getString("status"));
                    Log.d(this.getClass().getName(), "localNYTArticles.size()=" + localNYTArticles.size());

                } catch (JSONException e) {
                    Log.d(this.getClass().getName(), "Unexpected JSON response need to be investigated.");
                    e.printStackTrace();
                    // Run view-related code back on the main thread
                    SearchActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SearchActivity.this, "Something wrong.", Toast.LENGTH_LONG).show();
                        }
                    });
                    return;
                }

                // Run view-related code back on the main thread
                SearchActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (localNYTArticles == null) {
                            Toast.makeText(SearchActivity.this, "No Result Found.", Toast.LENGTH_LONG).show();
                        } else if (localNYTArticles.size() == 0) {
                            Toast.makeText(SearchActivity.this, "Nothing Found ! Try other words. ", Toast.LENGTH_LONG).show();
                        } else {
                            switch(uiAction){
                                case RELOAD:
                                    nytArticleArrayAdapter.clear();
                                    nytArticleArrayAdapter.addAll(localNYTArticles);
                                    break;
                                case ADD:
                                    nytArticleArrayAdapter.addAll(localNYTArticles);
                                    break;
                                default:
                                    Toast.makeText(SearchActivity.this, "Opps", Toast.LENGTH_SHORT).show();
                                break;
                            }

                        }

                    }
                });
            }
        });

    }

    @Override
    public void onFinishSettingDialog(UIFilter uiFilter) {

        this.uiFilter = uiFilter;
        if(this.uiFilter!=null && this.uiFilter.isActivated()){
            Toast.makeText(SearchActivity.this,"Advanced Search Setting Saved.", Toast.LENGTH_LONG).show();
            Log.d(this.getClass().getName(), "String.valueOf(svSearchView.getQuery())=" + String.valueOf(svSearchView.getQuery()));
            Log.d(this.getClass().getName(), "uiFilter.getBeginDate()=" + uiFilter.getBeginDate());
            Log.d(this.getClass().getName(), "uiFilter.getSort()=" + uiFilter.getSort());
        } else {
            Toast.makeText(SearchActivity.this,"Advanced Search Setting Cleared!.", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isOnlineAndToast(){
        Toast.makeText(SearchActivity.this,"Testing Network Connectivity...", Toast.LENGTH_SHORT).show();
        if(isOnline()){
            Toast.makeText(SearchActivity.this,"Internet is available, enjoy! ", Toast.LENGTH_LONG).show();
            return true;
        }else{
            Toast.makeText(SearchActivity.this,"Internet is not available, please check your connection!", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }
        return false;
    }

}
