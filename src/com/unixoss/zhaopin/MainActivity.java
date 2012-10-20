package com.unixoss.zhaopin;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListView;

public class MainActivity extends Activity implements Runnable {
	ListAdapter adapter;
	List<Info> list;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adapter = new ListAdapter(this);
        ListView listView = (ListView)findViewById(R.id.listView1);
        listView.setAdapter(adapter);
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

	public Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			adapter.notifyDataSetChanged(list);
    	}
	};

	@Override
	public void run() {
		try {
			HtmlParser parser = new HtmlParser("http://job.ustb.edu.cn/accms/sites/jobc/zhaopinhuixinxi-list.jsp?F_FL1=%E6%A0%A1%E5%86%85&F_FL2=");
			list = parser.getInfo();
			mHandler.sendEmptyMessage(0);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
