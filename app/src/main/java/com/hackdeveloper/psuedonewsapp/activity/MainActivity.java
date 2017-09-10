package com.hackdeveloper.psuedonewsapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.hackdeveloper.psuedonewsapp.R;
import com.hackdeveloper.psuedonewsapp.app.AppController;
import com.hackdeveloper.psuedonewsapp.clicklistener.CustomRVItemTouchListener;
import com.hackdeveloper.psuedonewsapp.clicklistener.RecyclerViewItemClickListener;
import com.hackdeveloper.psuedonewsapp.helper.NewsItem;
import com.hackdeveloper.psuedonewsapp.helper.Recycler_View_Adapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.CacheRequest;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private static final String apiUrl =  "http://starlord.hackerearth.com/newsjson";
    private static final String TAG = "PSEUDO NEWS";
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    List<NewsItem> newsItems = new ArrayList<>();
    public static List<NewsItem> favouriteItems = new ArrayList<>();

    ArrayList<String> publishers = new ArrayList<>();
    ArrayList<String> categories = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        fill_List();

    }

    private void fill_List() {

        JsonArrayRequest req = new JsonArrayRequest(apiUrl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("Response", response.toString());

                        try{
                            JSONArray jadata=new JSONArray(response.toString());
                            newsItems.clear();
                            for(int i=0;i<jadata.length();i++)
                            {
                                JSONObject jo=jadata.getJSONObject(i);
                                NewsItem newsItem = new NewsItem();

                                newsItem.setID(jo.getString("ID"));
                                newsItem.setTITLE(jo.getString("TITLE"));
                                newsItem.setURL(jo.getString("URL"));
                                newsItem.setPUBLISHER(jo.getString("PUBLISHER"));
                                newsItem.setCATEGORY(jo.getString("CATEGORY"));
                                newsItem.setHOSTNAME(jo.getString("HOSTNAME"));
                                newsItem.setTIMESTAMP(jo.getLong("TIMESTAMP"));
                                newsItems.add(newsItem);
                            }

                            SetupRecyclerView(newsItems);


                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                    }
                });

        AppController.getInstance().addToRequestQueue(req, TAG);

    }

    ArrayList<String> GetAllPublishers () {

        for (int i = 0;i<newsItems.size(); i++) {
            String publisher = newsItems.get(i).getPUBLISHER();
            if (!publishers.contains(publisher))
                publishers.add(publisher);
        }

        return publishers;
    }

    ArrayList<String> GetAllCategories () {

        for (int i = 0;i<newsItems.size(); i++) {
            String category = newsItems.get(i).getCATEGORY();
            if (!categories.contains(category))
                categories.add(category);
        }

        return categories;
    }



    private void SetupRecyclerView(final List<NewsItem> newsItems) {

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        Recycler_View_Adapter adapter = new Recycler_View_Adapter(newsItems, getApplication());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);

        recyclerView.addOnItemTouchListener(new CustomRVItemTouchListener(getApplicationContext(), recyclerView, new RecyclerViewItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                /*String postUrl = newsItems.get(position).getURL();

                Intent i = new Intent(MainActivity.this, BrowserActivity.class);
                i.putExtra("postUrl", postUrl);
                startActivity(i);*/
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        progressBar.setVisibility(View.GONE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.favourites:
                new Recycler_View_Adapter(favouriteItems, getApplicationContext()).old_to_new();
                SetupRecyclerView(favouriteItems);
                break;
            case R.id.old_to_new:
                Toast.makeText(this, "Sorted Increasing", Toast.LENGTH_SHORT).show();
                new Recycler_View_Adapter(newsItems, getApplicationContext()).old_to_new();
                SetupRecyclerView(newsItems);
                break;
            case R.id.new_to_old:
                Toast.makeText(this, "Sorted Decreasing", Toast.LENGTH_SHORT).show();
                new Recycler_View_Adapter(newsItems, getApplicationContext()).new_to_old();
                SetupRecyclerView(newsItems);
                break;
            case R.id.all:
                Toast.makeText(this, "No Filter", Toast.LENGTH_SHORT).show();
                SetupRecyclerView(newsItems);
                break;
            case R.id.publisher_filter:
                Toast.makeText(this, "publisher_filter", Toast.LENGTH_SHORT).show();

                final String filtered[] = GetAllPublishers().toArray(new String[GetAllPublishers().size()]);
                filteredList(filtered, "p");

                break;
            case R.id.category_filter:
                Toast.makeText(this, "category_filter", Toast.LENGTH_SHORT).show();
                final String filtered1[] = GetAllCategories().toArray(new String[GetAllCategories().size()]);
                filteredList(filtered1, "c");
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    private void filteredList(final String[] filtered, final String type) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick one");

        final String[] filterFactor = new String[1];
        builder.setSingleChoiceItems(filtered, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                filterFactor[0] = filtered[i];
            }
        });

        builder.setPositiveButton("Filter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                new Recycler_View_Adapter(newsItems, getApplicationContext()).filter(filterFactor[0], type);
                SetupRecyclerView(newsItems);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.setCancelable(true);

        builder.show();
    }

}
