package com.example.androidanime;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ActiveBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("YOYO","ActiveBroadcast should be calling notify");

        MainActivity.getMainInstanceActivity().Notify(intent.getExtras().getInt("ID"));
    }
}
