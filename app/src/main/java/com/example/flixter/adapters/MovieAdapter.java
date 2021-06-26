package com.example.flixter.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flixter.MovieDetailsActivity;
import com.example.flixter.R;
import com.example.flixter.models.Movie;

import org.jetbrains.annotations.NotNull;
import org.parceler.Parcels;

import java.net.URL;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    Context context;
    List<Movie> movies;
    public static final String URL_KEY = "url key";

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    //usually involves inflating a layout from XML and returning the holder
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View movieView = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(movieView);
    }

    //involves populating data into the item through holder
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        //get movie at position
        Movie movie = movies.get(position);
        //bind movie data into ViewHolder
        holder.bind(movie);
    }

    //returns count of items
    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;
        ImageView ivDetailPoster;
        String imageUrl;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            //ivDetailPoster = itemView.findViewById(R.id.ivDetailPoster);
            itemView.setOnClickListener(this);
        }

        public void bind(Movie movie) {
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());
            Glide.with(context).load("https://courses.codepath.com/courses/android_university_fast_track/unit/2").transform(new RoundedCornersTransformation(200, 10)).
            placeholder(R.drawable.flicks_movie_placeholder).override(200,300).into(ivPoster);


            //if in landscape
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                imageUrl = movie.getBackdropPath();
            } else {
                imageUrl = movie.getPosterPath();
            }

//            String imageUrlDetail;
//            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//                imageUrlDetail = movie.getBackdropPath();
//                Log.d("Detail Image", imageUrlDetail);
//            } else {
//                imageUrlDetail = movie.getPosterPath();
//                Log.d("Detail Image", imageUrlDetail);
//            }

            Glide.with(context).load(imageUrl).transform(new RoundedCornersTransformation(200,10)).centerCrop().fitCenter().into(ivPoster); //add .transform(new RoundedCornersTransformation(200,10))
            //Glide.with(context).load(imageUrl).into(ivPoster);

        }

        @Override
        public void onClick(View v) {
//            Get the position & ensure it’s valid
            int position = getAdapterPosition();
//              make sure position is valid
            if (position != RecyclerView.NO_POSITION) {
                //            Get the Movie at that position in the list
                Movie movie = movies.get(position);
                //            Create an Intent to display MovieDetailsActivity
                Intent intent = new Intent(context, MovieDetailsActivity.class);
                //            Pass the movie as an extra serialized via Parcels.wrap()
                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
                intent.putExtra(URL_KEY, imageUrl);
                //            Show the activity
                context.startActivity(intent);
            }

        }
    }
}
