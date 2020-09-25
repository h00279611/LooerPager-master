package com.example.common.bean;

import java.util.List;

public class Table {

    private String tableName;
    private String url;
    private List<TableAttr> mTableAttrList;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<TableAttr> getTableAttrList() {
        return mTableAttrList;
    }

    public void setTableAttrList(List<TableAttr> tableAttrList) {
        mTableAttrList = tableAttrList;
    }

    public static class TableAttr {
        private String filedName;
        private String dbAttrName;
        private Class<?> filedType;
        private boolean isInsert;
        private boolean isKey;

        public String getFiledName() {
            return filedName;
        }

        public void setFiledName(String filedName) {
            this.filedName = filedName;
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

        public void setFiledType(Class<?> filedType) {
            this.filedType = filedType;
        }

        public Class<?> getFiledType() {
            return filedType;
        }
    }
}

