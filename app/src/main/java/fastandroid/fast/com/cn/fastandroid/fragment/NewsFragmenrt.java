package fastandroid.fast.com.cn.fastandroid.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.TextView;

import java.util.List;

import fastandroid.fast.com.cn.fastandroid.bean.News;


/**
 * Created by zzs on 2017/3/13
 */
public class NewsFragmenrt extends Fragment {
    private TextView mMID_title;
    public static final String TAG = "NewsFragmenrt";

    private List<News> mNewsData;
//    public static NewsAdapter newsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        initData();

    }

//    private void initData() {
//        mNewsData = new ArrayList<>();
//
////        for (int i = 0; i < 10; i++) {
////            News news = new News("新闻标题" + i, "新闻内容新闻内容新闻内容新闻内容新闻内容新闻内容新" + i);
////            mNewsData.add(news);
////        }
//
//        DBHelper dbHelper = new DBHelper(getContext());
//
//        Cursor cursor = dbHelper.query();
//        while (cursor.moveToNext()) {
//            String title = cursor.getString(cursor.getColumnIndex("title"));
//            String content = cursor.getString(cursor.getColumnIndex("content"));
//            String url = cursor.getString(cursor.getColumnIndex("url"));
//            String isRead = cursor.getString(cursor.getColumnIndex("isRead"));
//            String time = cursor.getString(cursor.getColumnIndex("time"));
//
//            News news = new News();
//            news.setTitle(title);
//            news.setContent(content);
//            news.setUrl(url);
//            news.setTime(time);
//            //从数据库取数据时判断是否已读
//            if (isRead.equals("1")) {
//                news.setRead(false);
//            } else if (isRead.equals("0")) {
//                news.setRead(true);
//            }
//
//            mNewsData.add(0, news);
//            Log.d(TAG, "initData2: " + mNewsData);
//        }
//        cursor.close();
//        dbHelper.close();
//    }


//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View mNewsFragment = inflater.inflate(R.layout.fragment_news, container, false);
//
//        ImageView left_image = (ImageView) mNewsFragment.findViewById(R.id.left_image);
//        left_image.setImageResource(R.drawable.angle_left);
//        left_image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getActivity().finish();
//            }
//        });
//
//        mMID_title = (TextView) mNewsFragment.findViewById(R.id.midle_title);
//        mMID_title.setText("新闻");
//
//        ListView lv_news = (ListView) mNewsFragment.findViewById(R.id.lv_push_news_notice);
//        LinearLayout ll_empty = (LinearLayout) mNewsFragment.findViewById(R.id.ll_homeapp_empty);
//        newsAdapter = new NewsAdapter(getActivity(), mNewsData);
//        lv_news.setAdapter(newsAdapter);
//        lv_news.setEmptyView(ll_empty);
//
//        lv_news.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent intent1 = new Intent(getActivity(), DetailActivity.class);
//                intent1.putExtra("detail_url", mNewsData.get(i).getUrl());
//                startActivity(intent1);
//
//                mNewsData.get(i).setRead(true);//点击后设为已读
//                newsAdapter.notifyDataSetChanged();
//                homeAppAdapter.notifyDataSetChanged();
//
//                //同时更改数据库中isRead标签
//                DBHelper dbHelper = new DBHelper(getContext());
//                ContentValues contentValues = new ContentValues();
//                contentValues.put("isRead", 0);
//                dbHelper.update(contentValues, "title=?", new String[]{mNewsData.get(i).getTitle()});
//                dbHelper.close();
//            }
//        });
//        return mNewsFragment;
//    }
}
