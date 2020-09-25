package com.sunofbeaches.looerpager.constants;

public class DBConstants {

    public static final String CREATE_COMPANY_TABLE ="CREATE TABLE IF NOT EXISTS tal_company(_id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR)";

    public static final String CREATE_USER_PRIVATE_COMPANY_TABLE ="CREATE TABLE IF NOT EXISTS tal_private_company(\n" +
            "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "    company_id int,\n" +
            "    is_open boolean)";


    public static final String UPDATE_COMPANY_TABLE ="alter table tal_company add test int ";

}
