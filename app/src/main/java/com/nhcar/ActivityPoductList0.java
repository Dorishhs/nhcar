package com.nhcar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.nhcar.adapter.CatAdapter1;
import com.nhcar.adapter.ProductListAdapter;
import com.nhcar.entity.ECarNewsResult;
import com.nhcar.entity.EProduct;
import com.nhcar.entity.EProductListResult;
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

public class ActivityPoductList0 extends AppCompatActivity {
	// 声明控件、对象
	private OkHttpClient okHttpClient=new OkHttpClient.Builder()
			.connectTimeout(5, TimeUnit.SECONDS)
			.readTimeout(3,TimeUnit.SECONDS)
			.build();   //第三方网络工具okhttp3对象
	private Gson gson=new Gson();   //JSON工具对象
	private int pageNo=0;	//	当前页码

	private MyListView productlist_listview;
	private Button productback,btnmore;

	private List<EProduct> listProductListResult=new ArrayList<EProduct>();	//存放汽车列表信息，当前页前累加
	private ProductListAdapter adapter;	//	数据适配器对象

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_productlist0);

		initView();
		initData();
		initListener();

	}// onCreate End

	private void initView() {
		productlist_listview=this.findViewById(R.id.productlist_listview);
		productback=this.findViewById(R.id.productback);
		btnmore=this.findViewById(R.id.btnmore);
	}

	private void initData() {
		Intent intent=this.getIntent();
		int cid=intent.getIntExtra("cid",0);
		Toast.makeText(this, "品牌ID"+cid, Toast.LENGTH_SHORT).show();
		loadProductList(cid);
	}

	private void initListener() {
		productback.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		btnmore.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=getIntent();
				int cid=intent.getIntExtra("cid",0);
				loadMoreProductList(cid);
			}
		});
	}
	private void loadProductList(int cid) {
		String url = Const.SERVER_URL + Const.SERVLET_URL + "getproductListByCid";
		pageNo = 1;
		Log.d("<<<>>>", url);

		FormBody.Builder formBoby = new FormBody.Builder();   //表单参数对象
		formBoby.add("cid", String.valueOf(cid));
		formBoby.add("pageno", String.valueOf(pageNo)); //参数1，还可以添加更多参数
		formBoby.add("pagesize", "6");

		final Request request=new Request.Builder()
				.url(url)
				.post(formBoby.build())
				.build();
		Call call=okHttpClient.newCall(request);
		call.enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				Log.d("getNewsListByPage返回:", e.getMessage());
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				String result = response.body().string(); //接收接口返回的内容
				Log.d("getNewsListByPage返回：", result);

				//将接口返回的JSON数据还原为List<E>
				EProductListResult eProduct = gson.fromJson(result, EProductListResult.class);
//                final List<ECarNews> listCarNews=eCarNewsResult.getDataResult();
				listProductListResult = eProduct.getDataResult();
				Log.d("<<<<listSummer行数>>>", listProductListResult.size() + "");
				Log.d("<<<<listSummer>>>", gson.toJson(listProductListResult));


				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						adapter = new ProductListAdapter(getApplicationContext(), listProductListResult);
						productlist_listview.setAdapter(adapter);
					}
				});
			}
		});
	}

	private void loadMoreProductList(int cid) {
		String url = Const.SERVER_URL + Const.SERVLET_URL + "getproductListByCid";
		pageNo++;
		Log.d("<<<>>>", url);

		FormBody.Builder formBoby = new FormBody.Builder();   //表单参数对象
		formBoby.add("cid", String.valueOf(cid));
		formBoby.add("pageno", String.valueOf(pageNo)); //参数1，还可以添加更多参数
		formBoby.add("pagesize", "6");

		final Request request=new Request.Builder()
				.url(url)
				.post(formBoby.build())
				.build();
		Call call=okHttpClient.newCall(request);
		call.enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				Log.d("getNewsListByPage返回:", e.getMessage());
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				String result = response.body().string(); //接收接口返回的内容
				Log.d("getNewsListByPage返回：", result);

				//将接口返回的JSON数据还原为List<E>
				EProductListResult eProduct = gson.fromJson(result, EProductListResult.class);
				listProductListResult.addAll(eProduct.getDataResult());
				listProductListResult = eProduct.getDataResult();
				Log.d("<<<<listSummer行数>>>", listProductListResult.size() + "");
				Log.d("<<<<listSummer>>>", gson.toJson(listProductListResult));


				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						ProductListAdapter adapter = new ProductListAdapter(getApplicationContext(), listProductListResult);
						productlist_listview.setAdapter(adapter);
						//通知数据更新，即刷新提示
						adapter.notifyDataSetChanged();
					}
				});
			}
		});
	}
}
