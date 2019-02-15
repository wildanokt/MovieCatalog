package com.wildanokt.searchmoviemade.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Binder;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.wildanokt.searchmoviemade.BuildConfig;
import com.wildanokt.searchmoviemade.R;
import com.wildanokt.searchmoviemade.db.DatabaseContract;
import com.wildanokt.searchmoviemade.db.DatabaseHelper;
import com.wildanokt.searchmoviemade.entity.Favorite;

import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.wildanokt.searchmoviemade.db.DatabaseContract.CONTENT_URI;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

//    private final List<Bitmap> mWidgetItems = new ArrayList<>();
//    private List<Favorite> list = new ArrayList<>();

    private Cursor cursor;
    private Context mContext;
    private int mAppWidgetId;


    public StackRemoteViewsFactory(Context mContext, Intent intent) {
        this.mContext = mContext;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        loadData();
    }

    private void loadData(){
        cursor = mContext.getContentResolver().query(
                CONTENT_URI,
                null,
                null,
                null,
                null
        );
    }

    @Override
    public void onDataSetChanged() {
        final long identityToken = Binder.clearCallingIdentity();
        loadData();
        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Favorite item = getItem(position);
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        Bitmap bitmap = null;

        String imageUrl = BuildConfig.IMAGE_URL+item.getImgPath();
        String image = imageUrl.replace("/w185","/w500");
        Log.i("PROCESS", "getImage:"+image);
        try {
            bitmap = Glide.with(mContext)
                    .asBitmap()
                    .load(image)
                    .into(com.bumptech.glide.request.target.Target.SIZE_ORIGINAL, com.bumptech.glide.request.target.Target.SIZE_ORIGINAL)
                    .get();
        }catch (InterruptedException | ExecutionException e){
            e.printStackTrace();
            Log.e("EXCEPTION", "getViewAt: ERROR:"+e.getMessage() );
        }
        Bundle extras = new Bundle();
        extras.putInt(FavoriteWidget.EXTRA_ITEM, position);

        Intent fillIntent = new Intent();
        fillIntent.putExtras(extras);

        rv.setImageViewBitmap(R.id.widget_image, bitmap);
        rv.setOnClickFillInIntent(R.id.widget_image, fillIntent);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    public Favorite getItem(int position){
        if (!cursor.moveToPosition(position)){
            throw new IllegalStateException("Position invalid");
        }
        return new Favorite(cursor);
    }
}
