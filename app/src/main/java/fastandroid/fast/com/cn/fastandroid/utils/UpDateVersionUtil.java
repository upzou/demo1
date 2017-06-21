package fastandroid.fast.com.cn.fastandroid.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import fastandroid.fast.com.cn.fastandroid.activity.UpdateVersionService;


/**
 * Created by zzs on 2017/5/15
 */

public class UpDateVersionUtil {
    private static final String TAG = "UpDateVersionUtil";

    public static void UpDateVersion(final Context mContext) {

        PackageManager packageManager = mContext.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String versionName = packageInfo.versionName;
//        String versionName = "1.2";
        Log.e(TAG, "versionName: " + versionName);

        String androidver = SPUtil.getString(mContext, "androidver", "");

        if (versionName.equals(androidver)) {
            Toast.makeText(mContext, "当前是最新版本", Toast.LENGTH_SHORT).show();
        } else {

            int checkedNetWorkType = NetWorkUtil.checkedNetWorkType(mContext);
            Log.e(TAG, "checkedNetWorkType: " + checkedNetWorkType);

            switch (checkedNetWorkType) {
                case NetWorkUtil.NONETWORK:
                    Toast.makeText(mContext, "网络连接异常,请检查后再试!", Toast.LENGTH_SHORT).show();
                    break;
                case NetWorkUtil.WIFI:
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                    builder1.setTitle("发现新版本")
                            .setMessage("当前版本:" + versionName + "\n最新版本:" + androidver)
                            .setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
//                                        downAsynFile();

                                    Intent intent = new Intent(mContext, UpdateVersionService.class);

                                    boolean isServiceWork = ServiceUtil.isServiceWork(mContext, "fastandroid.fast.com.cn.fastandroid.activity.UpdateVersionService");
                                    if (isServiceWork) {
                                        Toast.makeText(mContext, "正在下载最新的安装包", Toast.LENGTH_SHORT).show();
                                    } else {
                                        mContext.startService(intent);
                                    }
                                }
                            })
                            .setNegativeButton("以后再说", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    SPUtil.setBoolean(mContext, "UpDateLater", true);
                                }
                            }).show();
                    break;
                case NetWorkUtil.NOWIFI:
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("发现新版本!");
                    builder.setMessage("当前不是wifi连接,继续下载会耗费数据流量,是否继续?")
                            .setPositiveButton("继续", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

//                                            if (file.exists() && file.getName().equals("gdmsaec-app.apk")) {
//                                            }

                                    Intent intent = new Intent(mContext, UpdateVersionService.class);
//                                        intent.putExtra("downloadUrl", "http://app.mi.com/download/294");
                                    boolean isServiceWork = ServiceUtil.isServiceWork(mContext, "fastandroid.fast.com.cn.fastandroid.activity.UpdateVersionService");
                                    if (isServiceWork) {
                                        Toast.makeText(mContext, "正在下载最新的安装包", Toast.LENGTH_SHORT).show();
                                    } else {
                                        mContext.startService(intent);
                                    }

                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    SPUtil.setBoolean(mContext, "UpDateLater", true);
                                }
                            }).show();
                    break;
            }
        }
    }


//    public String getVersionName(Context context) {
//            PackageManager packageManager = context.getPackageManager();
//            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
//            return packageInfo.versionName;
//
//        return null;
//    }

}
