package com.example.androidanime;

import android.app.Application;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

public class ReminderBroadcast  extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, App.CHANNEL_1).
                setSmallIcon(R.drawable.ic_baseline_event_available_24).
                setContentTitle("Episode " + String.valueOf(intent.getExtras().getInt("EpNum")) + " of " + intent.getExtras().getString("animeTitle") + " is out!").
                setContentText("GOO WATCH IT NOW!!!").
                setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(intent.getExtras().getInt("ID"), builder.build());
    }
}
