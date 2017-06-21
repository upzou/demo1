package fastandroid.fast.com.cn.fastandroid.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;

import java.io.IOException;

import cn.jpush.android.api.JPushInterface;
import fastandroid.fast.com.cn.fastandroid.R;
import fastandroid.fast.com.cn.fastandroid.bean.ResponseLogin;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity {
    public static final String TOKEN = "9413E8CD76924AAD8C290DF01656F148";//token值
    private static final String PASSWORD_STRING = "12901256789012347344565678890123";//密钥
    public static final MediaType REQUEST_FORMAT = MediaType.parse("application/json; charset=utf-8");


    private String TAG = "LoginActivity";
    private Context mContext;

    private TextView mMID_title;
    private ImageView mRightImage;
    private View mRightView;
    private EditText mUser; // 帐号编辑框
    private EditText mPassword; // 密码编辑框
    //    private CircularProgressButton btnLogin;
    private Button btnLogin;

    private String mWebServiceSite;

    private long mExitTime;
    public SharedPreferences mPref;
    private Call call;

    private ResponseLogin responseLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        String encrypt = Aes.encrypt("admin|9413E8CD76924AAD8C290DF01656F148|123", PASSWORD_STRING);
//        Log.e(TAG, "encrypt: "+ encrypt);

        mPref = getSharedPreferences("config", Context.MODE_PRIVATE);

        String webURL = mPref.getString("webURL", "");
        if (webURL.equals("")) {
            mWebServiceSite = "http://" + getString(R.string.WebServiceSite);
            Log.d(TAG, "mWebServiceSite: " + mWebServiceSite);
        } else {
            mWebServiceSite = webURL;
        }
        Log.d(TAG, "[URL]" + mWebServiceSite);
        initView();

    }

    private void initView() {
        mContext = this;

        mMID_title = (TextView) findViewById(R.id.midle_title);
        mUser = (EditText) findViewById(R.id.login_user_edit);
        mPassword = (EditText) findViewById(R.id.login_passwd_edit);

        //如果有登陆记录则在编辑框回显
        String spUn = mPref.getString("un", "");
        String spPwd = mPref.getString("pwd", "");
        mUser.setText(spUn);
        mUser.setSelection(spUn.length());//设置光标在末尾位置
        mPassword.setText(spPwd);

        mMID_title.setText("登陆");

        mRightImage = (ImageView) findViewById(R.id.right_image);
        mRightImage.setImageResource(R.drawable.set);

        mRightView = findViewById(R.id.view_right);
        mRightView.setOnClickListener(new View.OnClickListener() {
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
                                    Toast.makeText(mContext, "数据输入有误,写入失败!", Toast.LENGTH_SHORT).show();
                                } else {

                                    SharedPreferences.Editor editor = mPref.edit();
                                    editor.putString("webURL", baseURL);
                                    editor.putString("userInput", IPandHost);
                                    editor.commit();
                                    //同步修改变量
                                    mWebServiceSite = baseURL;

                                    Toast.makeText(mContext, "数据成功写入！当前的数据源为" + baseURL, Toast.LENGTH_SHORT).show();
                                }

                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });

        btnLogin = (Button) findViewById(R.id.login_btn);
        btnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                doPost(v);
            }
        });
    }

    /**
     * 通过POST方式跟后台交互
     */
    public void doPost(View view) {
        //获得OkHttpClient对象
        final OkHttpClient mOkHttpClient = new OkHttpClient();
//                .Builder()
//                .connectTimeout(5, TimeUnit.SECONDS)
//                .writeTimeout(5, TimeUnit.SECONDS)
//                .readTimeout(30, TimeUnit.SECONDS)
//                .build();

        final String un = mUser.getText().toString().trim();//获取用户输入的账号
        final String pwd = mPassword.getText().toString().trim();//获取用户输入的密码

        final String registrationID = JPushInterface.getRegistrationID(this);//获取极光推送注册ID
        Log.d(TAG, "registrationID: " + registrationID);


        //账号和密码都不能为空
        if (TextUtils.isEmpty(un)) {
            Toast.makeText(this, "账号不能为空!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(this, "密码不能为空!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(registrationID)) {
            Toast.makeText(this, "推送注册失败,请重启软件!", Toast.LENGTH_SHORT).show();
            return;
        }

//            //最初的测试代码,HttpUrl
//            RequestBody body = new FormBody.Builder().build();
//            HttpUrl.Builder urlBuilder = HttpUrl.parse("http://10.0.0.2:8088/Cnpc/api/Account")//http://10.0.0.2:8088/CnpcMobile/Help
//                    .newBuilder()
//                    .addQueryParameter("un", un)//添加查询参数
//                    .addQueryParameter("pwd", pwd)
////                    .addQueryParameter("device", pwd)
//                    .addQueryParameter("token", TOKEN);
//
//            String url = urlBuilder.build().toString();
//            Request request = new Request.Builder().post(body).url(url).build();
//
//
//            //JSON方式提交
//            String requestJson = "{" +
//                    "\"un\": \"" + un + "\"," +
//                    "\"pwd\": \"" + pwd + "\"," +
//                    "\"device\": \"sample string 3\"," +
//                    "\"token\": \"" + TOKEN + "\"" +
//                    "}";
//
//            Log.d(TAG, "doPost: " + requestJson);
//            RequestBody body = RequestBody.create(REQUEST_FORMAT, requestJson);

        //URL转码(特殊字符)
//        try {
//            String text1 = URLEncoder.encode("http://10.0.0.2/News/List/1", "utf-8");
//            Log.d(TAG, "URLEncoder: "+text1);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }

        okhttp3.RequestBody body = new FormBody.Builder() //键值对方式提交
                .add("un", un)
                .add("pwd", pwd)
                .add("device", registrationID)
                .add("token", TOKEN)
                .build();

        String requestUrl = mWebServiceSite + "api/Account/Login";

        HttpUrl parse = HttpUrl.parse(requestUrl);//okhttp解析url是否合法
        if (parse == null) {
            Toast.makeText(getApplicationContext(), "数据源地址有误!", Toast.LENGTH_SHORT).show();
            return;
        }

        Request request = new Request.Builder()
                .url(requestUrl)
                .post(body)
                .build();

        Log.e(TAG, "登录请求地址:" + requestUrl);

        call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
//                    int serversLoadTimes = 0;//如果超时并未超过指定次数，则重新连接  && serversLoadTimes < 2
//                    if (e.getCause().equals(SocketTimeoutException.class) ) {
////                        serversLoadTimes++;
////                        mOkHttpClient.newCall(call.request()).enqueue(this);
//                        Toast.makeText(getApplicationContext(), "网络连接超时", Toast.LENGTH_SHORT).show();
//
//                    } else {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "请求网络失败!", Toast.LENGTH_SHORT).show();
                    }
                });
//                    }
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                final String string = response.body().string();
                Log.d("response: ", string);
                //将返回的JSON字符串解析成ResponseLogin对象

                try {
                    responseLogin = JSON.parseObject(string, ResponseLogin.class);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (responseLogin.getCode().equals("0")) {

                                SharedPreferences.Editor editor = mPref.edit();
                                //第一次登陆成功后将状态记录下来,第二次启动程序时将跳过登陆界面
                                editor.putBoolean("isFirstRun", true);

                                editor.putString("un", un);
                                editor.putString("pwd", pwd);
                                editor.putString("device", registrationID);
                                editor.putString("token", TOKEN);
                                String user_token = responseLogin.getUser_token();
                                Log.e(TAG, "user_token: "+user_token );
                                editor.putString("user_token", user_token);
                                editor.putString("user_code", responseLogin.getUser_code());
                                editor.commit();

//                                    Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                LoginActivity.this.startActivity(intent);
                                LoginActivity.this.finish();
                            } else {
                                Toast.makeText(getApplicationContext(), responseLogin.getErrmsg(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (JSONException e) {
                    MainActivity.showToast("服务器返回结果异常!", getApplicationContext());
                    Log.e(TAG, "FASTJSON: " + e);
                    e.printStackTrace();
                }
            }
        });
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();

            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }






 /*       String pwdMD5 = MD5("123");
        String tokenMD5 = MD5(TOKEN);
        Log.d(TAG, "unMD5: " + unMD5 + " \n pwdMD5:" + pwdMD5 + " \n tokenMD5:" + tokenMD5);



    // MD5加密，32位
    public static String MD5(String content) {
        byte[] hash;
        try {
            // 得到一个信息摘要器
            hash = MessageDigest.getInstance("MD5").digest(content.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("NoSuchAlgorithmException", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UnsupportedEncodingException", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {

            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }
*/


}
