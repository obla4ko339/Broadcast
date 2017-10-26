package com.example.grabvn.broadcast;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.Looper;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

public class MyService extends Service {

    ParseTask parsetask;
    private Handler mainLoopHandler;
    private Runnable queryServerRunnable;
    Timer timer = new Timer();

    public MyService() {
    }

    final String LOG_TAG = "myLogs";

    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "onCreate");

    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "onStartCommand");
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                someTask();
            }
        };

        timer.schedule(timerTask, 1000, 5000);

        return super.onStartCommand(intent, flags, startId);
    }

    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
        Log.d(LOG_TAG, "onDestroy");
    }

    public IBinder onBind(Intent intent) {
        Log.d(LOG_TAG, "onBind");
        return null;
    }

    void someTask() {



        parsetask = new ParseTask();
        parsetask.execute();

    }


    private class ParseTask extends AsyncTask<Void, Void, String> {

        HttpURLConnection urlConnection;
        BufferedReader reader = null;
        String resultJson = "";
        URL url;
        JSONObject jsonObjectnew = null;
        JSONObject jsonObject = null;
        String name = "";
        JSONArray jsonArraynew = null;
        String srcImg = "";
        JSONObject frd;




        @Override
        protected String doInBackground(Void... params) {
            try {
                url = new URL("http://kultura-to.ru/mjson.php?datepost=2017-11-01");
                Log.d("URL", ""+url);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer stringBuffer = new StringBuffer();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                Log.d("reader",""+reader);
                String line;
                while ((line = reader.readLine()) != null){
                    stringBuffer.append(line);
                }
                resultJson = stringBuffer.toString();
                reader.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                jsonObjectnew = new JSONObject(resultJson);
                jsonArraynew = jsonObjectnew.getJSONArray("event");
                for(int i =0; i<jsonArraynew.length(); i++){
                    frd = jsonArraynew.getJSONObject(i);

                    srcImg = "http://kultura-to.ru/" + frd.getString("image");
                    //DataCalendarEvent dataCalendarEvent = new DataCalendarEvent(frd.getString("place"),frd.getString("title"),frd.getString("type"),frd.getString("startdatenotime"), frd.getString("id"), srcImg );
                    //dataCalendarEventsList.add(dataCalendarEvent);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return resultJson;
        }

        @Override
        protected void onPostExecute(String resultJson) {
            super.onPostExecute(resultJson);
            Log.d("DATAJSON",""+resultJson.length());


        }
    }

}
