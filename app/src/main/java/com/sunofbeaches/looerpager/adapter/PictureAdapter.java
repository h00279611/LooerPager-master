package com.sunofbeaches.looerpager.adapter;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.sunofbeaches.looerpager.model.PagerItem;
import com.sunofbeaches.looerpager.utils.UrlUtils;

import java.util.List;

public class PictureAdapter extends PagerAdapter {

    private final List<PagerItem> pagerItemList;

    public PictureAdapter(List<PagerItem> pagerItemList) {
        this.pagerItemList = pagerItemList;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }



    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        final int realPosition = position % pagerItemList.size();
        final ImageView itemView = new ImageView(container.getContext());
        final PagerItem pagerItem = pagerItemList.get(realPosition);
        if (pagerItem.getUrl() == null || "".equals(pagerItem.getUrl())) {
            itemView.setImageResource(pagerItem.getPicResId());
        } else {
            final Handler pictureHandler =  new PictureHandler(itemView);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Bitmap bitmap = UrlUtils.getURLimage(pagerItem.getUrl());
                    Message message = new Message();
                    message.obj = bitmap;
                    message.what=0;
                    pictureHandler.sendMessage(message);
                }
            }).start();


            // 此种方式因为在HandMessage中处理要耗时较长时间会报：The application may be doing too much work on its main thread
            // 所以一般会另起线程，又因为要调用主线程的视图元素,需要用Handler,否则会报：Only the original thread that created a view hierarchy can touch its views
//            Message message = new Message();
//            message.what=1;
//            message.obj = pagerItem.getUrl();
//            pictureHandler.sendMessage(message);

        }

        itemView.setScaleType(ImageView.ScaleType.FIT_XY);
        container.addView(itemView);
        return itemView;
    }


    public int getDataSize() {
        return pagerItemList.size();
    }

    private class PictureHandler extends Handler {

        private final ImageView imageView;

        public PictureHandler(ImageView imageView) {
            this.imageView = imageView;
        }

        //在消息队列中实现对控件的更改
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Bitmap bmp = (Bitmap) msg.obj;
                    imageView.setImageBitmap(bmp);
                    break;
                case 1:
                    String url = (String) msg.obj;
                    Bitmap bitmap = UrlUtils.getURLimage(url);
                    imageView.setImageBitmap(bitmap);
                    break;
            }
        }
    }


}
