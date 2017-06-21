package fastandroid.fast.com.cn.fastandroid.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import fastandroid.fast.com.cn.fastandroid.ProgressResponseBody;
import fastandroid.fast.com.cn.fastandroid.R;
import fastandroid.fast.com.cn.fastandroid.utils.SPUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by zzs on 2017/5/12
 */

public class UpdateVersionService extends Service {

    public static final String TAG = "UpdateVersionService";
    private NotificationManager nm;
    private Notification notification;

    //标题标识
    private int titleId = 0;
    //安装文件
    private File file;

    private String mWebServiceSite;

    private String mURLEncoder;


    @Override
    public void onCreate() {
        super.onCreate();

        file = new File("/sdcard/设计院管理.apk");


        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notification = new Notification();
        notification.icon = R.drawable.ic_launcher;
        notification.tickerText = "开始下载";
        notification.when = System.currentTimeMillis();
        notification.contentView = new RemoteViews(getPackageName(), R.layout.notifycation);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Bundle bundle = intent.getExtras();
//        String url = bundle.getString("downloadUrl");
        String androidurl = SPUtil.getString(getApplicationContext(), "androidurl", "");
//        String webURL = SPUtil.getString(getApplicationContext(), "webURL", "");


//        if (webURL.equals("")) {
//            mWebServiceSite = "http://" + getString(R.string.WebServiceSite);
//            Log.d(TAG, "mWebServiceSite: " + mWebServiceSite);
//        } else {
//            mWebServiceSite = webURL;
//        }
//
        SharedPreferences conig = getSharedPreferences("config", Context.MODE_PRIVATE);
        String webURL = conig.getString("webURL", "");
        String user_token = conig.getString("user_token", "");
        if (webURL.equals("")) {
            mWebServiceSite = "http://" + getString(R.string.WebServiceSite);
            Log.d(TAG, "mWebServiceSite: " + mWebServiceSite);
        } else {
            mWebServiceSite = webURL;
        }
        Log.d(TAG, "[URL]" + mWebServiceSite);


        //URL转码(特殊字符)
        try {
            mURLEncoder = URLEncoder.encode(mWebServiceSite + androidurl, "utf-8");
            Log.d(TAG, "mURLEncoder: " + mURLEncoder);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String loadUrl = mWebServiceSite + "Helper/Redirect?" + "echostr=" + user_token + "&uri=" + mURLEncoder;
        if (TextUtils.isEmpty(loadUrl)) {
            loadUrl = "http://www.fast.com.cn";
        }


        //新版本已经下载
//        if(file.exists() && file.getName().equals("设计院管理.apk")){
//
//            Intent intent1 =getInstallIntent(file);
//            getBaseContext().startActivity(intent1);
//        }else {
            //没有下载，则开启服务下载新版本
            try {

                Toast.makeText(getBaseContext(), "正在下载安装文件.", Toast.LENGTH_SHORT).show();

                downloadAPK(loadUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
//        }

//        nm.notify(titleId, notification);

//        http://10.0.0.2:8088/CnpcMobile/Config/设计院管理.apk
        return super.onStartCommand(intent, flags, startId);
    }

    private void downloadAPK(String url) throws IOException {

        Log.e(TAG, "downloadAPK: "+url );

        notification.contentView.setTextViewText(R.id.msg, "开始下载：");
        nm.notify(titleId, notification);

        //构建一个请求
        Request request = new Request.Builder()
                .url(url)
//                .url("http://gdown.baidu.com/data/wisegame/57a788487345e938/QQ_358.apk")//测试
//                .url("http://app.mi.com/download/456476")//测试
//                .url("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2859174087,963187950&fm=23&gp=0.jpg")//测试
                .build();
        //构建我们的进度监听器
        final ProgressResponseBody.ProgressListener listener = new ProgressResponseBody.ProgressListener() {
            @Override
            public void update(long bytesRead, long contentLength, boolean done) {
                //计算百分比并更新ProgressBar
                final long percent = (100 * bytesRead / contentLength);

                notification.contentView.setTextViewText(R.id.msg, "正在下载：设计院管理");
                // 更改文字
                notification.contentView.setTextViewText(R.id.bartext, percent + "%");
                // 更改进度条
                notification.contentView.setProgressBar(R.id.progressBar1, 100, (int) percent, false);
                // 发送消息
                nm.notify(0, notification);
//                mProgressBar.setProgress(percent);
//                Log.d("cylog","下载进度："+(100*bytesRead)/contentLength+"%");
            }
        };
        //创建一个OkHttpClient，并添加网络拦截器
        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Response response = chain.proceed(chain.request());
                        //这里将ResponseBody包装成我们的ProgressResponseBody
                        return response.newBuilder()
                                .body(new ProgressResponseBody(response.body(), listener))
                                .build();
                    }
                })
                .build();
        //发送响应
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                InputStream inputStream = response.body().byteStream();
                FileOutputStream fileOutputStream = null;
                try {
                    fileOutputStream = new FileOutputStream(file);
                    byte[] buffer = new byte[2048];
                    int len = 0;
                    while ((len = inputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, len);
                    }
                    fileOutputStream.flush();
                } catch (IOException e) {
                    Log.i("wangshu", "IOException");
                    e.printStackTrace();
                }

                Log.d("wangshu", "文件下载成功");


                // 更改文字
                notification.contentView.setTextViewText(R.id.msg, "下载完成!");
//                notification.contentView.setViewVisibility(R.id.btnStartStop, View.GONE);
//                notification.contentView.setViewVisibility(R.id.btnCancel,View.GONE);
                // 发送消息
                nm.notify(0, notification);
                stopSelf();

//                notification.
//                notification.flags = Notification.FLAG_AUTO_CANCEL;//点击通知栏之后 消失
                nm.cancel(0);
                //收起通知栏
//                collapseStatusBar(UpdateVersionService.this);
                //自动安装新版本
                Intent installIntent = getInstallIntent(file);
                startActivity(installIntent);
                //关闭旧版本的应用程序的进程
                android.os.Process.killProcess(android.os.Process.myPid());

            }
        });
    }


    /**
     * 收起通知栏
     *
     * @param context
     */
//    public void collapseStatusBar(Context context) {
//        try {
//            Object statusBarManager = context.getSystemService("statusbar");
//            Method collapse;
//            if (Build.VERSION.SDK_INT <= 16) {
//                collapse = statusBarManager.getClass().getMethod("collapse");
//            } else {
//                collapse = statusBarManager.getClass().getMethod("collapsePanels");
//            }
//            collapse.invoke(statusBarManager);
//        } catch (Exception localException) {
//            localException.printStackTrace();
//        }
//    }


    /**
     * 得到安装的intent
     *
     * @param apkFile
     * @return
     */
    public Intent getInstallIntent(File apkFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setDataAndType(Uri.fromFile(new File(apkFile.getAbsolutePath())),
                "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        return intent;
    }


    /**
     * 获取SD卡的根目录
     *
     * @return null：不存在SD卡
     */
    public static File getRootDirectory() {
        return isAvailable() ? Environment.getExternalStorageDirectory() : null;
    }


    /**
     * SD卡是否可用
     *
     * @return 只有当SD卡已经安装并且准备好了才返回true
     */
    public static boolean isAvailable() {
        return getState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取SD卡的状态
     *
     * @return
     */
    public static String getState() {
        return Environment.getExternalStorageState();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
