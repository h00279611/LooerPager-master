package com.example.common.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.common.bean.DBModel;
import com.example.common.bean.Table;
import com.example.common.util.ReflectUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public abstract class BaseDataDone<T extends DBModel> implements IBase<T> {

    protected final String TAG = "BaseDataDone";

    protected Class<T> mClass;

    protected Context mContext;

    public BaseDataDone(Context context, Class<T> mClass) {
        this.mContext = context;
        this.mClass = mClass;
    }

    protected abstract void insert(T t, Table table, ContentValues values);

    protected abstract void batchInsert(List<T> list, Table table, List<ContentValues> values);

    protected abstract void deleteAll(Table table);

    protected abstract Cursor getCursor(Table table);


    public boolean insert(T t) {
        return done(table -> {
            ContentValues values = convertValues(table, t);
            insert(t, table, values);
        });
    }


    public boolean batchInsert(List<T> list) {
        return done(table -> {
            List<ContentValues> values = list.stream().map(v -> convertValues(table, v)).collect(Collectors.toList());
            batchInsert(list, table, values);
        });
    }


    public boolean deleteAll() {
        return done(table -> deleteAll(table));
    }


    public List<T> query() {
        List<T> list = new ArrayList<>();
        try {
            Table table = convertTable();
            if (table == null) {
                return null;
            }

            Cursor cursor = getCursor(table);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    Object obj = getObjFromCursor(table, cursor);
                    list.add((T) obj);
                    cursor.moveToNext();
                }
            }
        } finally {
            DatabaseManager.getInstance().closeDatabase();
        }

        return list;
    }


    private boolean done(Consumer<Table> consumer) {
        try {
            Table table = convertTable();
            if (table == null) {
                Log.w(TAG, "object is not set table annotation");
                return false;
            }

            Optional.ofNullable(consumer).ifPresent(v -> consumer.accept(table));
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return false;
        }

        return true;
    }


    private Table convertTable() {
        return TableParser.getInstance().convertTable(mClass);
    }


    private ContentValues convertValues(Table table, T t) {
        ContentValues values = new ContentValues();
        List<Table.TableAttr> tableAttrList = table.getTableAttrList();
        if (tableAttrList == null) {
            return null;
        }

        for (Table.TableAttr tableAttr : tableAttrList) {
            String filedName = tableAttr.getFiledName();
            Object value = ReflectUtils.getValue(t, filedName);

            try {
                Field field = t.getClass().getDeclaredField(filedName);
                Class<?> fieldType = field.getType();
                if (fieldType == Integer.class || fieldType == int.class) {
                    values.put(filedName, Integer.parseInt(value.toString()));
                } else if (fieldType == Boolean.class || fieldType == boolean.class) {
                    values.put(filedName, Boolean.parseBoolean(value.toString()));
                } else if (fieldType == Double.class || fieldType == double.class) {
                    values.put(filedName, Double.parseDouble(value.toString()));
                } else if (fieldType == Float.class || fieldType == float.class) {
                    values.put(filedName, Float.parseFloat(value.toString()));
                } else if (fieldType == Long.class || fieldType == long.class) {
                    values.put(filedName, Long.parseLong(value.toString()));
                } else {
                    values.put(filedName, String.valueOf(value));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return values;
    }


    private Object getObjFromCursor(Table table, Cursor cursor) {
        Object obj = null;
        try {
            obj = mClass.newInstance();
            List<Table.TableAttr> attrList = table.getTableAttrList();
            for (Table.TableAttr tableAttr : attrList) {
                int columnIndex = cursor.getColumnIndex(tableAttr.getDbAttrName());
                String value = cursor.getString(columnIndex);
                if(value == null){
                    continue;
                }

                Field field = mClass.getDeclaredField(tableAttr.getFiledName());
                field.setAccessible(true);
                Class<?> fieldType = field.getType();
                field.set(obj, getValue(value, fieldType));
            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        return obj;
    }

    private Object getValue(String value, Class<?> fieldType) {
        if (fieldType == Boolean.class || fieldType == boolean.class) {
            return Boolean.valueOf(value);
        } else if (fieldType == Integer.class || fieldType == int.class) {
            return Integer.valueOf(value);
        }

        return value;
    }


}
