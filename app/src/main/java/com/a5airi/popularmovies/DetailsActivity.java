package com.a5airi.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.a5airi.popularmovies.model.JsonUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DetailsActivity extends AppCompatActivity {


    public static final String MOVIE_EXTRA = "movieDetails";
    ImageView cover_imageview , intro_imageview;
    TextView title , release_date , summary , user_rating ;
    JsonUtils jsondata;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        cover_imageview = (ImageView) findViewById(R.id.cover_imageView);
        intro_imageview = (ImageView) findViewById(R.id.intro_imageView);
        title = (TextView) findViewById(R.id.title_view);
        release_date = (TextView) findViewById(R.id.release_date_view);
        summary = (TextView) findViewById(R.id.sammary_view);
        user_rating = (TextView) findViewById(R.id.user_rating_view);

        Intent i = getIntent();
        jsondata  = (JsonUtils) i.getSerializableExtra(MOVIE_EXTRA);
        setDetailesView();

    }

    private void setDetailesView(){




        Picasso.with(this)
                .load(jsondata.getCover_image())
                .into(cover_imageview);

        Picasso.with(this)
                .load(jsondata.getIntro_image())
                .into(intro_imageview);

        title.setText(jsondata.getTitle());
        release_date.setText(jsondata.getrelease_date());
        user_rating.setText(jsondata.getvote_average());
        summary.setText(jsondata.getSummary());
    }
}
