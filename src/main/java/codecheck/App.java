package codecheck;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class App {
	public static void main(String[] args) {
//		System.setProperty("proxySet", "true");
//		System.setProperty("proxyHost", "tkyproxy.intra.tis.co.jp");
//		System.setProperty("proxyPort", "8080");
		
		StringBuilder urlString = new StringBuilder("http://challenge-server.code-check.io/api/hash?q=");
		urlString.append(args[0]);
		
		HttpURLConnection urlConn = null;
		BufferedReader bufferedReader = null;
		
		try {
			URL url = new URL(urlString.toString());
			urlConn = (HttpURLConnection) url.openConnection();
			urlConn.setRequestMethod("GET");
			urlConn.connect();
			
			bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
			String line;
			while((line = bufferedReader.readLine()) != null) {
				Map<String, String> jsonMap = decode(line);
				System.out.println(jsonMap.get("hash"));
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if (urlConn != null) {
				urlConn.disconnect();
			}
			
			try {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static Map<String, String> decode(String json) {
		Map<String, String> jsonMap = new HashMap<String, String>();
		String strArray[] = json.substring(1, json.length() -1).split(",");
		
		for (String str : strArray) {
			String element[] = str.split(":");
			String key = element[0].substring(1, element[0].length() -1);
			String value = element[1].substring(1, element[1].length() -1);
			
			jsonMap.put(key, value);
		}
		
		return jsonMap;
	}
}
