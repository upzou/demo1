package fastandroid.fast.com.cn.fastandroid.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

import fastandroid.fast.com.cn.fastandroid.R;
import fastandroid.fast.com.cn.fastandroid.fragment.NewsFragmenrt;
import fastandroid.fast.com.cn.fastandroid.fragment.NoticeFragment;

/**
 * Created by zzs on 2017/3/13
 */

public class NewsNoticeActivity extends FragmentActivity {

    private LayoutInflater mInflater;

    //定义数组来存放Fragment界面
    private Class<?> fragmentArray[] = {
            NewsFragmenrt.class,
            NoticeFragment.class};

    //定义数组来存放按钮图片
    private int mImageViewArray[] = {
            R.drawable.tab_news_btn,
            R.drawable.tab_notice_btn
    };

    //Tab选项卡的文字
    private String mTextviewArray[] = {"新闻", "公告"};
    private FragmentTabHost mTabhost;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_notice);

        initView();

    }

    private void initView() {
        mInflater = LayoutInflater.from(this);
        mTabhost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabhost.setup(this, getSupportFragmentManager(), R.id.news_notice_tabcontent);

        for (int i = 0; i < mTextviewArray.length; i++) {
            TabHost.TabSpec tabSpec = mTabhost.newTabSpec(mTextviewArray[i]).setIndicator(getTabItemView(i));
            mTabhost.addTab(tabSpec, fragmentArray[i], null);
            mTabhost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.selector_tab_background);

        }

    }

    /**
     * 给Tab按钮设置图标和文字
     */
    private View getTabItemView(int index) {
        View view = mInflater.inflate(R.layout.tab_item_news_notice, null);

        TextView textView = (TextView) view.findViewById(R.id.tv_nn);
        textView.setText(mTextviewArray[index]);

        return view;
    }

}
