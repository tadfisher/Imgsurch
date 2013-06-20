package com.example.imgsurch;

import android.app.ActionBar;
import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit.Callback;
import retrofit.RequestHeaders;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Header;
import retrofit.client.Response;

public class MainActivity extends Activity implements SearchView.OnQueryTextListener {
    // Views
    private SearchView mSearchView;
    private GridView mGridView;

    // Data
    private List<String> mUrls = new ArrayList<String>();
    private int mAdapterChoice;

    // Helpers
    private ImageUrlAdapter mAdapter;
    private RestAdapter mRestAdapter;
    private ImgurService mService;
    private boolean mIsSearching;

    private SpinnerAdapter mSpinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRestAdapter = new RestAdapter.Builder()
                .setServer("https://api.imgur.com/3/")
                .setRequestHeaders(new RequestHeaders() {
                    @Override
                    public List<Header> get() {
                        return Collections.singletonList(new Header("Authorization", "Client-ID 1ca6c3eeba08b54"));
                    }
                })
                .setDebug(true)
                .build();

        mService = mRestAdapter.create(ImgurService.class);

        mGridView = (GridView) findViewById(R.id.grid_view);

        mSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.action_list,
                android.R.layout.simple_spinner_dropdown_item);
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actionBar.setListNavigationCallbacks(mSpinnerAdapter, mOnNavigationListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        mSearchView = (SearchView) menu.findItem(R.id.search).getActionView();
        mSearchView.setIconifiedByDefault(true);
        mSearchView.setQueryHint("Search Imgur");
        mSearchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        // Launch the search request
        doSearch(s);

        // Hide the keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    private void doSearch(String query) {
        if (mIsSearching) {
            return;
        }

        mIsSearching = true;
        mService.searchImages(query, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                String body;
                try {
                    InputStream in = response.getBody().in();
                    body = convertStreamToString(in);
                } catch (IOException e) {
                    Log.e("IMGSURCH", "Error", e);
                    return;
                }

                mUrls.clear();
                try {
                    JSONObject json = new JSONObject(body);
                    JSONArray data = json.getJSONArray("data");

                    for (int i = 0; i < data.length(); i++) {
                        JSONObject image = data.getJSONObject(i);

                        if (image.getBoolean("is_album") || image.getBoolean("nsfw")) continue;

                        String id = image.getString("id");

                        mUrls.add(image.getString("link").replace("\\", "").replace(id, id + "b"));
                    }
                } catch (JSONException e) {
                    Toast.makeText(MainActivity.this, "Decode FAIL", Toast.LENGTH_LONG).show();
                    return;
                }


                mAdapter.notifyDataSetInvalidated();
                mAdapter.notifyDataSetChanged();

                mIsSearching = false;
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Toast.makeText(MainActivity.this, "FAIL", Toast.
                        LENGTH_LONG).show();
                mIsSearching = false;
            }
        });
    }

    private ActionBar.OnNavigationListener mOnNavigationListener = new ActionBar.OnNavigationListener() {
        @Override
        public boolean onNavigationItemSelected(int itemPosition, long itemId) {
            mAdapterChoice = itemPosition;
            switch (mAdapterChoice) {
                case 0:
                    mAdapter = new VolleyImageUrlAdapter(MainActivity.this, mUrls);
                    break;
                case 1:
                    mAdapter = new PicassoImageUrlAdapter(MainActivity.this, mUrls);
                    break;
                case 2:
                    mAdapter = new IonImageUrlAdapter(MainActivity.this, mUrls);
                    break;
            }
            mGridView.setAdapter(mAdapter);
            return true;
        }
    };

    private String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
