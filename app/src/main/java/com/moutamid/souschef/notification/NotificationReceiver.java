package com.moutamid.souschef.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.fxn.stash.Stash;
import com.moutamid.souschef.Constants;
import com.moutamid.souschef.MainActivity;
import com.moutamid.souschef.models.WeekMeal;

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
        } else if (type.equals(Constants.Notification_Type.FIVE_DAY.toString())) {
             sendFiveDayNotification();
        }

    }

    private void sendFiveDayNotification() {
        String title = "Reminder";
        String description = "Use perishable items from the pantry before they spoil! 🥚🍌🍅 Let's prevent food waste together!";
        notificationHelper.sendHighPriorityNotification(title, description, MainActivity.class);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Stash.getLong(Constants.LAST_TIME, System.currentTimeMillis()));
        calendar.add(Calendar.HOUR_OF_DAY, 15);
        if (Calendar.getInstance().after(calendar)) {
            calendar.add(Calendar.DAY_OF_YEAR, 5);
        }
        NotificationScheduler.scheduleDailyNotification(context, calendar, false);
    }

    private void sendDailyNotification() {
        ArrayList<WeekMeal> list = Stash.getArrayList(Constants.WEEK_MEAL, WeekMeal.class);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int adjustedDay = (calendar.get(Calendar.DAY_OF_WEEK) + 5) % 7;
        if (!list.get(adjustedDay).meal.isEmpty()) {
            String title = "Today's Special : " + list.get(adjustedDay).meal;
            String description = "Hey there! Enjoy the process of making it and savor every bite. Bon appétit!";
            notificationHelper.sendHighPriorityNotification(title, description, MainActivity.class);
//        calendar.setTimeInMillis(Stash.getLong(Constants.LAST_TIME, System.currentTimeMillis()));
            calendar.add(Calendar.HOUR_OF_DAY, Constants.HOUR);
            NotificationScheduler.scheduleDailyNotification(context, calendar, true);
        }
    }

}