package com.example.myappportfolio.popularmovies;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    ArrayList<String> movieInfoList = null;
    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View detailView = inflater.inflate(R.layout.fragment_detail, container, false);
        Intent intent = getActivity().getIntent();
        //ImageView gr = (ImageView)detailView.findViewById(R.id.movie_detail);
        if(intent != null){
             movieInfoList = intent.getStringArrayListExtra("movieData");
        }
       // String[] movieDetailArray = {"AA","","AA","AA","AAA"};
        TextView titleView = (TextView) detailView.findViewById(R.id.title);
        titleView.setText(movieInfoList.get(0));
        TextView descriptionView = (TextView) detailView.findViewById(R.id.description);
        descriptionView.setText(movieInfoList.get(2));
        TextView releaseDateView = (TextView) detailView.findViewById(R.id.releaseDate);
        releaseDateView.setText(movieInfoList.get(4));
        TextView ratingView = (TextView) detailView.findViewById(R.id.rating);
        ratingView.setText(movieInfoList.get(3));

        Picasso
                .with(getActivity())
                .load(movieInfoList.get(1))
                .fit() // will explain later
                .into((ImageView) detailView.findViewById(R.id.thumbline));
        //gr.setAdapter(androidMovieDetailAdaptor);
        return detailView;
    }
}
