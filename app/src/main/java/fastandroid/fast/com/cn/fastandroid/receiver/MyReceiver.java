package fastandroid.fast.com.cn.fastandroid.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;
import fastandroid.fast.com.cn.fastandroid.R;
import fastandroid.fast.com.cn.fastandroid.activity.MainActivity;
import fastandroid.fast.com.cn.fastandroid.activity.NewsNoticeActivity;
import fastandroid.fast.com.cn.fastandroid.adapter.HomeAppAdapter;
import fastandroid.fast.com.cn.fastandroid.adapter.NewsAdapter;
import fastandroid.fast.com.cn.fastandroid.adapter.TaskAdapter;
import fastandroid.fast.com.cn.fastandroid.bean.News;
import fastandroid.fast.com.cn.fastandroid.db.DBHelper;

import static fastandroid.fast.com.cn.fastandroid.activity.PushNewsNoticeActivity.newsAdapter;
import static fastandroid.fast.com.cn.fastandroid.activity.PushTasklistActivity.taskAdapter;
import static fastandroid.fast.com.cn.fastandroid.fragment.HomeFragmenrt.homeAppAdapter;

/**
 * Created by zzs on 2017/3/14
 */

public class MyReceiver extends BroadcastReceiver {
    public static final String TAG = "MyReceiver";
    private String appid;

    @Override
    public void onReceive(Context context, Intent intent) {


        Bundle bundle = intent.getExtras();

        ContentValues contentValues = new ContentValues();


        Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
//            processCustomMessage(context, bundle);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setDefaults(Notification.DEFAULT_SOUND
                    | Notification.DEFAULT_VIBRATE
                    | Notification.DEFAULT_LIGHTS);// 设置为铃声、震动、呼吸灯闪
//            builder.setTicker("嘿嘿嘿嘿嘿嘿");//设置收到通知时,顶部提示文字

            builder.setAutoCancel(true);//设置点击一次后消失
            builder.setSmallIcon(R.drawable.ic_app);
            Resources resources = context.getResources();
            builder.setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_app));
            builder.setContentTitle("这是自定义消息标题");
            builder.setContentText("这是自定义消息内容");

//            Intent webIntent = new Intent(Intent.ACTION_VIEW);
//			webIntent.setData(Uri.parse("http://www.v2ex.com"));

            Intent i = new Intent(context, NewsNoticeActivity.class);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, i, 0);
            builder.setContentIntent(pendingIntent);

            Notification notification = builder.build();
            NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            nm.notify(1, notification);

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

            //获取当前时间并和接收到的消息一起存入数据库
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm     ");
            Date curDate = new Date(System.currentTimeMillis());//获取当前时间
            String pushTime = formatter.format(curDate);
            Log.e(TAG, "onReceive: " + pushTime);

            News news = new News();//用于把新接收到的通知实时显示到新闻列表
            news.setRead(false);
            news.setTime(pushTime);
            contentValues.put("time", pushTime);
//
            DBHelper dbHelper = new DBHelper(context, context.getString(R.string.DB_NEWS));//获取数据库帮组对象
            //将接收到的通知存入本地数据库
            for (String key : bundle.keySet()) {
                if (key.equals(JPushInterface.EXTRA_NOTIFICATION_TITLE)) {
                    Log.e(TAG, "EXTRA_NOTIFICATION_TITLE: " + bundle.get(key));
                    String value = bundle.get(key).toString();
                    contentValues.put("title", value);
                    news.setTitle(value);
                } else if (key.equals(JPushInterface.EXTRA_ALERT)) {
                    Log.e(TAG, "EXTRA_ALERT: " + bundle.get(key));
                    String value = bundle.get(key).toString();
                    contentValues.put("content", value);
                    news.setContent(value);
                } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                    try {
                        JSONObject jsonObject = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                        String urll = jsonObject.getString("url");
                        Log.e(TAG, "推送过来的详情url: " + urll);
                        contentValues.put("url", urll);
                        news.setUrl(urll);
                        appid = jsonObject.getString("appid");
                        Log.e(TAG, "推送过来的appid: " + appid);
                        contentValues.put("appid", appid);
                        news.setAppid(appid);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            contentValues.put("isRead", "1");//收到新通知存入数据库时加isRead标签:1代表未读,0代表已读
            dbHelper.insert(contentValues, context.getString(R.string.TABLE_NEWS));
            dbHelper.close();
            if (appid == null || appid.equals("") || appid.equals("1")) {
                if (newsAdapter == null) {
                    newsAdapter = new NewsAdapter();
                }
                newsAdapter.add(news);//通知adapter实时更新listview
            } else if (appid.equals("2")) {
                if (taskAdapter == null) {
                    taskAdapter = new TaskAdapter();
                }
                taskAdapter.add(news);//通知adapter实时更新listview
            }

            if (homeAppAdapter==null){
                homeAppAdapter=new HomeAppAdapter();
            }
            homeAppAdapter.notifyDataSetChanged();


        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
            String type = bundle.getString(JPushInterface.EXTRA_EXTRA);
            try {
                JSONObject jsonObject = new JSONObject(type);
//                String key = jsonObject.getString("key");//根据推送的不同消息来实现跳转不同的页面
                //打开自定义的Activity
                Intent i = new Intent(context, MainActivity.class);
                i.putExtras(bundle);
                //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..


        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }


    }

    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    //send msg to MainActivity
//    private void processCustomMessage(Context context, Bundle bundle) {
//        if (MainActivity.isForeground) {
//            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//            Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
//            msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
//            if (!ExampleUtil.isEmpty(extras)) {
//                try {
//                    JSONObject extraJson = new JSONObject(extras);
//                    if (extraJson.length() > 0) {
//                        msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
//                    }
//                } catch (JSONException e) {
//
//                }
//
//            }
//            context.sendBroadcast(msgIntent);
//        }
//    }


}
