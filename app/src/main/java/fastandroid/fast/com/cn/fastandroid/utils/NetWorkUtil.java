package fastandroid.fast.com.cn.fastandroid.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by zzs on 2017/5/10
 */

public class NetWorkUtil {

    public static final int NONETWORK = 0;//没有网络
    public static final int WIFI = 1;//当前是WiFi连接
    public static final int NOWIFI = 2;//当前不是WiFi连接

    //检查是否有网络连接
    public static boolean checkedNetWork(Context context) {
        //获得连接设备管理器
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return false;
        }

        //获取网络连接对象
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isAvailable()) {
            return false;
        }

        return true;
    }

    //检查当前网络的类型
    public static int checkedNetWorkType(Context context) {
        if (!checkedNetWork(context)) {
            return NONETWORK;
        }

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting()) {
            return WIFI;
        } else {
            return NOWIFI;
        }

    }

}
