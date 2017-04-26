package fastandroid.fast.com.cn.fastandroid.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import fastandroid.fast.com.cn.fastandroid.R;
import fastandroid.fast.com.cn.fastandroid.bean.App;
import fastandroid.fast.com.cn.fastandroid.db.DBHelper;
import it.sephiroth.android.library.picasso.Picasso;

/**
 * Created by zzs on 2017/3/6
 */

public class HomeAppAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<App> mData;
    private Context mContext;

    public HomeAppAdapter() {
    }

    public HomeAppAdapter(Context context, List<App> mData) {
        mInflater = LayoutInflater.from(context);
        this.mData = mData;
        mContext = context;
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
        final ViewHolder holder;
        if (view == null) {
            view = mInflater.inflate(R.layout.item_home_app, viewGroup, false);
            holder = new ViewHolder();
            holder.iv_app_icon = (ImageView) view.findViewById(R.id.iv_app_icon);
            holder.tv_app_name = (TextView) view.findViewById(R.id.tv_app_name);
            holder.tv_red_point = (TextView) view.findViewById(R.id.tv_red_point);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        App app = mData.get(i);
        Picasso.with(holder.iv_app_icon.getContext()).load(app.getIcon()).into(holder.iv_app_icon);
        holder.tv_app_name.setText(app.getName());


        DBHelper dbHelper = new DBHelper(mContext, mContext.getString(R.string.DB_NEWS));//查询数据库里存储的标签isRead
        Cursor cursor = dbHelper.query(mContext.getString(R.string.TABLE_NEWS));
        while (cursor.moveToNext()) {
            String isRead = cursor.getString(cursor.getColumnIndex("isRead"));
            String appid = cursor.getString(cursor.getColumnIndex("appid"));

            if (appid == null || appid.equals("")) {//当没有推送appid时,默认存入第一行
                if (i == 0) {
                    if (isRead.equals("1")) {//当isRead=1时显示小红点
                        holder.tv_red_point.setVisibility(View.VISIBLE);
                        break;//只需有一条数据isRead=1,就显示小红点.否则隐藏小红点.
                    } else {
                        holder.tv_red_point.setVisibility(View.GONE);
                    }
                }
            } else if (app.getAppid() == Integer.parseInt(appid)) {
                if (isRead.equals("1")) {//当isRead=1时显示小红点
                    holder.tv_red_point.setVisibility(View.VISIBLE);
                    break;//只需有一条数据isRead=1,就显示小红点.否则隐藏小红点.
                } else {
                    holder.tv_red_point.setVisibility(View.GONE);
                }
            }
        }
        cursor.close();
        dbHelper.close();
        return view;
    }

    public class ViewHolder {
        ImageView iv_app_icon;
        TextView tv_app_name;
        TextView tv_red_point;
    }

}
