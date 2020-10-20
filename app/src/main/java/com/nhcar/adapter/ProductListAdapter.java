package com.nhcar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nhcar.R;
import com.nhcar.entity.ECategory;
import com.nhcar.entity.EProduct;
import com.nhcar.utils.Const;

import java.util.ArrayList;
import java.util.List;

import loopj.android.image.SmartImageView;

public class ProductListAdapter extends BaseAdapter {
    //声明对象、变量
    private Context context;    //用于存放上下文
    private List<EProduct> list=new ArrayList<EProduct>();     //用于存放数据源
    private LayoutInflater layoutInflater;  //用于获取布局

    //构造方法：需要上下文、数据源集合进行初始化
    public ProductListAdapter(Context context, List<EProduct> list) {
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
        ProductListAdapter.ViewHolder holder=new ProductListAdapter.ViewHolder();
        layoutInflater=LayoutInflater.from(context);
        //如果视图不存在
        if(convertView==null){
            //获取项布局
            convertView=layoutInflater.inflate(R.layout.activity_productlist_item,null);
            //获取项布局中的控件
            holder.smv=convertView.findViewById(R.id.productlist_image);
            holder.tv=convertView.findViewById(R.id.productlist_title);
            holder.pv=convertView.findViewById(R.id.productlist_price);
            convertView.setTag(holder);
        }else {
            holder=(ProductListAdapter.ViewHolder) convertView.getTag();
        }
        //视图中控件的数据绑定
        String imgUrl= Const.SERVER_URL+Const.PIC_URL+list.get(position).getPpic();
        //Log.d("<<<<<Cat imgURL>>>>>",imgUrl);
        holder.smv.setImageUrl(imgUrl);
        holder.tv.setText(list.get(position).getPname());
        holder.pv.setText(list.get(position).getPprice()+"");
        return convertView;
    }

    //ViewHolder类的成员与项布局中的控件一一对应，名称可以不同但控件类型必要相同
    class ViewHolder{
        SmartImageView smv;
        TextView tv;
        TextView pv;
    }
}

