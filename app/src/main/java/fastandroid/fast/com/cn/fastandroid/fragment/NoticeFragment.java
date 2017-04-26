package fastandroid.fast.com.cn.fastandroid.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import fastandroid.fast.com.cn.fastandroid.R;

/**
 * Created by zzs on 2017/3/13
 */
public class NoticeFragment extends Fragment {

    public static final String TAG = "NoticeFragment";
    private String mURLEncoder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mNoticeFragment = inflater.inflate(R.layout.detail, container, false);


        //URL转码(特殊字符)
        try {
            mURLEncoder = URLEncoder.encode("http://10.0.0.2:8088/CnpcMobile/News/List?code=2&classgroup=5%2C6", "utf-8");
            Log.d(TAG, "mURLEncoder: " + mURLEncoder);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

//        Bundle extras = getIntent().getExtras();
//        String detail_url =extras.getString("detail_url");
//        if (detail_url == null) {
        String detail_url = "http://10.0.0.2:8088/CnpcMobile/Helper/Redirect?" +
                "echostr=DDEC5AC9C2A7CBDA9009F3E424885042BD541672AD5858048D7A52B1F95B1D9AEAD4E4E3266CDA9C79BD7624BB5C5B39&uri="
                + mURLEncoder;
//        }
        WebView webview_detail = (WebView) mNoticeFragment.findViewById(R.id.webview_detail);
        final ProgressBar pb = (ProgressBar) mNoticeFragment.findViewById(R.id.webv_progressBar);
        TextView midle_title = (TextView) mNoticeFragment.findViewById(R.id.midle_title);
        ImageView left_image = (ImageView) mNoticeFragment.findViewById(R.id.left_image);
        midle_title.setText("公告");
        left_image.setImageResource(R.drawable.angle_left);
        left_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
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

        return mNoticeFragment;


    }
}
