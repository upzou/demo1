package fastandroid.fast.com.cn.fastandroid.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import fastandroid.fast.com.cn.fastandroid.R;
import fastandroid.fast.com.cn.fastandroid.fragment.HomeFragmenrt;
import fastandroid.fast.com.cn.fastandroid.fragment.SettingFragment;

public class MainActivity extends FragmentActivity {
    private long mExitTime;
    //定义FragmentTabHost对象
    private FragmentTabHost mTabHost;

    //定义一个布局
    private LayoutInflater layoutInflater;

    private BottomNavigationBar bottom_navigation_bar;


    //定义数组来存放Fragment界面
    private Class<?> fragmentArray[] = {
            HomeFragmenrt.class,
            SettingFragment.class};

    //定义数组来存放按钮图片
    private int mImageViewArray[] = {
            R.drawable.tab_home_btn,
            R.drawable.tab_more_btn
    };

    //Tab选项卡的文字
    private String mTextviewArray[] = {"首页", "设置"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        bottom_navigation_bar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
//        initBottomNavigationBar();



        initView();


    }




    /**
     * 主界面控件初始化
     */
    private void initView() {
        //实例化布局对象
        layoutInflater = LayoutInflater.from(this);

        //实例化TabHost对象，得到TabHost
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        //得到fragment的个数
        int count = fragmentArray.length;

        for (int i = 0; i < count; i++) {
            //为每一个Tab按钮设置图标、文字和内容
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(mTextviewArray[i]).setIndicator(getTabItemView(i));
            //将Tab按钮添加进Tab选项卡中
            mTabHost.addTab(tabSpec, fragmentArray[i], null);
            //设置Tab按钮的背景
            mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.selector_tab_background);
        }

    }

    /**
     * 给Tab按钮设置图标和文字
     */
    private View getTabItemView(int index) {
        View view = layoutInflater.inflate(R.layout.tab_item_view, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
        imageView.setImageResource(mImageViewArray[index]);

        TextView textView = (TextView) view.findViewById(R.id.textview);
        textView.setText(mTextviewArray[index]);

        return view;
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


    public static void showToast(final String toast, final Context context) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }).start();
    }


    //初始化BottomNavigationBar
    private void initBottomNavigationBar() {
        bottom_navigation_bar.setMode(BottomNavigationBar.MODE_FIXED);
        bottom_navigation_bar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_DEFAULT);
        bottom_navigation_bar.setInActiveColor(R.color.colorInActive)
                .setActiveColor(R.color.colorPrimary)
                .setBarBackgroundColor(R.color.colorBarBg);
        bottom_navigation_bar.addItem(new BottomNavigationItem(R.drawable.icon_home_nor, "首页").
                setInactiveIcon(ContextCompat.getDrawable(this, R.drawable.icon_home_sel)))
                .addItem(new BottomNavigationItem(R.drawable.icon_more_nor, "设置").
                        setInactiveIcon(ContextCompat.getDrawable(this, R.drawable.icon_more_sel)))
                .initialise();
    }


}
