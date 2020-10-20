package com.nhcar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nhcar.adapter.CatAdapter;
import com.nhcar.adapter.CatAdapter1;
import com.nhcar.entity.ECategory;
import com.nhcar.entity.EProduct;
import com.nhcar.utils.Const;
import com.nhcar.view.MyListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ActivityCategory extends AppCompatActivity {
    //声明控件、对象、变量
    private OkHttpClient okHttpClient=new OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5,TimeUnit.SECONDS)
            .build();
    private Gson gson=new Gson();

    private ListView categorylist_listview;

    private List<ECategory> listCat=new ArrayList<ECategory>();//存放所有品牌信息

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_category);

        initView();
        initData();
        initListener();

    }// onCreate End

    private void initView(){
        categorylist_listview=this.findViewById(R.id.categorylist_listview);
    }

    private void initData(){
        loadCategory();
    }

    private void initListener(){
        categorylist_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int cid=listCat.get(position).getCid();
                Intent intent=new Intent(getApplicationContext(),ActivityPoductList0.class);
                intent.putExtra("cid",cid);
                startActivity(intent);
            }
        });
    }
    private void loadCategory(){
        //利用OkHttp3网络工具访问网络接口获取数据

        String url= Const.SERVER_URL+Const.SERVLET_URL+"getAllCategory";

        Request request=new Request.Builder()
                .url(url)
                .get()
                .build();
        Call call=okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("网络访问返回结果",e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result=response.body().string();//接口返回结果
                Log.d("网络访问返回结果",result);
                //将接口返回的JSON数据还原为List<E>
                listCat=gson.fromJson(result,new TypeToken<List<ECategory>>(){}.getType());
                Log.d("<<<<JSON还原后集合的成员数量>>>",listCat.size()+"");
                Log.d("<<JSON还原后集合的第一个成员品牌名称>",listCat.get(3).getCname());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        CatAdapter1 adapter1=new CatAdapter1(listCat,getApplicationContext());
                        categorylist_listview.setAdapter(adapter1);
                    }
                });
            }
        });
    }

}
