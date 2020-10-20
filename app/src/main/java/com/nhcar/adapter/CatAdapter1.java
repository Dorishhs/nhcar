package com.nhcar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nhcar.R;
import com.nhcar.entity.ECategory;
import com.nhcar.utils.Const;

import java.util.ArrayList;
import java.util.List;

import loopj.android.image.SmartImageView;

public class CatAdapter1 extends BaseAdapter {
    //声明对象、变量
    private Context context;    //用于存放上下文
    private List<ECategory> list=new ArrayList<ECategory>();     //用于存放数据源
    private LayoutInflater layoutInflater;  //用于获取布局

    //构造方法：需要上下文、数据源集合进行初始化
    public CatAdapter1(List<ECategory> list, Context context) {
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
            convertView=layoutInflater.inflate(R.layout.activity_category_item,null);
            //获取项布局中的控件
            holder.smv=convertView.findViewById(R.id.category_image);
            holder.tv=convertView.findViewById(R.id.category_title);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder) convertView.getTag();
        }
        //视图中控件的数据绑定
        String imgUrl= Const.SERVER_URL+Const.PIC_URL+list.get(position).getCpic();
        //Log.d("<<<<<Cat imgURL>>>>>",imgUrl);
        holder.smv.setImageUrl(imgUrl);
        holder.tv.setText(list.get(position).getCname());
        return convertView;
    }

    //ViewHolder类的成员与项布局中的控件一一对应，名称可以不同但控件类型必要相同
    class ViewHolder{
        SmartImageView smv;
        TextView tv;
    }
}
