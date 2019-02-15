package com.wildanokt.searchmoviemade.notification;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.wildanokt.searchmoviemade.R;
import com.wildanokt.searchmoviemade.activity.SettingActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Receiver extends BroadcastReceiver{

    Context context;

    //notif id
    private static int notifId     =  0;  //upcoming movie
    private final int ID_REPEATING = -1; //daily

    public static final String TYPE_ONE_TIME  = "OneTime";
    public static final String TYPE_REPEATING = "Repeating";

    public static final String EXTRA_MESSAGE  = "message";
    public static final String EXTRA_TYPE     = "type";
    public static final String EXTRA_TITLE    = "title";
    public static final String EXTRA_ID       = "id";
    public static final String EXTRA_DATE     = "date";

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String type = intent.getStringExtra(EXTRA_TYPE);
        String msg = intent.getStringExtra(EXTRA_MESSAGE);
        String title = type.equalsIgnoreCase(TYPE_ONE_TIME)?
                intent.getStringExtra(EXTRA_TITLE) :
                "Good Morning";
        int notifId = type.equalsIgnoreCase(TYPE_ONE_TIME)?
                intent.getIntExtra(EXTRA_ID, 0) :
                ID_REPEATING;

        String time[] = getCurrentDate().split("-");
        int hour = Integer.parseInt(time[3]);
        int minute = Integer.parseInt(time[4]);

        if (type.equalsIgnoreCase(TYPE_ONE_TIME)){
            if (hour == 8){
                showNotification(context, title, msg, notifId);
                Log.d("ALARM", "onReceive: alarm on id:"+notifId);
            }
        }
        if (type.equalsIgnoreCase(TYPE_REPEATING)){
            if (hour == 7 && minute == 0){
                showNotification(context, title, msg, notifId);
                Log.d("ALARM", "onReceive: repeating alarm showed");
            }
        }
    }

    public void setOneTimeNotification(Context context, String type, String date, String message){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        String[] dateArray = date.split("-");
        String[] dateNow = getCurrentDate().split("-");

        if (dateNow[0].equalsIgnoreCase(dateArray[0])&&
                dateNow[1].equalsIgnoreCase(dateArray[1])&&
                dateNow[2].equalsIgnoreCase(dateArray[2])) {

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, Integer.parseInt(dateArray[0]));
            calendar.set(Calendar.MONTH, Integer.parseInt(dateArray[1])-1);
            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateArray[2]));

            calendar.set(Calendar.HOUR_OF_DAY, 8);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

            Intent intent = new Intent(context, Receiver.class);
            intent.putExtra(EXTRA_MESSAGE, message);
            intent.putExtra(EXTRA_TYPE, type);
            intent.putExtra(EXTRA_ID, this.notifId);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context,
                    this.notifId,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            if (alarmManager != null){
                //set alarm
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
            SettingActivity.setNotifOnce(true);
            this.notifId++;
            Log.i("NOTIFICATION SET", "setOneNotif: success with id:"+notifId);
        }else {
            return;
        }
    }

    public void setRepeatingNotif(Context context, String type, String message){
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, Receiver.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(EXTRA_TYPE, type);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 07);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.DAY_OF_YEAR, 1);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING, intent, 0);
        if (manager!= null){
            manager.setInexactRepeating(manager.RTC_WAKEUP, calendar.getTimeInMillis(), manager.INTERVAL_DAY, pendingIntent);
        }
        SettingActivity.setNotifRepeat(true);
        Log.i("NOTIFICATION SET", "setRepeatingNotif:  success");
    }

    private String getCurrentDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        Date date = Calendar.getInstance().getTime();
        return dateFormat.format(date);
    }

    private void showNotification(Context context, String title, String message, int notifId) {
        String CHANNEL_ID = "Movie_id";
        String CHANNEL_NAME = "Movie Notification";
        String GROUP_KEY = "group_key";
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder;
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_movie_black);
        mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_movie_black)
                .setLargeIcon(largeIcon)
                .setGroup(GROUP_KEY)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setAutoCancel(true);

        //oreo ++
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});
            mBuilder.setChannelId(CHANNEL_ID);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
        Notification notification = mBuilder.build();
        if (manager != null) {
            manager.notify(notifId, notification);
        }
    }

    public void deactivatedNotification(Context context, String type){
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, Receiver.class);
        int requestCode = type.equalsIgnoreCase(TYPE_ONE_TIME) ? intent.getIntExtra(EXTRA_ID, 0) : ID_REPEATING;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
        pendingIntent.cancel();

        if (type.equalsIgnoreCase(TYPE_ONE_TIME)){
            SettingActivity.setNotifOnce(false);
        }else{
            SettingActivity.setNotifRepeat(false);
        }

        if (manager!= null){
            manager.cancel(pendingIntent);
        }
        Log.d("DEACTIVATED", "deactivatedNotification: id: "+requestCode);
    }
}
