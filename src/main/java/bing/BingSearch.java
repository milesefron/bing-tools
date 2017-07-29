package bing;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class BingSearch {

	public static final String API_HOST = "https://api.cognitive.microsoft.com";
	public static final String API_PATH = "/bing/v5.0/search?";
	public static final String API_QUERY_ARG = "q";
	public static final String API_KEY_ARG = "Ocp-Apim-Subscription-Key";
	public static final String API_COUNT_ARG = "count";
	public static final String API_MKT_ARG = "mkt";
	public static final String API_SAFE_ARG = "safesearch";
	
	private String userSubscriptionID;
	
	private int resultCount = 10;
	private String market = "en-us";
	private String safesearch = "Off";
	
	private String queryString;
	
	
	public BingSearch(String userSubscriptionID) {
		this.userSubscriptionID = userSubscriptionID;
	}
	
	public void runSearch() throws Exception {
		checkStatus();
		
		URI searchURI = getSearchURI();		
		URL searchURL = searchURI.toURL();
		
		
		HttpURLConnection conn = (HttpURLConnection) searchURL.openConnection();
		conn.setRequestProperty(API_KEY_ARG, userSubscriptionID);
		
		
		InputStream is = conn.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder builder = new StringBuilder();
		String line;
	    while ((line = reader.readLine()) != null) {
	        builder.append(line);
	        builder.append('\r');
	      }
	    
	    String jsonString = builder.toString();
	    
	    //System.out.println(jsonString);
	    //System.exit(1);
	    
	    JSONParser parser = new JSONParser();
	    JSONObject jsonObject = (JSONObject) parser.parse(jsonString);
	    JSONObject temp = (JSONObject) jsonObject.get("webPages");
	    JSONArray jsonArray = (JSONArray) temp.get("value");
	    Iterator<JSONObject> it = jsonArray.iterator();
	    while(it.hasNext()) {
	    		jsonObject = it.next();
	    		System.out.println(jsonObject.get("name"));
	    		System.out.println(jsonObject.get("displayUrl"));
	    		System.out.println(jsonObject.get("snippet") + "\n");
	    }
	    
	}
	
	private URI getSearchURI() {
		URI uri = null;
		try {
			uri = URI.create(API_HOST + 
					             API_PATH + 
					             API_COUNT_ARG + "=" + resultCount + "&" +
					             API_MKT_ARG   + "=" + market + "&" +
					             API_SAFE_ARG  + "=" + safesearch + "&" + 
					             API_QUERY_ARG + "=" + URLEncoder.encode(queryString, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return uri;
	}
	
	private void checkStatus() throws Exception {
		if(userSubscriptionID == null) {
		   String message = "Trouble with API credentials." + "\n" +
							"userSubscriptionID: " + userSubscriptionID;		
			throw new Exception(message);
		}	
	}
		
	/* GETTERS AND SETTERS */

	public void setUserSubscriptionID(String userSubscriptionID) {
		this.userSubscriptionID = userSubscriptionID;
	}
	public void setResultCount(int resultCount) {
		this.resultCount = resultCount;
	}
	public void setMarket(String market) {
		this.market = market;
	}
	public void setSafesearch(String safesearch) {
		this.safesearch = safesearch;
	}
	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}
	
	
}
