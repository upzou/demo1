package fastandroid.fast.com.cn.fastandroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import fastandroid.fast.com.cn.fastandroid.R;
import fastandroid.fast.com.cn.fastandroid.bean.MenuDetail;


/**
 * Created by zzs on 2017/3/8
 */

public class MenuDetailAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<MenuDetail> mData;

    public MenuDetailAdapter(Context context, List<MenuDetail> mData) {
        mInflater = LayoutInflater.from(context);
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return null==mData?0:mData.size();
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
            view=mInflater.inflate(R.layout.item_menu_detail,viewGroup,false);
            holder=new ViewHolder();
            holder.tv_menudetail= (TextView) view.findViewById(R.id.tv_menudetail);
            view.setTag(holder);
        }else{
            holder= (ViewHolder) view.getTag();
        }
        MenuDetail menuDetail = mData.get(i);
        holder.tv_menudetail.setText(menuDetail.getName());
        return view;
    }

    public class ViewHolder{
        TextView tv_menudetail;
    }

}
