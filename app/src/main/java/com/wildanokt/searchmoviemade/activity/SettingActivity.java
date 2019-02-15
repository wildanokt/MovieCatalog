package com.wildanokt.searchmoviemade.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.wildanokt.searchmoviemade.R;
import com.wildanokt.searchmoviemade.notification.Receiver;
import com.wildanokt.searchmoviemade.notification.scheduler.SchedulerTask;

public class SettingActivity extends AppCompatActivity{

    TextView tvEveryday;
    TextView tvUpcomingMovie;
    Switch swEveryday;
    Switch swUpMov;

    private SchedulerTask mSchedulerTask;

    private static boolean notifOnce = true;
    private static boolean notifRepeat = true;

    public static void setNotifOnce(boolean notifOnce) {
        SettingActivity.notifOnce = notifOnce;
    }
    public static void setNotifRepeat(boolean notifRepeat) {
        SettingActivity.notifRepeat = notifRepeat;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //action bar
        getSupportActionBar().setTitle(R.string.setting);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //widget
        tvEveryday = findViewById(R.id.tv_repeat_notif);
        tvUpcomingMovie = findViewById(R.id.tv_one_notif);
        swEveryday = findViewById(R.id.repeating_toggle);
        swUpMov = findViewById(R.id.one_toggle);

        //receiver
        final Receiver receiver = new Receiver();
        receiver.setContext(this);

        //set switch status
        swEveryday.setChecked(notifRepeat);
        swUpMov.setChecked(notifOnce);

        //Scheduler task
        mSchedulerTask = new SchedulerTask(this);

        //daily
        swEveryday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    receiver.setRepeatingNotif(SettingActivity.this, Receiver.TYPE_REPEATING, getString(R.string.Repeat_message));
                    Log.i("ON REPEAT", "onCheckedChanged: toggle active");
                }else {
                    receiver.deactivatedNotification(SettingActivity.this ,Receiver.TYPE_REPEATING);
                    Log.i("OFF REPEAT", "onCheckedChanged: toggle inactive");
                }
            }
        });
        //release movie
        swUpMov.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    mSchedulerTask.createPeriodicTask();
                    Log.i("ON ONCE", "onCheckedChanged: toggle active");
                }else {
                    mSchedulerTask.cancelPeriodicTask();
                    Log.i("OFF ONCE", "onCheckedChanged: toggle inactive");
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}