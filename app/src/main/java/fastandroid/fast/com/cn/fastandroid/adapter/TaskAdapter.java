package fastandroid.fast.com.cn.fastandroid.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fastandroid.fast.com.cn.fastandroid.R;
import fastandroid.fast.com.cn.fastandroid.bean.News;

/**
 * Created by zzs on 2017/3/13
 */

public class TaskAdapter extends BaseAdapter {
    private LayoutInflater minflater;
    private List<News> mData;
    public static final String TAG = "NewsAdapter";

    public void setmData(List<News> mData) {
        this.mData = mData;
    }

    public TaskAdapter() {
    }

    public TaskAdapter(Context context, List<News> data) {
        minflater = LayoutInflater.from(context);
        mData = data;
    }

    @Override
    public int getCount() {
        return null == mData ? 0 : mData.size();
    }

    @Override
    public Object getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = minflater.inflate(R.layout.item_news, viewGroup, false);
            holder = new ViewHolder();
            holder.tv_news_title = (TextView) view.findViewById(R.id.tv_news_title);
            holder.tv_news_content = (TextView) view.findViewById(R.id.tv_news_content);
            holder.tv_push_time = (TextView) view.findViewById(R.id.tv_push_time);
            holder.tv_news_content.setTag(i);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
            holder.tv_news_content.setTag(i);
        }

        News news = (News) getItem(i);
        holder.tv_news_title.setText(news.getTitle());
        holder.tv_news_content.setText(news.getContent());
        holder.tv_push_time.setText(news.getTime());

        //根据是否已读标签设置字体颜色
        if (news.isRead()) {
            holder.tv_news_title.setTextColor(Color.GRAY);
            holder.tv_news_content.setTextColor(Color.GRAY);
        } else {
            holder.tv_news_title.setTextColor(Color.BLACK);
            holder.tv_news_content.setTextColor(Color.BLACK);
        }

        return view;
    }

    public void add(News news) {
        if (mData == null) {
            mData = new ArrayList<>();
        }
        mData.add(0, news);
        notifyDataSetChanged();
    }


    public class ViewHolder {
        TextView tv_news_title;
        TextView tv_news_content;
        TextView tv_push_time;
    }

}
