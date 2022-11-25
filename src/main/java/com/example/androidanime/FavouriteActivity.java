package com.example.androidanime;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class FavouriteActivity extends SwipeActivity {

    ListView lvFav;

    ArrayList<AnimeInfo> AnimeFavList = new ArrayList<AnimeInfo>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        DBAccess dbAccess = DBAccess.getInstance(getApplicationContext());
        dbAccess.open();
        AnimeFavList = dbAccess.GetFavAnime();
        dbAccess.close();

        Calendar currentTimeCalendar = Calendar.getInstance();
        long cTime = currentTimeCalendar.getTimeInMillis();

        lvFav = (ListView) findViewById(R.id.lvFav);

        Collections.sort(AnimeFavList,new AnimeInfo.AnimeComparatorAscending());
        AnimeFavAdapter adapter = new AnimeFavAdapter(FavouriteActivity.this, R.layout.anime_fav_layout, AnimeFavList);
        lvFav.setAdapter(adapter);

    }

    public void onSwipeLeft()
    {

    }

    public void onSwipeRight()
    {
        finish();
    }
}