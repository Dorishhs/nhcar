package com.nhcar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nhcar.adapter.CarNewsAdapter;
import com.nhcar.adapter.CatAdapter;
import com.nhcar.adapter.SummerAdapter;
import com.nhcar.entity.ECarNews;
import com.nhcar.entity.ECarNewsResult;
import com.nhcar.entity.ECategory;
import com.nhcar.entity.EProduct;
import com.nhcar.entity.EProductListResult;
import com.nhcar.utils.Const;
import com.nhcar.view.MyGridView;
import com.nhcar.view.MyListView;

import org.dkn.view.ImageCycleView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import loopj.android.image.SmartImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class ActivityIndex extends AppCompatActivity {
    // 声明控件、对象
    private OkHttpClient okHttpClient=new OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(3,TimeUnit.SECONDS)
            .build();   //第三方网络工具okhttp3对象
    private Gson gson=new Gson();   //JSON工具对象

    private ImageCycleView icv_topView;     //轮播控件
    private MyGridView grvfunctionlist;   //  热销品牌网络控件
    private Gallery index_jingqiu_gallery;  //夏季风暴廊控件
    private MyListView lvcarnews;   //汽车新闻


    private List<ECategory> listCat=new ArrayList<ECategory>();     //数据源
    private List<EProduct> listSummer=new ArrayList<EProduct>();    //存放夏季风暴数据
    private List<ECarNews> listCarNews=new ArrayList<ECarNews>();//存放汽车新闻


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_index);
        initView();
        initData();
        initListener();

    }// onCreate End



    private void initView(){
        icv_topView=this.findViewById(R.id.icv_topView);
        grvfunctionlist=this.findViewById(R.id.grvfunctionlist);
        index_jingqiu_gallery=this.findViewById(R.id.index_jingqiu_gallery);
        lvcarnews=this.findViewById(R.id.lvcarnews);

    }

    private void initData(){
        loadImages();//给轮播控件装载图片
        loadHotCategory();//调用接口读网络数据
        loadSummerProduct();
        loadCarNews();//调用接口读取网络新闻

    }
    private void initListener(){
        grvfunctionlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(ActivityIndex.this,"你点击的是："+listCat.get(position).getCname(),Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getApplicationContext(),ActivityPoductList0.class);
                int cid=listCat.get(position).getCid();
                intent.putExtra("cid",cid);
                //intent.putExtra("cid",listCat.get(position).getCid());
                startActivity(intent);
            }
        });
        index_jingqiu_gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ActivityIndex.this,"你点击的是："+listSummer.get(position).getPname(),Toast.LENGTH_SHORT).show();
            }
        });
        lvcarnews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ActivityIndex.this, "你点击的是："+listCarNews.get(position).getNtitle(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //自定义方法用于给轮播图控件装载图片
    private void loadImages(){
        List<ImageCycleView.ImageInfo> list=new ArrayList<ImageCycleView.ImageInfo>();
        /*
       list.add(new ImageCycleView.ImageInfo(R.drawable.ad1,"",""));
        list.add(new ImageCycleView.ImageInfo(R.drawable.ad2,"",""));
        list.add(new ImageCycleView.ImageInfo(R.drawable.ad3,"",""));
        list.add(new ImageCycleView.ImageInfo(R.drawable.ad5,"",""));
        list.add(new ImageCycleView.ImageInfo(R.drawable.ad6,"",""));

         */

        //网络图片
        String picUrl=Const.SERVER_URL+Const.PIC_URL;
        list.add(new ImageCycleView.ImageInfo(picUrl+"ad1.jpg","",""));
        list.add(new ImageCycleView.ImageInfo(picUrl+"ad2.jpg","",""));
        list.add(new ImageCycleView.ImageInfo(picUrl+"ad3.jpg","",""));
        list.add(new ImageCycleView.ImageInfo(picUrl+"ad5.jpg","",""));
        list.add(new ImageCycleView.ImageInfo(picUrl+"ad6.jpg","",""));

        icv_topView.loadData(list, new ImageCycleView.LoadImageCallBack() {
            @Override
            public ImageView loadAndDisplay(ImageCycleView.ImageInfo imageInfo) {
                /*ImageView iimg =new ImageView(getApplicationContext());
                iimg.setImageResource(Integer.parseInt(imageInfo.image.toString()));
                return iimg;

                 */
                //装载网络图片
                SmartImageView smv=new SmartImageView(getApplicationContext());
                smv.setImageUrl(imageInfo.image.toString());
               // Log.d("<<<<>>>>",imageInfo.image.toString());
                return smv;
            }
        });
    }

    //自定义方法用于调用网络接口获取热销品牌数据
    private void loadHotCategory(){
        //利用OkHttp3网络工具访问网络接口获取数据

        String url=Const.SERVER_URL+Const.SERVLET_URL+"getAllCategory";

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

                listCat=gson.fromJson(result,new TypeToken<List<ECategory>>(){}.getType());
                Log.d("<<<<JSON还原后集合的成员数量>>>",listCat.size()+"");
                Log.d("<<JSON还原后集合的第一个成员品牌名称>",listCat.get(3).getCname());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        CatAdapter adapter=new CatAdapter(listCat,getApplicationContext());
                        grvfunctionlist.setAdapter(adapter);
                    }
                });
            }
        });
    }

    private void loadSummerProduct(){
        String url=Const.SERVER_URL+Const.SERVLET_URL+"getproductListByType";
        Log.d("<<<<<>>>",url);

        FormBody.Builder formBoby=new FormBody.Builder();   //表单参数对象
        formBoby.add("type","3");   //参数1，还可以添加更多参数

        Request request=new Request.Builder()
                .url(url)
                .post(formBoby.build())
                .build();
        Call call=okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("getproductListByType返回:",e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result=response.body().string();//接收接口返回的内容
                Log.d("getproductListByType返回:",result);
                //将接口返回的JSON数据还原为List<E>
                listSummer=gson.fromJson(result,new TypeToken<List<EProduct>>(){}.getType());
                Log.d("<<<<listSummer行数>>>",listSummer.size()+"");

                //在UI线程中更新
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //此处更新UI：数据绑定到GridView
                        SummerAdapter adapter=new SummerAdapter(getApplicationContext(),listSummer);
                        index_jingqiu_gallery.setAdapter(adapter);
                        index_jingqiu_gallery.setSelection(2);//设置选中项，选中项会居中显示
                    }
                });
            }
        });
    }//loadSummerProduct

    //自定义方法用于调用网络接口获取汽车新闻资讯
    private void loadCarNews(){
        String url=Const.SERVER_URL+Const.SERVLET_URL+"getNewsListByPageNo";
        Log.d("<<<>>>",url);

        FormBody.Builder formBoby=new FormBody.Builder();   //表单参数对象
        formBoby.add("pageno","1"); //参数1，还可以添加更多参数
        formBoby.add("pagesize","6");

        final Request request=new Request.Builder()
                .url(url)
                .post(formBoby.build())
                .build();
        Call call=okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("getNewsListByPage返回:",e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result=response.body().string(); //接收接口返回的内容
                Log.d("getNewsListByPage返回：",result);

                //将接口返回的JSON数据还原为List<E>
                ECarNewsResult eCarNewsResult=gson.fromJson(result,ECarNewsResult.class);
//                final List<ECarNews> listCarNews=eCarNewsResult.getDataResult();
                listCarNews=eCarNewsResult.getDataResult();
                Log.d("<<<<listSummer行数>>>",listSummer.size()+"");
                Log.d("<<<<listSummer>>>",gson.toJson(listCarNews));

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //更新UI
                        CarNewsAdapter adapter=new CarNewsAdapter(getApplicationContext(),listCarNews);
                        lvcarnews.setAdapter(adapter);
                    }
                });
            }
        });
    }


}
