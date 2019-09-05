package com.example.booklist;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
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
     * @param url
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
        return null;
    }

}
