package fastandroid.fast.com.cn.fastandroid.fragment;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import cn.jpush.android.api.JPushInterface;
import fastandroid.fast.com.cn.fastandroid.R;
import fastandroid.fast.com.cn.fastandroid.activity.AboutActivity;
import fastandroid.fast.com.cn.fastandroid.activity.LoginActivity;
import fastandroid.fast.com.cn.fastandroid.db.CacheDao;
import fastandroid.fast.com.cn.fastandroid.db.CacheService;
import fastandroid.fast.com.cn.fastandroid.db.SettingInfo;
import fastandroid.fast.com.cn.fastandroid.ui.SwitchButton;
import fastandroid.fast.com.cn.fastandroid.utils.UpDateVersionUtil;
import okhttp3.HttpUrl;


public class SettingFragment extends Fragment {

    private View mFragmentView;
    private TextView mMID_title;

    private SwitchButton sbPush;
    private View layoutUserInfo;
    private View layoutAbout;
    private View layoutUpdata;
    private View layoutClearCache;
    private View layoutDataAddConfig;
    private Button btnLoginOut;


    private String mDeviceID;
    private boolean mPushStart;
    private Context mContext;

    private SharedPreferences mPref;
    public static final String TAG = "SettingFragment";

    private NotificationManager nm;
    private Notification notification;

    private File file;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        System.out.println("SettingFragment--onCreate");
        super.onCreate(savedInstanceState);

        file = new File("/sdcard/设计院管理.apk");


        initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        System.out.println("SettingFragment--onCreateView");

        if (null == mFragmentView) {
            mFragmentView = inflater.inflate(R.layout.fragment_setting, container, false);

            mMID_title = (TextView) mFragmentView.findViewById(R.id.midle_title);
            mMID_title.setText("设置");

            initView();
        }

        ViewGroup parent = (ViewGroup) mFragmentView.getParent();
        if (parent != null) {
            parent.removeView(mFragmentView);
        }

        return mFragmentView;
    }

    /*
     * 初始化数值，以确定后台控件的设置属性
     */
    private void initData() {
        mDeviceID = "FastMQTT";

        mContext = this.getActivity();

        SettingInfo si = new SettingInfo(mContext);
        String PushMsg = si.GetSettingValueByKey("PushMsg");
        if (PushMsg.equals("NO")) {
            mPushStart = false;
//            SwitchSushService(STOP);
            JPushInterface.stopPush(getContext());

        } else {
            mPushStart = true;
            JPushInterface.resumePush(getContext());
//            SwitchSushService(START);
        }

        mContext = this.getActivity();

        mPref = mContext.getSharedPreferences("config", Context.MODE_PRIVATE);
    }

    /*
     * 初始化视图，绑定控件事件
     */
    private void initView() {
        sbPush = (SwitchButton) mFragmentView.findViewById(R.id.switch_push);
        btnLoginOut = ((Button) mFragmentView.findViewById(R.id.btn_login_out));
        layoutAbout = mFragmentView.findViewById(R.id.layout_about);
        layoutUpdata = mFragmentView.findViewById(R.id.layout_updata);

        layoutClearCache = mFragmentView.findViewById(R.id.layout_clear_cache);
        layoutDataAddConfig = mFragmentView.findViewById(R.id.layout_server_config);

        sbPush.setChecked(mPushStart);
        sbPush.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (sbPush.isChecked()) {
                    JPushInterface.resumePush(getContext());
                } else {
                    JPushInterface.stopPush(getContext());
                }
                //保存状态
                SettingInfo si = new SettingInfo(buttonView.getContext());
                si.SetSettingValueByKey("PushMsg", sbPush.isChecked() ? "Yes" : "NO");

            }
        });

        btnLoginOut.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new DoWorkWithNetWork().execute();
            }
        });



        //submenu 关于
        layoutAbout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //Item点击代码这里添加
                Intent intent = new Intent(mContext, AboutActivity.class);
                String versionName = getVersionName(mContext);
                intent.putExtra("versionName", versionName);
                startActivity(intent);
            }
        });

        //submenu 检查更新
//        layoutUpdata.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SimpleDialogFragment.createBuilder(mContext, getChildFragmentManager())
//                        .setTitle("软件更新提示")
//                        .setMessage("当前版本已是最新版本!")
//                        .show();
//            }
//        });

        layoutUpdata.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                UpDateVersionUtil.UpDateVersion(mContext);//版本更新
            }
        });

        layoutClearCache.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                CacheService mCS = new CacheDao(mContext);
                if (mCS.ClearCache()) {
                    Toast.makeText(
                            mContext,
                            "缓存数据已清除",
                            Toast.LENGTH_LONG
                    ).show();
                } else {
                    Toast.makeText(
                            mContext,
                            "缓存数据清除失败",
                            Toast.LENGTH_LONG
                    ).show();
                }

            }
        });

        //submenu 配置数据源
        layoutDataAddConfig.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final android.widget.EditText et = new android.widget.EditText(mContext);

                //回显用户输入的数据源地址
                String userInput = mPref.getString("userInput", getString(R.string.WebServiceSite));
                et.setText(userInput);
                et.setSelection(userInput.length());

                new AlertDialog.Builder(mContext)
                        .setTitle("请输入数据源地址，范例：10.0.0.92/CnpcMobile/")
                        .setView(et)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                String IPandHost = et.getText().toString();
                                String baseURL = "http://" + IPandHost;
//                                String baseURL = "http://" + IPandHost + "/CnpcMobile/";

                                HttpUrl parse = HttpUrl.parse(baseURL);//okhttp解析url是否合法
                                if (parse == null) {
                                    Toast.makeText(mContext, "数据源输入有误!", Toast.LENGTH_SHORT).show();
                                } else {

                                    SharedPreferences.Editor editor = mPref.edit();
                                    editor.putString("webURL", baseURL);
                                    editor.putString("userInput", IPandHost);
                                    editor.commit();

                                    Toast.makeText(mContext, "数据成功写入！当前的数据源为" + baseURL, Toast.LENGTH_SHORT).show();
                                }

                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();

            }
        });
    }

    /*
     * 设置是否开启推送服务
     **/
//    private void SwitchSushService(boolean isStart) {
//        if (isStart) {
//            SharedPreferences.Editor editor = getActivity().getSharedPreferences(PushService.TAG, Context.MODE_PRIVATE).edit();
//            editor.putString(PushService.PREF_DEVICE_ID, mDeviceID);
//            editor.commit();
//            PushService.actionStart(this.getActivity().getApplicationContext());
//        } else {
//            PushService.actionStop(this.getActivity().getApplicationContext());
//        }
//    }

    //异步后台交互数据
    private class DoWorkWithNetWork extends AsyncTask<Void, Void, Boolean> {
        //后台处理部分  
        @Override
        protected Boolean doInBackground(Void... params) {
            //后台登出操作
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return true;
        }

        protected void onPostExecute(Boolean state) {


            //跳转到登陆界面
            Intent intent = new Intent(mContext, LoginActivity.class);
            startActivity(intent);

//            mPref=mContext.getSharedPreferences("config",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = mPref.edit();
            editor.putBoolean("isFirstRun", false);
            editor.commit();

        }
    }


    //版本检查，有最新的则更新
    private void CheckAppVersion() {
        PackageManager manager = this.getActivity().getPackageManager();
        PackageInfo info;
        try {
            info = manager.getPackageInfo(this.getActivity().getPackageName(), 0);
        } catch (NameNotFoundException e) {
//            SimpleDialogFragment.createBuilder(mContext, getChildFragmentManager())
//                    .setTitle("软件更新提示")
//                    .setMessage("错误：无法获取当前版本号")
//                    .show();
            e.printStackTrace();
            return;
        }
        String CurVersion = info.versionName;
    }


    public String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

//    private void downAsynFile() {
//        OkHttpClient mOkHttpClient = new OkHttpClient();
//        String url = "http://app.mi.com/download/294";
//        Request request = new Request.Builder().url(url).build();
//        mOkHttpClient.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//            }
//
//
//            @Override
//            public void onResponse(Call call, Response response) {
//                InputStream inputStream = response.body().byteStream();
//                FileOutputStream fileOutputStream = null;
//                try {
//                    fileOutputStream = new FileOutputStream(new File("/sdcard/FSTfast.apk"));
//                    byte[] buffer = new byte[2048];
//                    int len = 0;
//                    while ((len = inputStream.read(buffer)) != -1) {
//                        fileOutputStream.write(buffer, 0, len);
//                    }
//                    fileOutputStream.flush();
//                } catch (IOException e) {
//                    Log.i("wangshu", "IOException");
//                    e.printStackTrace();
//                }
//
//                Log.d("wangshu", "文件下载成功");
//            }
//        });
//    }


}
