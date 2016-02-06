package com.example.wataru.room_memo;

import android.content.Context;
import android.database.Cursor;

import maneger.DatabaseManager;

/**
 * Created by wataru on 2016/01/31.
 */
public class MyCursorLoader extends SimpleCursorLoader {
    private long mPcId;

    public MyCursorLoader(Context context, long pcId) {
        super(context);
        mPcId = pcId;
    }

    @Override
    public Cursor loadInBackground() {
        DatabaseManager manager = new DatabaseManager(getContext());
        return manager.findPreviewConfirmDetailsById(mPcId);
    }
}
