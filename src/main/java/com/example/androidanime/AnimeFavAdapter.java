package com.example.androidanime;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class AnimeFavAdapter extends ArrayAdapter<AnimeInfo> {
    private static final String TAG = "AnimeListAdapter";

    private Context mContext;
    private int mResource;
    private int lastPosition = -1;

    private static class ViewHolder {
        ImageView bannerImage;
        TextView animeTitle;
        TextView animeEpisodes;
        TextView animeRemainingTime;
        TextView animeReleaseDate;
        TextView animeReleaseDay;
    }

    public AnimeFavAdapter(Context context, int resource, ArrayList<AnimeInfo> objects)
    {
        super(context,resource,objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        int bannerImage = getItem(position).mImage;
        String animeTitle = getItem(position).mTitle;
        String animeEpisodes = getItem(position).getEpisode();
        String animeRemainingTime = getItem(position).getDateTimeString();
        String animeReleaseDate = getItem(position).mReleaseDateString;
        String animeReleaseDay = getItem(position).mReleaseDayString;
        boolean animeFavourite = getItem(position).mFavourite;

        // create the view result for showing the animation
        final View result;

        // viewHolder object
        ViewHolder holder;

        if(convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder = new ViewHolder();
            holder.bannerImage = (ImageView) convertView.findViewById(R.id.favImage);
            holder.animeTitle = (TextView) convertView.findViewById(R.id.favTitle);
            holder.animeEpisodes = (TextView) convertView.findViewById(R.id.favEpisodes);
            holder.animeRemainingTime = (TextView) convertView.findViewById(R.id.favRemainingTime);
            holder.animeReleaseDate = (TextView) convertView.findViewById(R.id.favReleaseDate);
            holder.animeReleaseDay = (TextView) convertView.findViewById(R.id.favReleaseDay);

            result = convertView;

            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        holder.bannerImage.setImageResource(bannerImage);
        holder.animeTitle.setText(animeTitle);
        holder.animeEpisodes.setText(animeEpisodes);
        holder.animeEpisodes.setText(animeEpisodes);
        holder.animeRemainingTime.setText(animeRemainingTime);
        holder.animeReleaseDate.setText(animeReleaseDate);
        holder.animeReleaseDay.setText(animeReleaseDay);

        return convertView;
    }
}