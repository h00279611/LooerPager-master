package net.codepig.trackpoint;

public interface TrackPointCallBack {

    void onClick(String pageClassName, String viewIdName);

    void onPageOpen(String pageClassName);

    void onPageClose(String pageClassName);
}
