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
import fastandroid.fast.com.cn.fastandroid.R;
import fastandroid.fast.com.cn.fastandroid.utils.SPUtil;


public class AppstartActivity extends Activity {
    private Set<String> tagSet = new LinkedHashSet<String>();
    public static final String TAG = "AppstartActivity";
    public SharedPreferences mPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appstart);


        JPushInterface.setDebugMode(true);//设置JPush调试模式
        JPushInterface.init(this);//初始化JPush

        mPref = getSharedPreferences("config", Context.MODE_PRIVATE);
        SPUtil.setBoolean(getBaseContext(), "UpDateLater", false);//每次打开提示用户更新

        UpdateVersion();

//        tagSet.add("点点");
//        tagSet.add("金三胖");
//        Log.d("Tags", tagSet.toString());
//        JPushInterface.setAliasAndTags(this, null, tagSet, mTagsCallback);


//        加入延迟，3秒后进入主界面
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();

                //如果 是第一次运行则进入登录界面,否则进入主界面
                if (mPref.getBoolean("isFirstRun", false)) {
                    intent.setClass(AppstartActivity.this, MainActivity.class);
                } else {
                    intent.setClass(AppstartActivity.this, LoginActivity.class);
                }
                startActivity(intent);
                AppstartActivity.this.finish();
            }
        }, 3000);
    }

    private void UpdateVersion() {

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
