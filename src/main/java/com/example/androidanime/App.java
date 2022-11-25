package com.example.androidanime;

import android.app.AlarmManager;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.widget.ListView;

import java.lang.ref.WeakReference;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Collections;

public class App  extends Application {

    public static final String CHANNEL_1 = "notifyAnime";
    public static final String CHANNEL_2 = "reminder";

    ArrayList<AnimeInfo> AnimeInfoList = new ArrayList<AnimeInfo>();
    static WeakReference<App> appWeakReference;

    @Override
    public void onCreate()
    {
        super.onCreate();
        appWeakReference = new WeakReference<>(this);

        DBAccess dbAccess = DBAccess.getInstance(getApplicationContext());
        dbAccess.open();
        AnimeInfoList = dbAccess.GetAllAnime();
        dbAccess.close();

        Collections.sort(AnimeInfoList, new AnimeInfo.AnimeComparatorAscending());
        createNotificationChannel();
        InitWeeklyReminder();
    }

    public static App GetAppInstance()
    {
        return appWeakReference.get();
    }

    public ArrayList<AnimeInfo> GetAnimeList()
    {
        return AnimeInfoList;
    }

    private void createNotificationChannel()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationManager notificationManager = getSystemService(NotificationManager.class);

            CharSequence name = "AnimeReminderChannel";
            String description = "Channel for Anime Reminder";
            NotificationChannel channel = new NotificationChannel(CHANNEL_1,name,NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(description);
            notificationManager.createNotificationChannel(channel);

            CharSequence name2 = "ReminderToOpenApp";
            NotificationChannel channel2 = new NotificationChannel(CHANNEL_2,name,NotificationManager.IMPORTANCE_HIGH);
            channel2.setDescription("Channel for opening Reminder");
            notificationManager.createNotificationChannel(channel2);
        }
    }

    private void InitWeeklyReminder()
    {
        Intent intent = new Intent(App.this, WeeklyReminder.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(App.this, 200, intent, PendingIntent.FLAG_IMMUTABLE);


        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        long currentTime = System.currentTimeMillis();
        long littleLessThanWeek = 604800L * 1000L;
        alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,currentTime+littleLessThanWeek,pendingIntent);
    }
}
