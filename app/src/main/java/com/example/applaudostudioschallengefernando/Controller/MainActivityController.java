package com.example.applaudostudioschallengefernando.Controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.applaudostudioschallengefernando.Adapters.VerticalRecyclerViewAdapter;
import com.example.applaudostudioschallengefernando.Data.ConfigData;
import com.example.applaudostudioschallengefernando.Data.NetworkValidation;
import com.example.applaudostudioschallengefernando.Data.QueryDB;
import com.example.applaudostudioschallengefernando.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class MainActivityController extends AppCompatActivity {

     //variables for pagination
     private int iCurrentCategoryId = 1;
     private int maxCategories = 4;
     private boolean bIsLoading = false;

     //database variable
     QueryDB dbQrs = new QueryDB(this);

     //adapter and recyclerView related
     private Parcelable recyclerViewState;
     VerticalRecyclerViewAdapter adapter;
     RecyclerView recyclerView;
     ProgressBar pgBar;

     //Validations
    NetworkValidation nwValidator = new NetworkValidation();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            pgBar = findViewById(R.id.progressbar);
            recyclerView = findViewById(R.id.recyclerviewvertical);
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (dy > 0) {
                        if (!recyclerView.canScrollVertically(1)) {
                            pgBar.setVisibility(View.VISIBLE);
                            recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();
                            getInitialInfoFromAPI();
                        }
                    }
                }
            });

            if (nwValidator.isNetworkConnected(this)) {
                InternetValidation InternetOk = new InternetValidation();
                try {
                    if (InternetOk.execute().get()) {
                        dbQrs.open();
                        dbQrs.BeginTransaction();
                        dbQrs.DeleteCategories();
                        dbQrs.DeleteAnime();
                        dbQrs.CommitTransaction();
                        dbQrs.close();
                        getInitialInfoFromAPI();
                    } else {
                        initVerticalRecyclerView();
                        Toast.makeText(this, "Offline mode: not connected to Internet, last sync data will be used", Toast.LENGTH_LONG).show();
                    }

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            } else {
                initVerticalRecyclerView();
                Toast.makeText(this, "Offline mode: not connected to Internet, last sync data will be used", Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initVerticalRecyclerView( ){
        try {
            ArrayList<Integer> mCategoriesId = new ArrayList<>();
            ArrayList<String> mCategoryTitles = new ArrayList<>();
            dbQrs.open();
            Cursor cCategories = dbQrs.GetCategories();
            if (cCategories.moveToFirst()) {
                do {
                    int iCategoryId = cCategories.getInt(0);
                    String sCategoryTitle = cCategories.getString(1);

                    mCategoriesId.add(iCategoryId);
                    mCategoryTitles.add(sCategoryTitle);

                    adapter = new VerticalRecyclerViewAdapter(mCategoryTitles, mCategoriesId, this);
                    recyclerView.setAdapter(adapter);
                } while (cCategories.moveToNext());
            }
            dbQrs.close();
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void getInitialInfoFromAPI(){

        ConfigData cfData = new ConfigData();
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        // Request a string response from kitsu API
        for(int i=1; iCurrentCategoryId<=maxCategories; iCurrentCategoryId++){
            StringRequest stringRequest = new StringRequest(Request.Method.GET, cfData.sUrlGetKitsuCategories(iCurrentCategoryId),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String kitsuResponse) {
                            try {
                                JSONObject fullJsonObject = new JSONObject(kitsuResponse);
                                String stringCategoryData = fullJsonObject.get("data").toString();
                                JSONObject DataJsonObject = new JSONObject(stringCategoryData);

                                //this is inside data
                                int iCategoryId =Integer.parseInt(DataJsonObject.get("id").toString());
                               String stringCategoryAttributes = DataJsonObject.get("attributes").toString();

                                //this is inside attributes
                               JSONObject AttributeJsonObject = new JSONObject(stringCategoryAttributes);
                               String stringCategory = AttributeJsonObject.get("title").toString();
                                //We have the list of all categories, we can put them into the model and add it to a list
                                dbQrs.open();
                                dbQrs.BeginTransaction();
                                dbQrs.InsertCategory(iCategoryId,stringCategory);
                                dbQrs.CommitTransaction();
                                dbQrs.close();
                                getAnimeByCategory(iCategoryId,1,5);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });
            queue.add(stringRequest);
        }
        iCurrentCategoryId = maxCategories + 1;
        maxCategories = maxCategories + 5;
        pgBar.setVisibility(View.GONE);
    }

    private class InternetValidation extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            return nwValidator.isInternetAvailable();
        }

        @Override
        protected void onPostExecute(Boolean SetData) {

        }
    }

    public void getAnimeByCategory(final int iCategoryId, int iCurrentOffSet, int maxOffSet){

        ConfigData cfData = new ConfigData();
        RequestQueue queue = Volley.newRequestQueue(this);

        // Request a string response from kitsu API
            StringRequest stringRequest = new StringRequest(Request.Method.GET, cfData.sUrlGetKitsuAnimesByCategories(iCategoryId,1),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String kitsuResponse) {
                            try {

                                JSONObject fullJsonObject = new JSONObject(kitsuResponse);

                                String stringCategoryData = fullJsonObject.get("data").toString();

                                JSONArray DataJsonArray = new JSONArray(stringCategoryData);

                                int iAnimeId = 0;
                                String sAnimeAttributes ="";

                                for(int i=0; i < DataJsonArray.length(); i++) {
                                    JSONObject jsonobject = DataJsonArray.getJSONObject(i);
                                    iAnimeId  = Integer.parseInt(jsonobject.getString("id"));
                                    sAnimeAttributes = jsonobject.getString("attributes");

                                    JSONObject AttributeJsonObject =  new JSONObject(sAnimeAttributes);
                                    String sSlug = AttributeJsonObject.get("slug").toString();
                                    String sSynopsys = AttributeJsonObject.get("synopsis").toString();
                                    String sCanonTitle = AttributeJsonObject.get("canonicalTitle").toString();
                                    String sAverageRating = AttributeJsonObject.get("averageRating").toString();
                                    String sStartDate = AttributeJsonObject.get("startDate").toString();
                                    String sEndDate = AttributeJsonObject.get("endDate").toString();
                                    String sAgeRating = AttributeJsonObject.get("ageRating").toString();
                                    String sEpisodeCount = AttributeJsonObject.get("episodeCount").toString();
                                    String sEpisodeLength  = AttributeJsonObject.get("episodeLength").toString();
                                    String sYoutubeId  = AttributeJsonObject.get("youtubeVideoId").toString();
                                    String sStatus  = AttributeJsonObject.get("status").toString();
                                    String sType  = AttributeJsonObject.get("subtype").toString();

                                    String sManyTitles = AttributeJsonObject.get("titles").toString();
                                    String sPosterImages = AttributeJsonObject.get("posterImage").toString();


                                    JSONObject PosterImagesJsonObject = new JSONObject(sPosterImages);
                                    String sUrlSmallImage = PosterImagesJsonObject.get("small").toString();
                                    JSONObject tilesJsonObject = new JSONObject(sManyTitles);

                                    dbQrs.open();
                                    dbQrs.BeginTransaction();
                                    dbQrs.InsertAnime(iCategoryId,iAnimeId,sSlug,sUrlSmallImage,sSlug,sType,sEpisodeCount,sStartDate,sEndDate,sCanonTitle,sAverageRating,
                                            sAgeRating,sEpisodeLength,sStatus,sSynopsys, sYoutubeId);
                                    dbQrs.CommitTransaction();
                                    dbQrs.close();
                                }

                                initVerticalRecyclerView();
                                recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });
            queue.add(stringRequest);
    }

}
