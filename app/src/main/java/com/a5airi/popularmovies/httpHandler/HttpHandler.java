package com.a5airi.popularmovies.httpHandler;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by khairy on 3/9/2018.
 */

public class HttpHandler {

    private static  final String TAG = HttpHandler.class.getSimpleName();

    public HttpHandler(){

    }

    public String makeServiceCall (String req_Url){
        String response = null ;

        try {
            URL url = new URL(req_Url);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoOutput(true);
            InputStream input = new BufferedInputStream(conn.getInputStream());
            response = convertToString(input);

        }catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return response;
    }

    private String convertToString (InputStream in){
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder builder = new StringBuilder();
        String link ;
        try {
            while ((link=reader.readLine()) != null){
                builder.append(link).append('\n');
            }
        } catch (IOException e) {
                e.printStackTrace();
        } finally {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        return builder.toString();
    }


}
