package com.sunofbeaches.looerpager.manager;

import android.content.Context;
import android.os.Message;

import com.sunofbeaches.looerpager.model.Company;

import java.util.List;

public class CompanyManager extends BaseManager<Company> {

    private CompanyListener mCompanyListener;

    public CompanyManager(Context context) {
        super(context);
    }

    public void setCompanyListener(CompanyListener companyListener) {
        mCompanyListener = companyListener;
    }

    @Override
    protected void handlerAddMsg(Message msg) {
        boolean status = (boolean) msg.obj;
        if (mCompanyListener != null) {
            mCompanyListener.addCompany(status);
        }
    }

    @Override
    protected void handlerQueryMsg(Message msg) {
        List<Company> list = (List<Company>) msg.obj;
        if (mCompanyListener != null) {
            mCompanyListener.getCompany(list);
        }
    }


    public void query(CompanyListener companyListener){
        this.mCompanyListener = companyListener;
        super.query();
    }




    public interface CompanyListener {

        default void addCompany(boolean status) {
        }

        default void getCompany(List<Company> list) {
        }

    }

}


