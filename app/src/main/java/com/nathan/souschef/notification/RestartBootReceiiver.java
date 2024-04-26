package com.nathan.souschef.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.fxn.stash.Stash;
import com.nathan.souschef.Constants;

import java.util.Calendar;

public class RestartBootReceiiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            if (Stash.getBoolean(Constants.NOTIFICATIONS, true)) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(Stash.getLong(Constants.LAST_TIME, System.currentTimeMillis()));
                NotificationScheduler.scheduleDailyNotification(context, calendar, false);
                Calendar calendarDaily = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                NotificationScheduler.scheduleDailyNotification(context, calendarDaily, true);
            }
        }
    }
}
