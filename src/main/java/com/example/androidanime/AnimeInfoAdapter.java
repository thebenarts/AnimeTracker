package com.example.androidanime;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class AnimeInfoAdapter extends ArrayAdapter<AnimeInfo> {
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
        CheckBox animeFavourite;
    }

    public AnimeInfoAdapter(Context context, int resource, ArrayList<AnimeInfo> objects)
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
            holder.bannerImage = (ImageView) convertView.findViewById(R.id.bannerImage);
            holder.animeTitle = (TextView) convertView.findViewById(R.id.animeTitle);
            holder.animeEpisodes = (TextView) convertView.findViewById(R.id.animeEpisodes);
            holder.animeRemainingTime = (TextView) convertView.findViewById(R.id.animeRemainingTime);
            holder.animeReleaseDate = (TextView) convertView.findViewById(R.id.animeReleaseDate);
            holder.animeReleaseDay = (TextView) convertView.findViewById(R.id.animeReleaseDay);
            holder.animeFavourite = (CheckBox) convertView.findViewById(R.id.animeFavourite);

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
        holder.animeFavourite.setChecked(animeFavourite);

        holder.animeFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBAccess dbAccess = DBAccess.getInstance(getContext());
                dbAccess.open();

                dbAccess.UpdateAnimeFavourites(getItem(position).GetRowID(),getItem(position).ToggleFavourite());
                dbAccess.close();

                if(getItem(position).mFavourite)
                {
                    // set notification for this anime
                    MainActivity.getMainInstanceActivity().AddImageNotification(getItem(position));
                }
                else
                {
                    // disable notification for this anime
                    MainActivity.getMainInstanceActivity().RemoveImageNotification(getItem(position));
                }
            }
        });

        return convertView;
    }
}