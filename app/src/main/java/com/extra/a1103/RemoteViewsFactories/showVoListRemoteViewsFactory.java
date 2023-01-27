package com.extra.a1103.RemoteViewsFactories;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.extra.a1103.R;

public class showVoListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory{
    private Context ctxt=null;
    private int appWidgetId;

    public showVoListRemoteViewsFactory(Context ctxt, Intent intent) {
        this.ctxt=ctxt;
        appWidgetId=intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return 50;
    }

    @Override
    public RemoteViews getViewAt(int i) {
        @SuppressLint("RemoteViewLayout")
        RemoteViews v=new RemoteViews(ctxt.getPackageName(), R.layout.show_vo_listview_layout);
        v.setTextViewText(R.id.txt_voNum,"ctxt");
        return v;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 50;
    }

    @Override
    public long getItemId(int i) {
        return 500;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}

