package com.moutamid.souschef.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.fxn.stash.Stash;
import com.moutamid.souschef.Constants;
import com.moutamid.souschef.MainActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class NotificationReceiver extends BroadcastReceiver {

    NotificationHelper notificationHelper;
    Context context;

    @Override
    public void onReceive(Context context1, Intent intent) {
        notificationHelper = new NotificationHelper(context1);
        context = context1;

        String type = intent.getStringExtra(Constants.NOTIFICATION_TYPE);

        if (type.equals(Constants.Notification_Type.DAILY.toString())) {
            sendDailyNotification();
        } else if (type.equals(Constants.Notification_Type.REVIEW.toString())) {
            // notificationHelper.sendReviewNotification();
        }

    }

    private void sendDailyNotification() {
        String title = "";
        String description = "";

        notificationHelper.sendHighPriorityNotification(title, description, MainActivity.class);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Stash.getLong(Constants.LAST_TIME, System.currentTimeMillis()));
        Random r = new Random();
        int low = 20;
        int high = 27;
        int result = r.nextInt(high - low) + low;
        calendar.add(Calendar.HOUR_OF_DAY, result);

        NotificationScheduler.scheduleDailyNotification(context, calendar);
    }
}