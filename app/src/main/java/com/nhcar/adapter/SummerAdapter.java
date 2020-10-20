package com.nhcar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.nhcar.R;
import com.nhcar.entity.EProduct;
import com.nhcar.utils.Const;

import java.util.ArrayList;
import java.util.List;

import loopj.android.image.SmartImageView;

public class SummerAdapter extends BaseAdapter {
    //声明对象，变量
    private Context context;
    private List<EProduct> list =new ArrayList<EProduct>();
    private LayoutInflater layoutInflater;

    public SummerAdapter(Context context, List<EProduct> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=new ViewHolder();
        layoutInflater=LayoutInflater.from(context);
        //如果视图不存在
        if(convertView==null){
            //获取项布局
            convertView=layoutInflater.inflate(R.layout.activity_index_gallery_item,null);
            //获取项布局中的控件
            holder.smv=convertView.findViewById(R.id.index_gallery_item_image);
            holder.tv=convertView.findViewById(R.id.index_gallery_item_text);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder) convertView.getTag();
        }
        //视图中控件的数据绑定
        String imgUrl= Const.SERVER_URL+Const.PIC_URL+list.get(position).getPpic();
        //Log.d("<<<<<Cat imgURL>>>>>",imgUrl);
        holder.smv.setImageUrl(imgUrl);
        holder.tv.setText(list.get(position).getPprice()+"");
        return convertView;
        }

        class ViewHolder {
            SmartImageView smv;
            TextView tv;
        }
}
