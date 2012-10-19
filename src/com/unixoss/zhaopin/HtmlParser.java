package com.unixoss.zhaopin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

public class HtmlParser {
	URL url;
	public HtmlParser(String url) throws MalformedURLException {
		this.url = new URL(url);
	}
	
	public List<Info> getInfo() throws IOException, ParseException {
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.connect();
		InputStream stream = conn.getInputStream();
		return parseItem(stream);
	}
	
	public List<Info> parseItem(InputStream stream) throws IOException, ParseException {
		String reType = "^.*rmtimespan.*\\[(\\w*).*";
		String reTitle = "^.*rmTitle.*href=\"(.*)\">(.*)<.*";
		String reTimeLocal = "^.*rmLocation.*";
		String reDateLocal = "^.*<(.*)";
		Pattern paType = Pattern.compile(reType);
		Pattern paTitle = Pattern.compile(reTitle);
		Pattern paTimeLocal = Pattern.compile(reTimeLocal);
		Pattern paDateLocal = Pattern.compile(reDateLocal);
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		List<Info> list = new ArrayList<Info>();
		Info info = new Info();
		String line;
		while(true) {
			line = reader.readLine();
			if(line == null)
				break;
			Matcher matcher = paType.matcher(line);
			if(matcher.matches()) {
				Log.d("type", matcher.group(1));
				info.type = matcher.group(1);
				continue;
			}
			matcher = paTitle.matcher(line);
			if(matcher.matches()) {
				info.link = matcher.group(1);
				Log.d("link", matcher.group(1));
				Log.d("title.", matcher.group(2));
				info.title = matcher.group(2);
				continue;
			}
			matcher = paTimeLocal.matcher(line);
			if(matcher.matches()) {
				StringBuilder builder = new StringBuilder();
				while(!line.contains("</td>")) {
					line = reader.readLine();
					builder.append(line);
				}
				Log.d("builder1", ""+builder.toString().);
				String str = builder.toString().replace("\r", "");
				matcher = paDateLocal.matcher(builder.toString());
				Log.d("builderbool1", ""+ Pattern.compile(".*").matcher(str).group());
				Log.d("buildergroup", matcher.group());
				DateFormat formater = new SimpleDateFormat("MM‘¬dd»’HH:mm");
				Date date = formater.parse(matcher.group(0));
				info.start = date.getTime();
				date = formater.parse(matcher.group(2));
				info.end = date.getTime();
				info.location = matcher.group(4);
				Log.d("start", info.start + "");
				Log.d("end", info.end + "");
				Log.d("location", info.location + "");
				//TODO: add time and location.
				list.add(info);
				info = new Info();
				continue;
			}
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