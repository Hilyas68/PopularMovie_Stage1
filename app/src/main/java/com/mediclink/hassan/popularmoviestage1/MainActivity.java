package com.mediclink.hassan.popularmoviestage1;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mediclink.hassan.popularmoviestage1.Util.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviesItemClickListener{

    private RecyclerView recyclerView;
    private TextView errorText;
    private ProgressBar progressBar;
    private MoviesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_movie);
        errorText = (TextView) findViewById(R.id.movie_error_message_display);
        progressBar = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        //Displaying Grid item using GridLayoutManager
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);

        //adapting the data to the recyclerview
        adapter = new MoviesAdapter(this,this);
        recyclerView.setAdapter(adapter);

        //Check for internet state
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager.getActiveNetworkInfo() != null && manager.getActiveNetworkInfo().isConnected()){
            showView();
            new getJsonData().execute("popular");
        }else {

            onErr();
        }
    }

    private void onErr() {
        recyclerView.setVisibility(View.INVISIBLE);
        errorText.setVisibility(View.VISIBLE);
    }

    public void showView() {
         recyclerView.setVisibility(View.VISIBLE);
        errorText.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onMoviesItemClick(int position, JSONArray array) {
        if(array != null) {
            try {
                JSONObject object = array.getJSONObject(position);

                String title = object.getString("title");
                String overview = object.getString("overview");
                String rating = object.getString("vote_average");
                String date = object.getString("release_date");
                String poster = "http://image.tmdb.org/t/p/w185" + object.getString("poster_path");

                Intent intent = new Intent(this, MoviesDetailActivity.class);
                intent.putExtra("Title", title);
                intent.putExtra("Overview", overview);
                intent.putExtra("Rating", rating);
                intent.putExtra("Date", date);
                intent.putExtra("Poster", poster);

                startActivity(intent);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sortby,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = R.id.popular;
        int id1 = R.id.rated;

        if (item.getItemId() == id){
            item.setChecked(true);
            ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if (manager.getActiveNetworkInfo() != null && manager.getActiveNetworkInfo().isConnected()) {
                recyclerView.setVisibility(View.INVISIBLE);
                new getJsonData().execute("popular");

                return true;
            }else{
                onErr();
            }
        }

        if (item.getItemId() == id1){
            recyclerView.setVisibility(View.INVISIBLE);
            item.setChecked(true);
            ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if (manager.getActiveNetworkInfo() != null && manager.getActiveNetworkInfo().isConnected()) {
                new getJsonData().execute("top rated");

                return true;
            }else{ onErr(); }
        }
        return false;
    }


    public class getJsonData extends AsyncTask<String, Void, JSONObject> {

    @Override
    protected void onPreExecute() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected JSONObject doInBackground(String... params) {

        String param = params[0];
        String s = null;
        JSONObject jsonObject = null;
        URL url = NetworkUtils.buildUrl(param);
        try {
            s = NetworkUtils.getResponseFromHttpUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            jsonObject = new JSONObject(s);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    protected void onPostExecute(JSONObject object) {
        progressBar.setVisibility(View.INVISIBLE);
        if (object != null){
            showView();
            JSONArray jsonArray = null;

            try {

                jsonArray = object.getJSONArray("results");

            } catch (JSONException e) {
                e.printStackTrace();
            }


            adapter.setData(jsonArray);
        }else {

            onErr();
        }
    }
}
}
