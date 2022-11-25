package com.example.androidanime;

import android.util.Log;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

public class AnimeInfo {
    private int animeID;
    int mImage;
    String mTitle;
    Date mDate;     // first episodes release date
    int mAllEpisodes;
    int mEpisode;
    long mTimeUntilNextEpisode;
    long mTimeOfNextEpisode;
    boolean mFinished;
    AnimeDateTime mDateTime;    // custom class that handles the formatting of time left till next episode
    String mReleaseDateString;
    String mReleaseDayString;
    boolean mFavourite;

    public AnimeInfo(int inID, int inImage, String inTitle, long inTime, int inEpisodes, boolean isFav)
    {
        animeID = inID;
        mImage = inImage;
        mTitle = inTitle;
        mDate = new Date(inTime);
        mAllEpisodes = inEpisodes;
        mFinished = false;
        mReleaseDateString = mDate.toString();
        UpdateTimeOfNextEpisode();
        mDateTime = new AnimeDateTime(UpdateTimeUntilNextEpisode());
        mReleaseDayString = UpdateReleaseDayString();
        mFavourite = isFav;
    }

    public void Update()
    {
        mDateTime.Update(UpdateTimeUntilNextEpisode());
    }

    private long UpdateTimeOfNextEpisode()
    {
        Calendar rightNow = Calendar.getInstance();
        long cTime = rightNow.getTimeInMillis();
        long cEpTime = mDate.getTime();
        int EpNum = 1;

        while(cEpTime < cTime)
        {
            cEpTime += (604800L * 1000L);
            EpNum++;
        }

        if(EpNum > mAllEpisodes)
        {
            mEpisode = mAllEpisodes;
            mFinished = true;
        }
        else
            mEpisode = EpNum;

        return mTimeOfNextEpisode = cEpTime;
    }

    private long UpdateTimeUntilNextEpisode()
    {
        Calendar rightNow = Calendar.getInstance();
        long cTime = rightNow.getTimeInMillis();

        return mTimeUntilNextEpisode = mTimeOfNextEpisode - cTime;
    }

    public String getEpisode()
    {
        return String.valueOf(mEpisode) + " / " + String.valueOf(mAllEpisodes);
    }

    public String getDateTimeString()
    {
        return mDateTime.GetDateTimeString();
    }

    private String UpdateReleaseDayString()
    {
        String fullString = mDate.toString();

        String day = fullString.substring(0,3);

        switch (day) {
            case "Mon":
                return "Monday";
            case "Tue":
                return "Tuesday";
            case "Wed":
                return "Wednesday";
            case "Thu":
                return "Thursday";
            case "Fri":
                return "Friday";
            case "Sat":
                return "Saturday";
            case "Sun":
                return "Sunday";
        }

        return day;
    }

    public void AnimeLogger()
    {
        Log.d("animeLogger", String.valueOf(animeID) + " " + mTitle);
        Log.d("animeLogger", mDate.toString());
        Log.d("animeLogger", String.valueOf(mEpisode));
        Log.d("animeLogger", mDateTime.GetDateTimeString());
        Log.d("animeLogger", String.valueOf(mFavourite));
    }

    public boolean ToggleFavourite()
    {
        return mFavourite = !mFavourite;
    }

    static class AnimeComparatorAscending implements Comparator<AnimeInfo>
    {

        @Override
        public int compare(AnimeInfo a1, AnimeInfo a2)
        {
            long a1Time = a1.mTimeUntilNextEpisode;
            long a2Time = a2.mTimeUntilNextEpisode;

            if(a1Time > a2Time)
                return 1;
            if(a2Time > a1Time)
                return -1;

            return 0;
        }
    }

    static class AnimeComparatorDescending implements Comparator<AnimeInfo>
    {

        @Override
        public int compare(AnimeInfo a1, AnimeInfo a2)
        {
            long a1Time = a1.mTimeUntilNextEpisode;
            long a2Time = a2.mTimeUntilNextEpisode;

            if(a1Time < a2Time)
                return 1;
            if(a2Time < a1Time)
                return -1;

            return 0;
        }
    }

    public int GetRowID()
    {
        return animeID;
    }

    public long GetTimeOfNextEpisode()
    {
        return mTimeOfNextEpisode;
    }
}