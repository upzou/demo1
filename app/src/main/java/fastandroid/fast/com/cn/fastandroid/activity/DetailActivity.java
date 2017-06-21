package fastandroid.fast.com.cn.fastandroid.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import fastandroid.fast.com.cn.fastandroid.R;


/**
 * Created by zzs on 2017/3/8
 */
public class DetailActivity extends Activity {

    private String mURLEncoder;
    private static final String TAG = "DetailActivity";
    private String mWebServiceSite;
    private static final String PASSWORD_STRING = "12901256789012347344565678890123";//密钥


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);

//        String encrypt = Aes.encrypt("admin|9413E8CD76924AAD8C290DF01656F148|123", PASSWORD_STRING);
//        Log.e(TAG, "encrypt: " + encrypt);

        Bundle extras = getIntent().getExtras();
        String detail_url = extras.getString("detail_url");

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
            mURLEncoder = URLEncoder.encode(mWebServiceSite + detail_url, "utf-8");
            Log.d(TAG, "mURLEncoder: " + mURLEncoder);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        detail_url = mWebServiceSite + "Helper/Redirect?" + "echostr=" + user_token + "&uri=" + mURLEncoder;
        if (TextUtils.isEmpty(detail_url)) {
            detail_url = "http://www.fast.com.cn";
        }
        Log.e(TAG, "detail_url: "+detail_url);

        WebView webview_detail = (WebView) findViewById(R.id.webview_detail);
        final ProgressBar pb = (ProgressBar) findViewById(R.id.webv_progressBar);

        TextView midle_title = (TextView) findViewById(R.id.midle_title);
        midle_title.setText("详情");
        ImageView left_image = (ImageView) findViewById(R.id.left_image);
        LinearLayout view_left = (LinearLayout) findViewById(R.id.view_left);
        left_image.setImageResource(R.drawable.angle_left);
        view_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        webview_detail.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    pb.setVisibility(View.GONE);
                } else {
                    if (pb.getVisibility() == View.GONE)
                        pb.setVisibility(View.VISIBLE);
                    pb.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
        webview_detail.setWebViewClient(new WebViewClient());
        //允许使用javascript
        webview_detail.getSettings().setJavaScriptEnabled(true);
        //开启 DOM storage API 功能 (HTML5 提供的一种标准的接口，主要将键值对存储在本地，
        // 在页面加载完毕后可以通过 JavaScript 来操作这些数据。）
        webview_detail.getSettings().setDomStorageEnabled(true);
        //提高渲染优先级
        webview_detail.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        //开启 Application Caches 功能
        webview_detail.getSettings().setAppCacheEnabled(true);
        webview_detail.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webview_detail.loadUrl(detail_url);
    }
}
