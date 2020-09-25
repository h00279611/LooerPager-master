package com.example.common.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.common.bean.DBModel;
import com.example.common.db.DBHelper;

public abstract class BaseContentProvider<T extends DBModel> extends ContentProvider {

    private DBHelper mDbHelper;
    private SQLiteDatabase mDatabase;

    public BaseContentProvider() {
        mDbHelper = new DBHelper(getContext());
    }

    protected abstract String getTable();


    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return mDatabase.query(getTable(), projection, selection, selectionArgs, null, null, sortOrder);
    }


    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        mDatabase = mDbHelper.getWritableDatabase();
        long id = mDatabase.insert(getTable(), null, values);
        uri = ContentUris.withAppendedId(uri, id);
        mDatabase.close();
        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
