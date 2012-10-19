package com.unixoss.zhaopin;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListView;

public class MainActivity extends Activity implements Runnable {
	ListAdapter adapter;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListAdapter adapter = new ListAdapter(this);
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

	@Override
	public void run() {
		try {
			HtmlParser parser = new HtmlParser("http://job.ustb.edu.cn/accms/sites/jobc/zhaopinhuixinxi-list.jsp?fromDate=20121029&F_FL1=%E6%A0%A1%E5%86%85&F_FL2=");
			List<Info> list = parser.getInfo();
			adapter.notifyDataSetChanged(list);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
