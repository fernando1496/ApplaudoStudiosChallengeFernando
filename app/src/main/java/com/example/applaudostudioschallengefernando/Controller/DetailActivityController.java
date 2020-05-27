package com.example.applaudostudioschallengefernando.Controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.applaudostudioschallengefernando.Data.QueryDB;
import com.example.applaudostudioschallengefernando.R;



public class DetailActivityController extends AppCompatActivity {

    ImageView igImageUrl;
    TextView txtMainTitle, txtCanonTitle, txtType, txtYear, txtGenres, txtAverageR, txtAgeR, txtEpisodeD,txtAiringStatus,txtSynopsys;
    Button btnYoutube;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        try {
            igImageUrl = findViewById(R.id.igImageUrl);
            txtMainTitle = findViewById(R.id.txtMainTitle);
            txtCanonTitle = findViewById(R.id.txtCanonTitle);
            txtType = findViewById(R.id.txtType);
            txtYear = findViewById(R.id.txtYear);
            txtGenres = findViewById(R.id.txtGenres);
            txtAverageR = findViewById(R.id.txtAverageR);
            txtAgeR = findViewById(R.id.txtAgeR);
            txtEpisodeD = findViewById(R.id.txtEpisodeD);
            txtAiringStatus = findViewById(R.id.txtAiringStatus);
            txtSynopsys = findViewById(R.id.txtSynopsys);
            btnYoutube = findViewById(R.id.btnYoutube);

            QueryDB dbQrs = new QueryDB(this);

            Bundle bundle = getIntent().getExtras();
            int AnimeId = bundle.getInt("AnimeId");

            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar.setContentInsetStartWithNavigation(0);

            dbQrs.open();

            Cursor cAnimeCursor = dbQrs.GetAnimeById(AnimeId);

            if(cAnimeCursor.moveToFirst()) {

                int sCategoryId = cAnimeCursor.getInt(0);
                String sImageUrl = cAnimeCursor.getString(3);
                String sAnimeSlug = cAnimeCursor.getString(4);
                String sType = cAnimeCursor.getString(5);
                String sNumberE = cAnimeCursor.getString(6);
                String sStartYear = cAnimeCursor.getString(7);
                String sEndYear = cAnimeCursor.getString(8);
                String sCanonTitle = cAnimeCursor.getString(9);
                String sAverageRating = cAnimeCursor.getString(10);
                String sAgeRating = cAnimeCursor.getString(11);
                String sEpisodeDuration = cAnimeCursor.getString(12);
                String sAiringStatus = cAnimeCursor.getString(13);
                String sSynopsys = cAnimeCursor.getString(14);
                final String sYouTubeId = cAnimeCursor.getString(15);

                Cursor cCategoryCursor = dbQrs.GetCategoryById(sCategoryId);

                String sGenre = "";
                if (cCategoryCursor.moveToFirst()) {
                    sGenre = cCategoryCursor.getString(1);
                }

                Glide.with(this).asBitmap().load(sImageUrl).into(igImageUrl);
                txtMainTitle.setText(sAnimeSlug);
                txtCanonTitle.setText(sCanonTitle);
                txtYear.setText(sStartYear + " till " + sEndYear);
                txtType.setText(sType + ", " + sNumberE + " Episodes");
                txtAverageR.setText(sAverageRating);
                txtAgeR.setText(sAgeRating);
                txtEpisodeD.setText(sEpisodeDuration);
                txtAiringStatus.setText(sAiringStatus);
                txtSynopsys.setText(sSynopsys);
                txtGenres.setText(sGenre);

                if (sYouTubeId.length() > 0) {
                    if (!sYouTubeId.equals(null)) {
                        btnYoutube.setVisibility(View.VISIBLE);
                            btnYoutube.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + sYouTubeId + "")));
                            }
                        });
                    }
                } else {
                    Toast.makeText(this, "Trailer not available", Toast.LENGTH_SHORT).show();
                }
            }
            dbQrs.close();

        }catch (Exception e){
            e.printStackTrace();
        }finally {

        }

    }
}
