package fastandroid.fast.com.cn.fastandroid.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import java.util.LinkedHashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import fastandroid.fast.com.cn.fastandroid.Constant;
import fastandroid.fast.com.cn.fastandroid.R;
import fastandroid.fast.com.cn.fastandroid.utils.SPUtil;


public class AppStartActivity extends Activity {
    private Set<String> tagSet = new LinkedHashSet<String>();// TODO: 2017/9/14 根据标签,别名推送
    public final String TAG = "AppStartActivity";
    public SharedPreferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appstart);

        JPushInterface.setDebugMode(true);//设置JPush调试模式
        JPushInterface.init(this);//初始化JPush

        mPref = getSharedPreferences(Constant.SP_NAME, Context.MODE_PRIVATE);
        SPUtil.setBoolean(getBaseContext(), "UpDateLater", false);//每次启动软件时提示用户更新

//        tagSet.add("点点");
//        tagSet.add("金三胖");
//        Log.d("Tags", tagSet.toString());
//        JPushInterface.setAliasAndTags(this, null, tagSet, mTagsCallback);


//        加入延迟，3秒后跳转
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();

                //如果 是第一次运行则进入登录界面,否则进入主界面
                if (mPref.getBoolean("isFirstRun", false)) {
                    intent.setClass(AppStartActivity.this, MainActivity.class);
                } else {
                    intent.setClass(AppStartActivity.this, LoginActivity.class);
                }
                startActivity(intent);
                AppStartActivity.this.finish();
            }
        }, 3000);
    }


    //设置标签,别名
//    private final TagAliasCallback mTagsCallback = new TagAliasCallback() {
//
//        @Override
//        public void gotResult(int code, String alias, Set<String> tags) {
//            String logs;
//            switch (code) {
//                case 0:
//                    logs = "Set tag and alias success";
//                    Log.i(TAG, logs);
//                    break;
//
//                case 6002:
//                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
//                    Log.i(TAG, logs);
//                    if (isConnected(getApplicationContext())) {
//                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_TAGS, tags), 1000 * 60);
//                    } else {
//                        Log.i(TAG, "No network");
//                    }
//                    break;
//
//                default:
//                    logs = "Failed with errorCode = " + code;
//                    Log.e(TAG, logs);
//            }
//
//            showToast(logs, getApplicationContext());
//        }
//
//    };
//
//
//    private static final int MSG_SET_ALIAS = 1001;
//    private static final int MSG_SET_TAGS = 1002;
//
//    private final Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(android.os.Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case MSG_SET_ALIAS:
//                    Log.d(TAG, "Set alias in handler.");
//                    JPushInterface.setAliasAndTags(getApplicationContext(), (String) msg.obj, null, mAliasCallback);
//                    break;
//
//                case MSG_SET_TAGS:
//                    Log.d(TAG, "Set tags in handler.");
//                    JPushInterface.setAliasAndTags(getApplicationContext(), null, (Set<String>) msg.obj, mTagsCallback);
//                    break;
//
//                default:
//                    Log.i(TAG, "Unhandled msg - " + msg.what);
//            }
//        }
//    };
//
//    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
//
//        @Override
//        public void gotResult(int code, String alias, Set<String> tags) {
//            String logs;
//            switch (code) {
//                case 0:
//                    logs = "Set tag and alias success";
//                    Log.i(TAG, logs);
//                    break;
//
//                case 6002:
//                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
//                    Log.i(TAG, logs);
//                    if (isConnected(getApplicationContext())) {
//                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
//                    } else {
//                        Log.i(TAG, "No network");
//                    }
//                    break;
//
//                default:
//                    logs = "Failed with errorCode = " + code;
//                    Log.e(TAG, logs);
//            }
//
//            showToast(logs, getApplicationContext());
//        }
//
//    };

}
