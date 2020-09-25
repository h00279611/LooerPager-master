package net.codepig.trackpoint;

public class TrackPoint {

    private static TrackPointCallBack trackPointCallBack;

    private TrackPoint() {
    }

    public static void init(TrackPointCallBack callBack) {
        trackPointCallBack = callBack;
    }

    static void onClick(String pageClassName, String viewIdName) {
        if (trackPointCallBack == null) {
            return;
        }
        trackPointCallBack.onClick(pageClassName, viewIdName);
    }

    static void onPageOpen(String pageClassName) {
        if (trackPointCallBack == null) {
            return;
        }
        trackPointCallBack.onPageOpen(pageClassName);
    }

    static void onPageClose(String pageClassName) {
        if (trackPointCallBack == null) {
            return;
        }
        trackPointCallBack.onPageClose(pageClassName);
    }
}
