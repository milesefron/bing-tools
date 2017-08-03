package bing;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	public static final String API_JSON_KEY_1 = "webPages";
	public static final String API_JSON_KEY_2 = "value";
	
	public static final String RESULTS_FIELD_URL = "url";
	public static final String RESULTS_FIELD_NAME = "name";
	public static final String RESULTS_FIELD_SNIPPET = "snippet";
	
	public static final String REGEX_URL1 = "http.*&v=1&r=";
	public static final String REGEX_URL2 = "&p=.*";

	private Pattern pattern1 = Pattern.compile(BingSearch.REGEX_URL1);
	private Pattern pattern2 = Pattern.compile(BingSearch.REGEX_URL2);
	
	private String userSubscriptionID;
	
	private int resultCount = 10;
	private String market = "en-us";
	private String safesearch = "Off";
	
	private String queryString;
	
	
	/**
	 * 
	 * @param userSubscriptionID A valid, active Bing web search API key (v. 5.0)
	 */
	public BingSearch(String userSubscriptionID) {
		this.userSubscriptionID = userSubscriptionID;
	}
	
	/**
	 * Runs a query against the Bing Web Search API.
	 * @param queryString
	 * @throws Exception
	 */
	public void runQuery(String queryString) throws Exception {
		this.queryString = queryString;
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
	        builder.append(System.lineSeparator());
	      }
	    
	    String jsonString = builder.toString();
	    
	    System.out.println(jsonString);
	    
	    JSONParser parser = new JSONParser();
	    JSONObject jsonObject = (JSONObject) parser.parse(jsonString);
	    JSONObject temp = (JSONObject) jsonObject.get("webPages");
	    JSONArray jsonArray = (JSONArray) temp.get("value");
	    @SuppressWarnings("unchecked")
		Iterator<JSONObject> it = jsonArray.iterator();
	    while(it.hasNext()) {
	    		jsonObject = it.next();
	    			    		
	    		String url = (String) jsonObject.get(BingSearch.RESULTS_FIELD_URL);
	    		
	    		Matcher matcher = pattern1.matcher(url);
	    		url = matcher.replaceFirst(""); 
	    		
	    		matcher = pattern2.matcher(url);
	    		url = matcher.replaceFirst(""); 
	    		url = URLDecoder.decode(url, "UTF-8");
	    		
	    		System.out.println(url);

	    		//System.out.println(jsonObject.get(BingSearch.RESULTS_FIELD_NAME));
	    		//System.out.println(jsonObject.get(BingSearch.RESULTS_FIELD_SNIPPET) + System.lineSeparator());
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
	
	
}
