package com.example.android.popularmoviesstageone.utilities;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.example.android.popularmoviesstageone.models.MovieParcel;
import com.example.android.popularmoviesstageone.models.ReviewParcel;
import com.example.android.popularmoviesstageone.models.VideoParcel;

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
import java.nio.charset.Charset;
import java.util.ArrayList;

public final class NetworkUtils {

    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();

    private static final String MOVIE_DB_BASE_URL = "http://api.themoviedb.org/3/movie?";

    // Insert your API key here
    private static final String API_KEY = "d7fce3918c27210d3297d5c25e13d32d";
    private static final String API_PARAM = "api_key";

    private static final String VIDEO_PATH = "videos";
    private static final String REVIEW_PATH = "reviews";

    public static ArrayList<MovieParcel> fetchMovieData(String sortOrder) {

        // Build the url
        Uri builtUri = Uri.parse(MOVIE_DB_BASE_URL).buildUpon()
                .appendPath(sortOrder)
                .appendQueryParameter(API_PARAM, API_KEY)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(LOG_TAG, "Built Movie List URL " + url);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Get an ArrayList of the type MovieParcel from the jsonResponse
        return extractFeatureFromJson(jsonResponse);
    }

    public static ArrayList<VideoParcel> fetchVideoData(String movieId) {
        // Build the url
        Uri builtUri = Uri.parse(MOVIE_DB_BASE_URL).buildUpon()
                .appendPath(movieId)
                .appendPath(VIDEO_PATH)
                .appendQueryParameter(API_PARAM, API_KEY)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(LOG_TAG, "Built Videos URL " + url);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Get an ArrayList of the type VideoParcel from the jsonResponse
        return extractVideoFeatureFromJson(jsonResponse);
    }

    public static ArrayList<ReviewParcel> fetchReviewData(String movieId) {
        // Build the url
        Uri builtUri = Uri.parse(MOVIE_DB_BASE_URL).buildUpon()
                .appendPath(movieId)
                .appendPath(REVIEW_PATH)
                .appendQueryParameter(API_PARAM, API_KEY)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(LOG_TAG, "Built Reviews URL " + url);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Get an ArrayList of the type ReviewParcel from the jsonResponse
        return extractReviewFeatureFromJson(jsonResponse);
    }

    private static String makeHttpRequest(URL url) throws IOException {

        String jsonResponse = "";

        //If the url is null, then return early
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputstream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /*milliseconds*/);
            urlConnection.setConnectTimeout(15000 /*milliseconds*/);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

             /*If the request was successful (response code 200),
             then read the input stream and parse the response.*/
            if (urlConnection.getResponseCode() == 200) {
                inputstream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputstream);
            } else {
                Log.e(LOG_TAG, "Error response code" + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the MoviesDB JSON results.", e);
        } finally {
            if (urlConnection == null) {
                urlConnection.disconnect();
            }

            if (inputstream == null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies that an IOException
                // could be thrown.
                inputstream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the InputStream into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputstream) throws IOException {

        StringBuilder stringArray = new StringBuilder();

        InputStreamReader inputStreamReader = new InputStreamReader(inputstream, Charset.forName("UTF-8"));
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line = bufferedReader.readLine();
        while (line != null) {
            stringArray.append(line);
            line = bufferedReader.readLine();
        }
        return stringArray.toString();
    }

    private static ArrayList<MovieParcel> extractFeatureFromJson(String jsonResponse) {

        //If the json string is empty or null, then return early
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding movies to
        ArrayList<MovieParcel> movieArrayList = new ArrayList<>();

        try {
            //Create a JSONObject from the jsonResponse string
            JSONObject jsonRootObject = new JSONObject(jsonResponse);

            //Extract the JSON array associated with the key "results"
            JSONArray itemsArray = jsonRootObject.getJSONArray("results");

            String title = "";
            String overview = "";
            String releaseDate = "";
            String votes = "";
            String poster = "";
            String backdrop = "";
            String movieId = "";

            for (int i = 0; i < itemsArray.length(); i++) {

                JSONObject singleMovieObject = itemsArray.getJSONObject(i);

                title = singleMovieObject.getString("title");
                overview = singleMovieObject.getString("overview");
                releaseDate = singleMovieObject.getString("release_date");
                votes = singleMovieObject.getString("vote_average");
                poster = singleMovieObject.getString("poster_path");
                backdrop = singleMovieObject.getString("backdrop_path");
                movieId = singleMovieObject.getString("id");

                // Add the details to a new Movie Array List
                movieArrayList.add(new MovieParcel(title, overview, releaseDate, votes, poster, backdrop, movieId));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the MoviesDB JSON results", e);
        }
        return movieArrayList;
    }

    private static ArrayList<VideoParcel> extractVideoFeatureFromJson(String jsonResponse) {

        //If the json string is empty or null, then return early
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding videos to
        ArrayList<VideoParcel> videoArrayList = new ArrayList<>();

        try {
            //Create a JSONObject from the jsonResponse string
            JSONObject jsonRootObject = new JSONObject(jsonResponse);

            //Extract the JSON array associated with the key "results"
            JSONArray itemsArray = jsonRootObject.getJSONArray("results");

            String id = "";
            String name = "";
            String key = "";
            String site = "";

            for (int i = 0; i < itemsArray.length(); i++) {

                JSONObject singleVideoObject = itemsArray.getJSONObject(i);

                id = singleVideoObject.getString("id");
                name = singleVideoObject.getString("name");
                key = singleVideoObject.getString("key");
                site = singleVideoObject.getString("site");

                // Add the details to a new Video Array List
                videoArrayList.add(new VideoParcel(id, name, key, site));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the MoviesDB JSON results", e);
        }
        return videoArrayList;
    }

    private static ArrayList<ReviewParcel> extractReviewFeatureFromJson(String jsonResponse) {

        //If the json string is empty or null, then return early
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding videos to
        ArrayList<ReviewParcel> reviewArrayList = new ArrayList<>();

        try {
            //Create a JSONObject from the jsonResponse string
            JSONObject jsonRootObject = new JSONObject(jsonResponse);

            //Extract the JSON array associated with the key "results"
            JSONArray itemsArray = jsonRootObject.getJSONArray("results");

            String id = "";
            String author = "";
            String content = "";
            String url = "";

            for (int i = 0; i < itemsArray.length(); i++) {

                JSONObject singleReviewObject = itemsArray.getJSONObject(i);

                id = singleReviewObject.getString("id");
                author = singleReviewObject.getString("author");
                content = singleReviewObject.getString("content");
                url = singleReviewObject.getString("url");

                // Add the details to a new Review Array List
                reviewArrayList.add(new ReviewParcel(id, author, content, url));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the MoviesDB JSON results", e);
        }
        return reviewArrayList;
    }
}
