package com.example.myappportfolio.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by sagar on 20-03-2016.
 */
public class AndroidMovieAdaptor extends ArrayAdapter {

    private Context context;
    private LayoutInflater inflater;
    private String[] movieList;
    public AndroidMovieAdaptor(Context context, String[] movieList) {
        super(context,0, movieList);
        this.context = context;
        this.movieList = movieList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Gets the AndroidFlavor object from the ArrayAdapter at the appropriate position
        //AndroidFlavor androidFlavor = getItem(position);

        // Adapters recycle views to AdapterViews.
        // If this is a new View object we're getting, then inflate the layout.
        // If not, this view already has the layout inflated from a previous call to getView,
        // and we modify the View widgets as usual.
        if (null == convertView) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.movie_list, parent, false);
        }

        Picasso
                .with(context)
                .load(movieList[position])
                .fit() // will explain later
                .into((ImageView) convertView.findViewById(R.id.movie_image));

        return convertView;
    }
}
