package com.example.flixter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixter.databinding.ActivityMovieDetailsBinding;
import com.example.flixter.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Headers;

import static com.example.flixter.adapters.MovieAdapter.URL_KEY;

public class MovieDetailsActivity extends AppCompatActivity {
    Movie movie;
    // the view objects
    TextView tvTitle;
    TextView tvOverview;
    RatingBar rbVoteAverage;
    String urlPath;
    ImageView ivDetailPoster;
    String ids;
    String movie_id;
    public static String MOVIE_ID_URL;
    public static final String TAG = "MovieDetailsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMovieDetailsBinding binding = ActivityMovieDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        tvTitle = binding.tvTitle;
        tvOverview = binding.tvOverview;
        rbVoteAverage = binding.rbVoteAverage;
        ivDetailPoster = binding.ivDetailPoster;

        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        urlPath = (String) getIntent().getStringExtra(URL_KEY);
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));

        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            urlPath = movie.getBackdropPath();
        } else {
            urlPath = movie.getPosterPath();
        }
        Glide.with(this).load(urlPath).transform(new RoundedCornersTransformation(200, 10)).into(ivDetailPoster);

        float voteAverage = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAverage / 2.0f);



        int movieID = movie.getId();
        MOVIE_ID_URL = String.format("https://api.themoviedb.org/3/movie/%s/videos?api_key=" + getString(R.string.YOUTUBE_API_KEY), movieID);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(MOVIE_ID_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    Log.i(TAG, "Results " + results.toString());
                    ids = results.getString(0);
                    JSONObject object = new JSONObject(ids);
                    movie_id = object.getString("key");
                    Log.i(TAG, "Video IDs " + ids.toString());
                } catch (JSONException e) {
                    Log.e(TAG, "hit json exception", e);
                }
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.e(TAG, "onFailure", throwable);
            }
        });


        ivDetailPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MovieDetailsActivity.this, MovieTrailerActivity.class);
                intent.putExtra("videoID", movie_id);
                Log.i("VIDEO ID", movie_id);
                startActivity(intent);
            }
        });
    }
}