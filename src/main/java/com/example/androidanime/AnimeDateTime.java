package com.example.androidanime;

import android.util.Log;

public class AnimeDateTime {
    int mDays;
    int mHours;
    int mMinutes;
    long mReleaseTime;
    String mDateTimeString;

    public AnimeDateTime(long inReleaseTime) {
        mReleaseTime = inReleaseTime;
        UpdateDateTime();
    }

    public void Update(long inTime)
    {
        mReleaseTime = inTime;
        UpdateDateTime();
    }

    private void UpdateDateTime()
    {
        mDays = (int)((mReleaseTime / 1000) / (60*60*24));
        if(mDays != 0)
            mReleaseTime -= mDays * 1000 * (60*60*24);

        mHours = (int)((mReleaseTime / 1000) /(60 * 60));
        if(mHours != 0)
            mReleaseTime -= mHours * 1000 * (60 * 60);

        mMinutes = (int)((mReleaseTime / 1000) /60);
        if(mMinutes >= 60)
        {
            mMinutes-=60;
            mHours++;
        }

        UpdateDateTimeString();
    }

    private void UpdateDateTimeString()
    {
        String dateTime = new String("");

        if(mDays != 0)
        {
            dateTime += String.valueOf(mDays);

            if(mDays == 1)
                dateTime += " day, ";
            else
                dateTime += " days, ";
        }

        if(mHours != 0)
        {
            dateTime += String.valueOf(mHours);

            if(mHours == 1)
                dateTime += " hour, ";
            else
                dateTime +=" hours, ";

        }

        if(mMinutes != 0)
        {
            dateTime += String.valueOf(mMinutes);

            if(mMinutes == 1)
                dateTime+= " minute remaining.";
            else
                dateTime+= " minutes remaining.";

        }

        mDateTimeString = dateTime;
    }

    public String GetDateTimeString()
    {
        return mDateTimeString;
    }
}
