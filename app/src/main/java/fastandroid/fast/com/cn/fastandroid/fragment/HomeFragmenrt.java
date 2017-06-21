package fastandroid.fast.com.cn.fastandroid.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import fastandroid.fast.com.cn.fastandroid.R;
import fastandroid.fast.com.cn.fastandroid.activity.PushNewsNoticeActivity;
import fastandroid.fast.com.cn.fastandroid.activity.PushTasklistActivity;
import fastandroid.fast.com.cn.fastandroid.activity.UpdateVersionService;
import fastandroid.fast.com.cn.fastandroid.adapter.HomeAppAdapter;
import fastandroid.fast.com.cn.fastandroid.bean.MenuDetail;
import fastandroid.fast.com.cn.fastandroid.bean.ResponseHome;
import fastandroid.fast.com.cn.fastandroid.utils.NetWorkUtil;
import fastandroid.fast.com.cn.fastandroid.utils.SPUtil;
import fastandroid.fast.com.cn.fastandroid.utils.ServiceUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static fastandroid.fast.com.cn.fastandroid.activity.LoginActivity.TOKEN;
import static fastandroid.fast.com.cn.fastandroid.activity.MainActivity.showToast;

/**
 * Created by zzs on 2017/3/6
 */

public class HomeFragmenrt extends Fragment {
    private View mHomeFragmenrtView;
    private Context mContext;
    public ResponseHome responseResult;//接收返回结果的对象
    public static HomeAppAdapter homeAppAdapter;//首页APP列表listview适配器
    public final String TAG = "MainActivity";

    private String mWebServiceSite;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
//        initData();
//        Log.e(TAG, "onCreate: " + "onCreate加载数据");
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        if (isEmpty(responseResult)) {
//            Log.e(TAG, "onCreateView: " + "onCreateView加载数据");
//        }

        initData();
//        initTestData();

        mHomeFragmenrtView = inflater.inflate(R.layout.fragment_home, container, false);
        ListView lv_home_app = (ListView) mHomeFragmenrtView.findViewById(R.id.lv_home_app);
        LinearLayout ll_homeapp_empty = (LinearLayout) mHomeFragmenrtView.findViewById(R.id.ll_homeapp_empty);
        TextView midle_title = (TextView) mHomeFragmenrtView.findViewById(R.id.midle_title);
        midle_title.setText("首页");



        try {
            Thread.sleep(500);
            lv_home_app.setAdapter(homeAppAdapter);
            lv_home_app.setEmptyView(ll_homeapp_empty);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        lv_home_app.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    Intent intent = new Intent(getActivity(), PushNewsNoticeActivity.class);
                    List<MenuDetail> names = responseResult.getAppList().get(i).getMenu();
                    //将MenuDetail对象传到MenuActivity
                    intent.putExtra("menu", (Serializable) names);
                    startActivity(intent);
                }
                if (i == 1) {
                    Intent intent = new Intent();
                    List<MenuDetail> names = responseResult.getAppList().get(i).getMenu();
                    //将appname传到MenuActivity,用来设置mMID_title的名称
                    intent.putExtra("appname", responseResult.getAppList().get(i).getName());
                    //将MenuDetail对象传到MenuActivity
                    intent.putExtra("menu", (Serializable) names);
                    intent.setClass(getActivity(), PushTasklistActivity.class);
                    startActivity(intent);
                }
            }
        });


        boolean UpDateLater = SPUtil.getBoolean(mContext, "UpDateLater", false);
        if (!UpDateLater) {
            UpDateVersion(getContext());
        }

        return mHomeFragmenrtView;

    }

    private void initTestData() {
        //将返回结果解析成ResponseHome对象
        responseResult = JSON.parseObject("{\"version\":1000,\"applist\":[{\"appid\":1,\"icon\"" +
                ":\"http://www.easyicon.net/api/resizeApi.php?id=1063843&size=128\",\"name\":\"" +
                "院内资讯\",\"menu\":[{\"name\":\"新闻\",\"url\":\"/News/List?code=1&classgroup=1\"}" +
                ",{\"name\":\"通知公告\",\"url\":\"/News/List?code=1&classgroup=5,6\"}]},{\"appid\":2," +
                "\"icon\":\"http://www.easyicon.net/api/resizeApi.php?id=1063808&size=128\",\"name\"" +
                ":\"待办事项\",\"menu\":[{\"name\":\"待办事项\",\"url\":\"/Task/List\"}]}]}", ResponseHome.class);
        //初始化首页APP列表listview适配器
        homeAppAdapter = new HomeAppAdapter(getActivity(), responseResult.getAppList());
    }

    private void initData() {
        //获得OkHttpClient对象
        final OkHttpClient mOkHttpClient = new OkHttpClient();
//                .Builder()
//                .connectTimeout(10, TimeUnit.SECONDS)
//                .writeTimeout(10, TimeUnit.SECONDS)
//                .readTimeout(30, TimeUnit.SECONDS)
//                .build();

//        Request.Builder body=new Request.Builder().url("http://10.0.0.2:8088/CnpcMobile/api/App/Config");
//        RequestBody body = new FormBody.Builder().build();
//        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://10.0.0.2:8088/CnpcMobile/api/App/Config")
//                .newBuilder()
//                .addQueryParameter("user_code", String.valueOf(USER_CODE));
//        String url = urlBuilder.build().toString();
//        Request request = new Request.Builder().post(body).url(url).build();

        SharedPreferences sp = getActivity().getSharedPreferences("config", Context.MODE_PRIVATE);
        String user_code = sp.getString("user_code", "");
        String user_token = sp.getString("user_token", "");
        String webURL = sp.getString("webURL", "");
        if (webURL.equals("")) {
            mWebServiceSite = "http://" + getString(R.string.WebServiceSite);
            Log.d(TAG, "mWebServiceSite: " + mWebServiceSite);
        } else {
            mWebServiceSite = webURL;
        }

        HttpUrl parse = HttpUrl.parse(mWebServiceSite + "api/App/Config");//okhttp解析url是否合法
        if (parse == null) {
            Toast.makeText(getActivity(), "数据源地址有误!", Toast.LENGTH_SHORT).show();
            return;
        }

        HttpUrl.Builder urlBuilder = HttpUrl.parse(mWebServiceSite + "api/App/Config")
                .newBuilder()
                .addQueryParameter("user_code", user_code)//添加查询参数
                .addQueryParameter("token", TOKEN);

        String url = urlBuilder.build().toString();

        Request request = new Request.Builder().url(url).build();

        Call call = mOkHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(getActivity(), "请求网络失败", Toast.LENGTH_SHORT).show();
//                    }
//                });
                showToast("请求网络失败", getActivity());

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String responseHome = response.body().string();
                Log.e("APPListResponse:", responseHome);
                try {
                    //将返回结果解析成ResponseHome对象
                    responseResult = JSON.parseObject(responseHome, ResponseHome.class);

                    //将版本信息存储到sp
                    String androidver = responseResult.getAppver().getAndroidver();
                    String androidurl = responseResult.getAppver().getAndroidurl();
                    SPUtil.setString(getActivity(), "androidver", androidver);
                    SPUtil.setString(getActivity(), "androidurl", androidurl);

                    //初始化首页APP列表listview适配器
                    homeAppAdapter = new HomeAppAdapter(getActivity(), responseResult.getAppList());
                } catch (Exception e) {
                    showToast("服务器返回结果异常", getActivity());
                    e.printStackTrace();
                }
            }
        });
    }


    //判断对象是否为空
    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if ((obj instanceof List)) {
            return ((List) obj).size() == 0;
        }
        if ((obj instanceof String)) {
            return ((String) obj).trim().equals("");
        }
        return false;
    }

    public  void UpDateVersion(final Context mContext) {

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


}
