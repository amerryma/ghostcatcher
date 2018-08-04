package com.wurbo.ghostcatcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;

public class HttpHelper {
    @TargetApi(9)
    public static boolean validateLogin(String user, String pass)
            throws IOException {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        StringBuilder urlStr = new StringBuilder();
        urlStr.append("http://www.wurbo.com/mysql/login.php").append("?user=")
                .append(user).append("&pass=").append(pass)
                .append("&key=e674be169f87bd39a6efcc70c600de8b41574a5e")
                .append("&noCache=").append(new Date().getTime());

        return executeAndWait(urlStr.toString()).contains("equal!");
    }

    @TargetApi(9)
    public static int getHighscore(String user) throws IOException {
        int highscore = 0;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        StringBuilder urlStr = new StringBuilder();
        urlStr.append("http://www.wurbo.com/mysql/highscores.php")
                .append("?hname=").append(user).append("&action=load")
                .append("&key=e674be169f87bd39a6efcc70c600de8b41574a5e")
                .append("&game=ghostcatcher");

        String response = executeAndWait(urlStr.toString());

        boolean found = false;
        String[] splitStr = response.split("<br/>");
        int i = 0;
        while (!found && i < splitStr.length) {
            String str = splitStr[i];
            i++;
            if (str.contains("highscore")) {
                try {
                    highscore = Integer.parseInt(str.split(":")[1].trim());
                    found = true;
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }

        return highscore;
    }

    @TargetApi(9)
    public static boolean setHighscore(String user, int score) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        StringBuilder urlStr = new StringBuilder();
        urlStr.append("http://www.wurbo.com/mysql/highscores.php")
                .append("?hname=").append(user).append("&score=").append(score)
                .append("&action=save")
                .append("&key=e674be169f87bd39a6efcc70c600de8b41574a5e")
                .append("&game=ghostcatcher");

        execute(urlStr.toString());

        return true;
    }

    @TargetApi(9)
    public static List<String> getHighscores() throws IOException {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        StringBuilder urlStr = new StringBuilder();
        urlStr.append("http://www.wurbo.com/mysql/highscores.php")
                .append("?action=loadAll")
                .append("&key=e674be169f87bd39a6efcc70c600de8b41574a5e")
                .append("&game=ghostcatcher");

        String response = executeAndWait(urlStr.toString());
        List<String> highscores = new ArrayList<String>();
        for (String str : response.split("<br/>")) {
            if (str.contains("username")) {
                highscores.add(str.split(":")[1]);
            }
        }

        return highscores;
    }

    private static String executeAndWait(String urlStr) {
        String response = "";

        AsyncTask<String, Integer, String> task = new UrlAsynTask()
                .execute(urlStr);

        try {
            response = task.get(5000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TimeoutException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return response;
    }

    private static void execute(String urlStr) {
        AsyncTask<String, Integer, String> task = new UrlAsynTask()
                .execute(urlStr);
    }
}

class UrlAsynTask extends AsyncTask<String, Integer, String> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String result) {
        // set the results in Ui

    }

    @Override
    protected String doInBackground(String... arg) {
        StringBuilder data = new StringBuilder();

        try {
            URL url = new URL(arg[0]);
            HttpGet httpRequest = null;

            httpRequest = new HttpGet(url.toURI());

            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient
                    .execute(httpRequest);

            HttpEntity entity = response.getEntity();
            BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(entity);
            InputStream input = bufHttpEntity.getContent();
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                data.append(inputLine);
            }
            in.close();

        } catch (MalformedURLException e) {
            // Log.e("ghosthunter", "Bad Url", e);
        } catch (Exception e) {
            // Log.e("ghosthunter", "IO Error", e);
        }
        return data.toString();
    }
}