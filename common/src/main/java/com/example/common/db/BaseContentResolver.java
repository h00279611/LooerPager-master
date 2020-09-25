package com.example.common.db;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.example.common.bean.DBModel;
import com.example.common.bean.Table;

import java.util.List;

public class BaseContentResolver<T extends DBModel> extends BaseDataDone<T> {

    private final ContentResolver mContentResolver;

    public BaseContentResolver(Context context, Class<T> mClass) {
        super(context, mClass);
        this.mContentResolver = context.getContentResolver();
    }


    @Override
    protected void insert(T dbModel, Table table, ContentValues values) {
        mContentResolver.insert(getUrl(table), values);
    }


    @Override
    protected void batchInsert(List<T> list, Table table, List<ContentValues> contentValues) {
        for (ContentValues values : contentValues) {
            mContentResolver.insert(getUrl(table), values);
        }
    }


    @Override
    protected void deleteAll(Table table) {
        mContentResolver.delete(getUrl(table), null, null);
    }

    @Override
    protected Cursor getCursor(Table table) {
        return mContentResolver.query(getUrl(table), null, null, null, null);
    }


    protected Uri getUrl(Table table) {
        String packageName = mContext.getPackageName();
        String uri = "content://" + packageName + "/contentResolver" + table.getUrl();
        return Uri.parse(uri);
    }

}
