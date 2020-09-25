package com.sunofbeaches.looerpager;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.print.PrintManager;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.print.PrintHelper;

import com.sunofbeaches.looerpager.print.MyPrintPdfAdapter;

public class PrintActivity extends BaseActivity {

    private View mPrintImgBt;
    private View mPrintPdfBt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print);

        initView();
        initEvent();
    }

    private void initView() {
        mPrintImgBt = findViewById(R.id.printImg);
        mPrintPdfBt = findViewById(R.id.printPdf);
    }

    private void initEvent() {
        mPrintImgBt.setOnClickListener(v -> {
            doPhotoPrint();
        });


        mPrintPdfBt.setOnClickListener(v -> {
//            doPdfPrint();
        });
    }

    private void doPdfPrint(String filePath) {
        PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
        MyPrintPdfAdapter myPrintAdapter = new MyPrintPdfAdapter(filePath);
        printManager.print("jobName", myPrintAdapter, null);
    }

    private void doPhotoPrint() {
        PrintHelper photoPrinter = new PrintHelper(this);
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FILL);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.icon_choice_ok);
        photoPrinter.printBitmap("droids.jpg - test print", bitmap);
    }
}
