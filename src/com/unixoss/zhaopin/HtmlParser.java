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
		Pattern paType = Pattern.compile(reType);
		Pattern paTitle = Pattern.compile(reTitle);
		Pattern paTimeLocal = Pattern.compile(reTimeLocal);
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		List<Info> list = new ArrayList<Info>();
		Info info = new Info();
		String line;
		while(true) {
			line = reader.readLine();
			if(line == null)
				break;
			Matcher matcher = paType.matcher(line);
			if(line.contains("rmRow")) {
				info.first = true;
				continue;
			}
			if(matcher.matches()) {
				info.type = matcher.group(1);
				continue;
			}
			matcher = paTitle.matcher(line);
			if(matcher.matches()) {
				info.link = matcher.group(1);
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
				//正则表达式不知道为什么就匹配不上，于是使用substring，但序号依然费解。
				DateFormat formater = new SimpleDateFormat("MM月dd日HH:mm");
				Date date = formater.parse(builder.substring(0, 16));
				info.start = date.getTime();
				date = formater.parse(builder.substring(22, 38));
				info.end = date.getTime();
				info.location = builder.substring(61).replace("</td>", "");
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
	boolean first;
}