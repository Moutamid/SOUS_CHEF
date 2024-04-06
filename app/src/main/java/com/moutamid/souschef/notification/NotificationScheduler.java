package com.moutamid.souschef.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.fxn.stash.Stash;
import com.moutamid.souschef.Constants;

import java.util.Calendar;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class NotificationScheduler {

    public static void scheduleDailyNotification(Context context, Calendar calendar) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Create an intent to trigger the notification
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra(Constants.NOTIFICATION_TYPE, Constants.Notification_Type.DAILY.toString());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                new Random().nextInt(100), intent,//NOTIFICATION_ID
                PendingIntent.FLAG_IMMUTABLE);//FLAG_UPDATE_CURRENT
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        Stash.put(Constants.LAST_TIME, calendar.getTimeInMillis());
    }
}