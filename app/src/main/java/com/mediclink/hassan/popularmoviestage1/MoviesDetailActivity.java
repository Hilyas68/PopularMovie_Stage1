package com.mediclink.hassan.popularmoviestage1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MoviesDetailActivity extends AppCompatActivity {

    private ImageView poster;
    private TextView title, plot, rating, date;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_detail);

        //Navigating action bar
        ActionBar actionBar = this.getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        poster = (ImageView) findViewById(R.id.movieposter);
        title = (TextView) findViewById(R.id.title1);
        rating = (TextView) findViewById(R.id.rating);
        plot = (TextView) findViewById(R.id.plot);
        date = (TextView) findViewById(R.id.date);

        Intent intent = getIntent();
        if(intent.hasExtra("Title") || intent.hasExtra("Overview") || intent.hasExtra("Rating") || intent.hasExtra("Date") || intent.hasExtra("Poster")){

            String title1 = intent.getStringExtra("Title");
            String overview1 = intent.getStringExtra("Overview");
            String rating1 = intent.getStringExtra("Rating");
            String date1 = intent.getStringExtra("Date");
            String poster1 = intent.getStringExtra("Poster");

            Picasso.with(this).load(poster1).into(poster);
            title.setText(title1);
            plot.setText(overview1);
            rating.setText(rating1+" / 10");
            date.setText(date1);
        }


    }
}
