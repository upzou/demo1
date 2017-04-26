package fastandroid.fast.com.cn.fastandroid.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import fastandroid.fast.com.cn.fastandroid.R;
import fastandroid.fast.com.cn.fastandroid.adapter.MenuDetailAdapter;
import fastandroid.fast.com.cn.fastandroid.bean.MenuDetail;

import static fastandroid.fast.com.cn.fastandroid.R.id.midle_title;


/**
 * Created by zzs on 2017/3/8
 */

public class TasklistActivity extends Activity {
    private TextView mMID_title;
    private String appname;
    private List<MenuDetail> menus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_detail);

        initData();

        initView();

    }

    private void initData() {
        Intent intent = getIntent();
        //获取传过来的MenuDetail集合
        menus = (List<MenuDetail>) intent.getSerializableExtra("menu");
        appname = intent.getStringExtra("appname");
    }

    private void initView() {
        mMID_title = (TextView) findViewById(midle_title);
        mMID_title.setText(appname);

        ImageView left_image = (ImageView) findViewById(R.id.left_image);
        left_image.setImageResource(R.drawable.angle_left);
        left_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ListView lv_menu_detail = (ListView) findViewById(R.id.lv_menu_detail);

        MenuDetailAdapter menuDetailAdapter = new MenuDetailAdapter(getApplication(), menus);

        lv_menu_detail.setAdapter(menuDetailAdapter);

        lv_menu_detail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent1 = new Intent(getApplication(), DetailActivity.class);
                String url = menus.get(i).getUrl();//将对应的url传到详情界面
                intent1.putExtra("detail_url", url);
                startActivity(intent1);

            }
        });
    }
}
