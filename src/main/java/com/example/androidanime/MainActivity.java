package com.example.androidanime;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.StrictMode;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.Number;
import java.lang.Long;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.widget.Toast;
import android.view.MotionEvent;

public class MainActivity extends SwipeActivity {

    ListView lvRSS;
    AlarmManager alarmManager;

    ArrayList<AnimeInfo> AnimeInfoList = new ArrayList<AnimeInfo>();
    public static WeakReference<MainActivity> weakActivity;

    public static MainActivity getMainInstanceActivity()
    {
        return weakActivity.get();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weakActivity = new WeakReference<>(MainActivity.this);

        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder(StrictMode.getVmPolicy())
                .detectLeakedClosableObjects()
                .build());

        AnimeInfoList = App.GetAppInstance().GetAnimeList();

        Calendar currentTimeCalendar = Calendar.getInstance();
        long cTime = currentTimeCalendar.getTimeInMillis();
        lvRSS = (ListView) findViewById(R.id.lvRSS);

        AnimeInfoAdapter adapter = new AnimeInfoAdapter(MainActivity.this, R.layout.anime_info_layout, AnimeInfoList);
        lvRSS.setAdapter(adapter);

        for(int i = 0; i != AnimeInfoList.size(); i++)
        {
            if(AnimeInfoList.get(i).mFavourite)
            {
                AddImageNotification(AnimeInfoList.get(i));

                RemoveAnimeFromNotification(AnimeInfoList.get(i));
            }
        }
    }

    @Override
    protected void onStop()
    {
        super.onStop();

        for(int i = 0; i != AnimeInfoList.size(); i++)
        {
            if(AnimeInfoList.get(i).mFavourite)
            {
                // remove image notification
                RemoveImageNotification(AnimeInfoList.get(i));

                // add to background notifications
                AddAnimeToNotifications(AnimeInfoList.get(i));

            }
        }
    }

    public void AddImageNotification(AnimeInfo animeInfo)
    {
        Intent intent = new Intent(MainActivity.this, ActiveBroadcast.class);
        intent.putExtra("ID",animeInfo.GetRowID());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this,  animeInfo.GetRowID(), intent, PendingIntent.FLAG_IMMUTABLE);

        if(alarmManager == null)
            alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        //one with the correct time of the anime release
        alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,animeInfo.GetTimeOfNextEpisode(),pendingIntent);

    }

    public void RemoveImageNotification(AnimeInfo animeInfo)
    {

        Intent intent = new Intent(MainActivity.this, ActiveBroadcast.class);
        intent.putExtra("ID",animeInfo.GetRowID());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this,  animeInfo.GetRowID(), intent, PendingIntent.FLAG_IMMUTABLE);

        if(alarmManager == null)
            alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        alarmManager.cancel(pendingIntent);
    }

    public void onSwipeLeft()
    {
        Intent intent = new Intent(MainActivity.this, FavouriteActivity.class);
        startActivity(intent);
    }

    public void onSwipeRight()
    {
        Notify();
    }

    public void NotificationFiredUpdateAnime(int inID)
    {
        Log.d("animeLogger", "HELLO WE HAVE BEEN CALLED");
    }

    public void AddAnimeToNotifications(AnimeInfo inAnime)
    {

        Intent intent = new Intent(MainActivity.this, ReminderBroadcast.class);
        intent.putExtra("EpNum",inAnime.mEpisode);
        intent.putExtra("animeTitle",inAnime.mTitle);
        intent.putExtra("ID",inAnime.GetRowID());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, inAnime.GetRowID(), intent, PendingIntent.FLAG_IMMUTABLE);

        if(alarmManager == null)
            alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        //one with the correct time of the anime release
        alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,inAnime.GetTimeOfNextEpisode(),pendingIntent);

    }

    public void RemoveAnimeFromNotification(AnimeInfo inAnime)
    {
        Intent intent = new Intent(MainActivity.this, ReminderBroadcast.class);
        intent.putExtra("EpNum",inAnime.mEpisode);
        intent.putExtra("animeTitle",inAnime.mTitle);
        intent.putExtra("ID",inAnime.GetRowID());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, inAnime.GetRowID(), intent, PendingIntent.FLAG_IMMUTABLE);

        if(alarmManager == null)
            alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        alarmManager.cancel(pendingIntent);
    }

    public void Notify()
    {
        AnimeInfo a = AnimeInfoList.get(0);
        Bitmap bmp = BitmapFactory.decodeResource(getResources(),a.mImage);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, App.CHANNEL_1).
                setSmallIcon(R.drawable.ic_baseline_event_available_24).
                setContentTitle("Episode " + String.valueOf(a.mEpisode) + " of " + a.mTitle + " is out!").
                setContentText("GOO WATCH IT NOW!!!").
                setLargeIcon(bmp).
                        setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainActivity.this);

        notificationManager.notify(0, builder.build());
    }

    public void Notify(int inID)
    {
        int selected = 0;
        for(int i = 0; i != AnimeInfoList.size(); i++)
        {
            if(AnimeInfoList.get(i).GetRowID() == inID)
            {
                selected = i;
                break;
            }
        }

        AnimeInfo a = AnimeInfoList.get(selected);
        Bitmap bmp = BitmapFactory.decodeResource(getResources(),a.mImage);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, App.CHANNEL_1).
                setSmallIcon(R.drawable.ic_baseline_event_available_24).
                setContentTitle("Episode " + String.valueOf(a.mEpisode) + " of " + a.mTitle + " is out!").
                setContentText("GOO WATCH IT NOW!!!").
                setLargeIcon(bmp).
                setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainActivity.this);

        notificationManager.notify(0, builder.build());
    }


    byte[] convertBitmapToByteArray(Bitmap bitmap)
    {
        ByteArrayOutputStream baos = null;
        try{
            baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG,25,baos);
            return baos.toByteArray();
        } finally {
            if(baos != null)
                try{
                    baos.close();
                } catch (IOException e)
                {
                    Log.e("WTF","BYTEARRAYOUTPUTSTREAM WAS NOT CLOSED");
                }
        }
    }

}