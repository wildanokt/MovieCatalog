package com.wildanokt.searchmoviemade.notification.scheduler;

import android.content.Context;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.gcm.Task;
import com.wildanokt.searchmoviemade.activity.SettingActivity;

public class SchedulerTask {
    private GcmNetworkManager mGcmNetworkManager;

    public SchedulerTask(Context context) {
        mGcmNetworkManager = GcmNetworkManager.getInstance(context);
    }

    public void createPeriodicTask(){
        Task periodicTask = new PeriodicTask.Builder()
                .setService(SchedulerService.class)
                .setPeriod(20)
                .setFlex(10)
                .setTag(SchedulerService.TAG_MOVIE_LOG)
                .setPersisted(true)
                .build();

        mGcmNetworkManager.schedule(periodicTask);
        SettingActivity.setNotifOnce(true);
    }

    public void cancelPeriodicTask(){
        if (mGcmNetworkManager!= null){
            mGcmNetworkManager.cancelTask(SchedulerService.TAG_MOVIE_LOG, SchedulerService.class);
            SettingActivity.setNotifOnce(false);
        }
    }
}
