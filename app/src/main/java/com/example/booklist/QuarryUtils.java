package com.example.booklist;

import android.util.Log;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
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

        String JsonResponse;
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

    private static String makeHttpRequest(URL url) {
        return null;
    }

    private static List<Book> extractBooksFromJson(String jsonResponse) {
        return null;
    }

}
