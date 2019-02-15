package com.wildanokt.searchmoviemade.notification.scheduler;

import android.util.Log;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;
import com.wildanokt.searchmoviemade.R;
import com.wildanokt.searchmoviemade.notification.Receiver;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class SchedulerService extends GcmTaskService {

    final String TAG = SchedulerService.class.getSimpleName();

    static String TAG_MOVIE_LOG = "MovieTask";

    @Override
    public int onRunTask(TaskParams taskParams) {
        int result = 0;
        if (taskParams.getTag().equals(TAG_MOVIE_LOG)){
            runMovieNotification();
            result = GcmNetworkManager.RESULT_SUCCESS;
        }
        return result;
    }

    @Override
    public void onInitializeTasks() {
        super.onInitializeTasks();
        SchedulerTask mSchedulerTask = new SchedulerTask(this);
        mSchedulerTask.createPeriodicTask();
    }

    private void runMovieNotification(){
        Log.d(TAG, "getMovie: running");
        SyncHttpClient client  = new SyncHttpClient();
        String url = com.wildanokt.searchmoviemade.BuildConfig.UPCOMING_MOVIE_URL;
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Receiver receiver = new Receiver();
                String result = new String(responseBody);
                Log.d(TAG, "onSuccess: "+result);
                try {
                    JSONObject responseObject = new JSONObject(result);
                    int arrayLength = responseObject.getJSONArray("results").length();
                    for (int i = 0; i < arrayLength; i++) {
                        String date = responseObject.getJSONArray("results")
                                .getJSONObject(i).getString("release_date");

                        String message = responseObject.getJSONArray("results")
                                .getJSONObject(i).getString("title")+" "+getResources().getString(R.string.message_extension);

                        receiver.setOneTimeNotification(getApplicationContext(), Receiver.TYPE_ONE_TIME, date, message);
                        Log.d(TAG, "Load: date:"+date+" message:"+message);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d(TAG, "onFailure: FAILED"+error.getMessage());
            }
        });
    }
}
