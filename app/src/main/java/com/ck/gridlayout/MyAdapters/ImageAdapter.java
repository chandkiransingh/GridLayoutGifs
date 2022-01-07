package com.ck.gridlayout.MyAdapters;

import android.content.Context;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ck.gridlayout.MyClasses.ImagesData;
import com.ck.gridlayout.R;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class ImageAdapter extends BaseAdapter {
    // Keep all Images in array


    private Context mContext;
    private ArrayList<ImagesData> imglist;

    // Constructor
    public ImageAdapter(Context c, ArrayList<ImagesData> imglist) {
        mContext = c;
        this.imglist = imglist;
    }

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return imglist.size();
    }

    public Object getItem(int position) {
        ImagesData img = imglist.get(position);
        return img.getOriginalImage().toString();
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        int screenWidth = metrics.widthPixels;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(screenWidth / 5, screenWidth / 5));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        try{
            ImagesData img = imglist.get(position);
            Glide.with(mContext).load(img.getPreviewImage()).into(imageView);
        }
        catch (Exception e){
            Log.d(TAG, "getView: exception is: "+e.getMessage().toString());
        }

        return imageView;
    }


//    public Integer[] mThumbIds = {
//            R.drawable.ic_launcher_background, R.drawable.ic_launcher_foreground,
//            R.drawable.ic_launcher_background, R.drawable.ic_launcher_foreground,
//            R.drawable.ic_launcher_background, R.drawable.ic_launcher_foreground,
//            R.drawable.ic_launcher_background, R.drawable.ic_launcher_foreground,
//            R.drawable.ic_launcher_background, R.drawable.ic_launcher_foreground,
//            R.drawable.ic_launcher_background, R.drawable.ic_launcher_foreground,
//            R.drawable.ic_launcher_background, R.drawable.ic_launcher_foreground,
//            R.drawable.ic_launcher_background, R.drawable.ic_launcher_foreground,
//            R.drawable.ic_launcher_background, R.drawable.ic_launcher_foreground,
//            R.drawable.ic_launcher_background, R.drawable.ic_launcher_foreground,
//            R.drawable.ic_launcher_background, R.drawable.ic_launcher_foreground,
//            R.drawable.ic_launcher_background, R.drawable.ic_launcher_foreground,
//            R.drawable.ic_launcher_background
//    };

}