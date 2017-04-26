package fastandroid.fast.com.cn.fastandroid.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fastandroid.fast.com.cn.fastandroid.R;
import fastandroid.fast.com.cn.fastandroid.adapter.TaskAdapter;
import fastandroid.fast.com.cn.fastandroid.bean.MenuDetail;
import fastandroid.fast.com.cn.fastandroid.bean.News;
import fastandroid.fast.com.cn.fastandroid.db.DBHelper;

import static fastandroid.fast.com.cn.fastandroid.fragment.HomeFragmenrt.homeAppAdapter;

/**
 * Created by zzs on 2017/4/24
 */

public class PushTasklistActivity extends Activity {

    public static final String TAG = "PushTasklistActivity";
    private List<MenuDetail> menus;
    private List<News> mTaskData;
    public static TaskAdapter taskAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_tasklist);

        initData();

        initView();

    }

    private void initView() {
        ListView lv_push_tasklist = (ListView) findViewById(R.id.lv_push_tasklist);
        LinearLayout ll_push_tasklist_empty = (LinearLayout) findViewById(R.id.ll_push_tasklist_empty);
        Button bt_one = (Button) findViewById(R.id.bt_one);

        ImageView left_image = (ImageView) findViewById(R.id.left_image);
        LinearLayout view_left = (LinearLayout) findViewById(R.id.view_left);
        left_image.setImageResource(R.drawable.angle_left);
        view_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        TextView mMID_title = (TextView) findViewById(R.id.midle_title);
        mMID_title.setText("推送");

        taskAdapter = new TaskAdapter(this, mTaskData);
        lv_push_tasklist.setAdapter(taskAdapter);
        lv_push_tasklist.setEmptyView(ll_push_tasklist_empty);

        lv_push_tasklist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent1 = new Intent(PushTasklistActivity.this, DetailActivity.class);
                intent1.putExtra("detail_url", mTaskData.get(i).getUrl());
                startActivity(intent1);

                mTaskData.get(i).setRead(true);//点击后设为已读
                taskAdapter.notifyDataSetChanged();
                homeAppAdapter.notifyDataSetChanged();

                //同时更改数据库中isRead标签
                DBHelper dbHelper = new DBHelper(PushTasklistActivity.this, getString(R.string.DB_NEWS));
                ContentValues contentValues = new ContentValues();
                contentValues.put("isRead", 0);
                dbHelper.update(contentValues, "title=?", new String[]{mTaskData.get(i).getTitle()}, getString(R.string.TABLE_NEWS));
                dbHelper.close();
            }
        });

        bt_one.setText(menus.get(0).getName());

        bt_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplication(), DetailActivity.class);
                String url = menus.get(0).getUrl();//将对应的url传到详情界面
                intent1.putExtra("detail_url", url);
                startActivity(intent1);
            }
        });


    }


    private void initData() {
        Intent intent = getIntent();
        //获取传过来的MenuDetail集合
        menus = (List<MenuDetail>) intent.getSerializableExtra("menu");

        mTaskData = new ArrayList<>();

//        for (int i = 0; i < 10; i++) {
//            News news = new News("新闻标题" + i, "新闻内容新闻内容新闻内容新闻内容新闻内容新闻内容新" + i);
//            mNewsData.add(news);
//        }

        DBHelper dbHelper = new DBHelper(this, getString(R.string.DB_NEWS));

        Cursor cursor = dbHelper.query(getString(R.string.TABLE_NEWS));
        while (cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String content = cursor.getString(cursor.getColumnIndex("content"));
            String url = cursor.getString(cursor.getColumnIndex("url"));
            String isRead = cursor.getString(cursor.getColumnIndex("isRead"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            String appid = cursor.getString(cursor.getColumnIndex("appid"));
            if (appid == null || appid.equals("")) {
                continue;
            } else if (appid.equals("2")) {

                News news = new News();
                news.setTitle(title);
                news.setContent(content);
                news.setUrl(url);
                news.setTime(time);
                //从数据库取数据时判断是否已读
                if (isRead.equals("1")) {
                    news.setRead(false);
                } else if (isRead.equals("0")) {
                    news.setRead(true);
                }

                mTaskData.add(0, news);
                Log.d(TAG, "initData2: " + mTaskData);
            }
        }

        cursor.close();
        dbHelper.close();

    }
}
