package com.example.booklist;

import android.text.TextUtils;
import android.util.Log;

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
import java.util.List;

public class QuarryUtils {

    private static final String TAG = QuarryUtils.class.getSimpleName();

    private QuarryUtils(){}

    /**
     * in this class we will :
     * send the lists of request books from the given url
     *
     * the operation to do :
     * create a method that will content :
     *  1- create url obj from the String url
     *  2- make http request and receive the json response
     *  3- extract the related fields from the json response
     *  4- return a list of books
     */
    public static List<Book> fetchBookData(String requestUrl){
        URL url = createUrl(requestUrl);

        String JsonResponse = null;
        try {
            JsonResponse = makeHttpRequest(url);
        }catch (IOException e){
            Log.e(TAG, "Request Http failed" );
        }

        List<Book> Books = extractBooksFromJson(JsonResponse);
        return Books;
    }

    /**
     * convert the request url string to url obj
     * @param requestUrl url string
     * @return url obj
     */
    private static URL createUrl(String requestUrl) {
        URL url = null;
        try {
            url = new URL(requestUrl);
        }catch (MalformedURLException e){
            Log.e(TAG, "createUrl: cannot create the url from the requestUrl String");
        }

        return url;
    }

    /**
     * get the json response from the given url
     * @param url given url
     * @return json response
     * @throws IOException when problem from fetching the json happens
     */
    private static String makeHttpRequest(URL url) throws IOException{
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream)throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<Book> extractBooksFromJson(String jsonResponse) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding Books to
        List<Book> Books = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(jsonResponse);

            // Extract the JSONArray associated with the key called "items",
            // which represents a list of books .
            JSONArray bookArray = baseJsonResponse.getJSONArray("items");

            // For each book in the bookArray, create an {@link book} object
            for (int i = 0; i < bookArray.length(); i++) {

                // Get a single book at position i within the list of books
                JSONObject currentBook = bookArray.getJSONObject(i);

                // For a given book, extract the JSONObject associated with the
                // key called "volumeInfo", which represents a list of all properties
                // for that book.
                JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");


                // Extract the value for the key called "title"
                String title = volumeInfo.getString("title");

                JSONArray authorsArray;
                String author = "Author: unknown";
                if(volumeInfo.has("authors")) {
                    // get the Authors Array
                    authorsArray = volumeInfo.getJSONArray("authors");

                    for (int j = 0; j < authorsArray.length(); j++) {

                        if (authorsArray.length() == 1 && j == 0) {
                            author = "Author: " + authorsArray.optString(j);
                        } else {
                            author = author.concat(" & " + authorsArray.optString(j));
                        }
                    }
                }


                String publishedDate = "Date: unavailable";
                if(volumeInfo.has("publishedDate"))
                    // Extract the value for the key called "publishedDate"
                    publishedDate = "Date: "+volumeInfo.getString("publishedDate");

                // Extract the value for the key called "previewLink"
                String previewUrl = volumeInfo.getString("previewLink");

                // Create a new {@link Book} object with the title, author, date,
                // and url from the JSON response.
                Book book = new Book(title, author, publishedDate, previewUrl);

                // Add the new {@link Book} to the list of Books.
                Books.add(book);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the Book JSON results", e);
        }

        // Return the list of Books
        return Books;
    }

}
