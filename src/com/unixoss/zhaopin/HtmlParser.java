package com.unixoss.zhaopin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

public class HtmlParser {
	URL url;
	public HtmlParser(String url) throws MalformedURLException {
		this.url = new URL(url);
	}
	
	public List<Info> getInfo() throws IOException {
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.connect();
		InputStream stream = conn.getInputStream();
		return parseItem(stream);
	}
	
	public List<Info> parseItem(InputStream stream) throws IOException {
		String reType = "^.*rmtimespan[^>]*>\\[(\\w*).*";
		String reTitle = "\\s*<td class=\"rmTitle\"><a href=\"(([^\"]*)\">(\\w*))</a>";
		String reTimeLocal = "\\s*<td class=\"rmLocation\">";
		Pattern paType = Pattern.compile(reType);
		Pattern paTitle = Pattern.compile(reTitle);
		Pattern paTimeLocal = Pattern.compile(reTimeLocal);
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		String line = reader.readLine();
		List<Info> list = new ArrayList<Info>();
		Info info = new Info();
		while(line != null) {
			Matcher matcher = paType.matcher(line);
			if(matcher.matches()) {
				Log.d("type", line);
				//Matcher matcher = paType.matcher(line);
				Log.d("type", matcher.group(1));
				//info.type = matcher.group(1);
			} else if(paTitle.matcher(line).matches()) {
				Log.d("title", line);
				//Matcher matcher = paTitle.matcher(line);
				Log.d("link", matcher.group());
				info.link = matcher.group(1);
				Log.d("link", matcher.group(0));
				info.title = matcher.group(2);
				Log.d("title.", info.title);
			} else if(paTimeLocal.matcher(line).matches()) {
				Log.d("timelocal", line);
				StringBuilder builder = new StringBuilder();
				while(!line.contains("</td>")) {
					line = reader.readLine();
					builder.append(line);
				}
				//TODO: add time and location.
				list.add(info);
				info = new Info();
			}
			line = reader.readLine();
		}
		return list;
	}
}

class Info {
	String type;
	String link;
	String title;
	long start;
	long end;
	String location;
}