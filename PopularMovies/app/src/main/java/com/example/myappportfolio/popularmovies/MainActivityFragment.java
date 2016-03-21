package com.example.myappportfolio.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    String[] resultStrs = null;
    AndroidMovieAdaptor androidMovieAdaptor = null;

    GridView grdView = null;
    Context context = null;
    final String preUrl = "http://image.tmdb.org/t/p/w185/";
    JSONArray movieInfoArrat = new JSONArray();
    ArrayList<String> movieInfoList = new ArrayList<String>();
    public MainActivityFragment() {

    }
     @Override
    public void onActivityCreated(Bundle savedInstanceState) {
         super.onActivityCreated(savedInstanceState);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            FetchMovieTesk weatherTask = new FetchMovieTesk();
            weatherTask.execute();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onStart(){
        super.onStart();
        FetchMovieTesk weatherTask = new FetchMovieTesk();
        weatherTask.execute();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //weatherTask.execute();
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        grdView = (GridView) rootView.findViewById(R.id.movie_list_grid);
        context = getActivity();
        grdView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                try{
                    ArrayList<String> movieDetailList = (ArrayList<String>)movieInfoArrat.get(position);
                    Intent intent = new Intent(getActivity(),DetailActivity.class).putStringArrayListExtra("movieData",movieDetailList);
                    startActivity(intent);
                }catch (Exception e){
                    Log.e("MyActivity","Error while creating an intent");
                }


            }
        });
        return rootView;
    }
    public class FetchMovieTesk extends AsyncTask<String, Void, String[]> {
        private final String LOG_TAG = FetchMovieTesk.class.getSimpleName();

        private String[] getMoviesData(String strMovieList) throws JSONException{
            JSONObject main_obj = new JSONObject(strMovieList);
            if(main_obj != null && main_obj.has("results")){
                JSONArray movielistAray = main_obj.getJSONArray("results");
                resultStrs = new String[movielistAray.length()];
                for(int i=0;i<movielistAray.length();i++){
                    JSONObject tempObj = movielistAray.getJSONObject(i);
                    String strTemp = tempObj.getString("poster_path");
                    //strTemp = strTemp.substring(1);
                    strTemp = preUrl+""+strTemp;
                    resultStrs[i] = strTemp;
                    movieInfoList = new ArrayList<String>();
                    movieInfoList.add(tempObj.getString("original_title"));
                    movieInfoList.add(preUrl+""+tempObj.getString("backdrop_path"));
                    movieInfoList.add(tempObj.getString("overview"));
                    movieInfoList.add(String.valueOf(tempObj.getDouble("vote_average")));
                    movieInfoList.add(tempObj.getString("release_date"));
                    movieInfoArrat.put(movieInfoList);
                }

            }
            return resultStrs;
        }

        @Override
        protected String[] doInBackground(String... params) {
            // If there's no zip code, there's nothing to look up.  Verify size of params.
            /*if (params.length == 0) {
                return null;
            }*/
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String movieListJsonStr = null;
            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                SharedPreferences shref = PreferenceManager.getDefaultSharedPreferences(getActivity());

                String key = shref.getString(getString(R.string.PREF_LIST),"popular");
                String FORECAST_BASE_URL =
                        "http://api.themoviedb.org/3/movie/"+key;
                final String APPID_PARAM = "api_key";

                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendQueryParameter(APPID_PARAM, "")
                        .build();

                URL url = null;
                try {
                    url = new URL(builtUri.toString());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                movieListJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getMoviesData(movieListJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(String[] result) {
            if (result != null) {
                androidMovieAdaptor = new AndroidMovieAdaptor(context, resultStrs);
                grdView.setAdapter(androidMovieAdaptor);
            }
        }

    }

}
