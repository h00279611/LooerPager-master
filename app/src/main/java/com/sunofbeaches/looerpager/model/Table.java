package com.sunofbeaches.looerpager.model;

import java.util.List;

public class Table {

    private String tableName;
    private List<TableAttr> mTableAttrList;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<TableAttr> getTableAttrList() {
        return mTableAttrList;
    }

    public void setTableAttrList(List<TableAttr> tableAttrList) {
        mTableAttrList = tableAttrList;
    }

    public static class TableAttr{
        private String fileName;
        private String dbAttrName;
        private boolean isInsert;
        private boolean isKey;

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getDbAttrName() {
            return dbAttrName;
        }

        public void setDbAttrName(String dbAttrName) {
            this.dbAttrName = dbAttrName;
        }

        public boolean isInsert() {
            return isInsert;
        }

        public void setInsert(boolean insert) {
            isInsert = insert;
        }

        public boolean isKey() {
            return isKey;
        }

        public void setKey(boolean key) {
            isKey = key;
        }
    }
}

