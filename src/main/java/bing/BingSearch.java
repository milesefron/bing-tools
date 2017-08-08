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
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import data.SearchHit;

/**
 * Provides access to functionality delivered by v5.0 of the Bing Web Search API.
 * The official Microsoft documentation on the API is available at:
 * https://docs.microsoft.com/en-us/azure/cognitive-services/bing-web-search/search-the-web
 * 
 * @author Miles Efron
 *
 */
public class BingSearch {

	/**
	 * hostname of the bing API
	 */
	public static final String API_HOST = "https://api.cognitive.microsoft.com";
	
	/**
	 * path to the API endpoint
	 */
	public static final String API_PATH = "/bing/v5.0/search?";
	
	/**
	 * the name of the 'query' command in the API URL
	 */
	public static final String API_QUERY_ARG = "q";
	
	/**
	 * a valid key for version 5.0 of the Bing search API
	 */
	public static final String API_KEY_ARG = "Ocp-Apim-Subscription-Key";
	
	/**
	 * how many results to retrieve (max)
	 */
	public static final String API_COUNT_ARG = "count";
	
	/**
	 * starting index of the hits we want (for paging through results)
	 */
	public static final String API_OFFSET_ARG = "offset";
	
	/**
	 * the 'market' argument to the API call
	 */
	public static final String API_MKT_ARG = "mkt";
	
	/**
	 * the level of the search's "safesearch": Off, Moderate, or On;
	 */
	public static final String API_SAFE_ARG = "safesearch";
	
	/**
	 * the name of the top-level part of the returned JSON
	 */
	public static final String API_JSON_KEY_1 = "webPages";
	
	/**
	 * the name of the second part of the returned JSON
	 */
	public static final String API_JSON_KEY_2 = "value";
	
	/**
	 * the name of each hit's URL field
	 */
	public static final String RESULTS_FIELD_URL = "url";
	
	/**
	 * the name of each hit's title field
	 */
	public static final String RESULTS_FIELD_NAME = "name";
	
	/**
	 * the name of each hit's snippet field
	 */
	public static final String RESULTS_FIELD_SNIPPET = "snippet";
	
	/**
	 * regex used for cleaning retrieved URLs
	 */
	public static final String REGEX_URL1 = "http.*&v=1&r=";
	
	/**
	 * regex used for cleaning retrieved URLs
	 */
	public static final String REGEX_URL2 = "&p=.*";

	/**
	 * Pattern used to clean up URLs
	 */
	public static final Pattern URL_PATTERN1 = Pattern.compile(BingSearch.REGEX_URL1);
	
	/**
	 * Pattern used to clean up URLs
	 */
	public static final Pattern URL_PATTERN2 = Pattern.compile(BingSearch.REGEX_URL2);
	
	private String userSubscriptionID;
	
	private int resultCount = 10;
	private int offset = 0;
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
	 * Runs a query and fetches the results as a JSON String
	 * @param queryString The keyword query we're going to run
	 * @return Search results encoded as a JSON object in the MS-defined format returned by the API endpoint
	 * @throws Exception Network or formatting problems.
	 */
	public String runQueryToJson(String queryString) throws Exception {
		String json = runQuery(queryString);
		return json;
	}
	
	/**
	 * Runs a query and fetches the results as a List of {@link data.SearchHit} objects.
	 * @param queryString The keyword query we're going to run
	 * @return A Java {@link java.util.List} object of {@link data.SearchHit}s containing the search results
	 * @throws Exception Network or formatting problems.
	 */
	public List<SearchHit> runQueryToSearchHits(String queryString) throws Exception {
		String json = runQuery(queryString);
		
		List<SearchHit> hits = new LinkedList<SearchHit>();
		
		// used to generate placeholder document scores
		int rank = 1;

		JSONParser parser = new JSONParser();
		JSONObject jsonObject = (JSONObject) parser.parse(json);
		JSONObject temp = (JSONObject) jsonObject.get(BingSearch.API_JSON_KEY_1);
		JSONArray jsonArray = (JSONArray) temp.get(BingSearch.API_JSON_KEY_2);
		@SuppressWarnings("unchecked")
		Iterator<JSONObject> it = jsonArray.iterator();
		while(it.hasNext()) {
			SearchHit hit = new SearchHit();
			
			// use a made-up score for now
			hit.setScore(1.0 / rank++);
			
			jsonObject = it.next();
			hit.setTitle((String)jsonObject.get(BingSearch.RESULTS_FIELD_NAME));
			hit.addTextToVector(hit.getTitle());
			String url = (String)jsonObject.get(BingSearch.RESULTS_FIELD_URL);
			url = url.replaceFirst(BingSearch.REGEX_URL1, "");
			url = url.replaceAll(BingSearch.REGEX_URL2, "");
			url = URLDecoder.decode(url, "UTF-8");
			hit.setUrl(url);
			hit.addTextToVector(hit.getUrl());
			
			hit.setSnippet((String)jsonObject.get(BingSearch.RESULTS_FIELD_SNIPPET));
			hit.addTextToVector(hit.getSnippet());
			
			hits.add(hit);
		}

		
		return hits;
	}
	
	
	
	

	private String runQuery(String queryString) throws Exception {
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
	    
	    
	    return jsonString;
	}
	
	private URI getSearchURI() {
		URI uri = null;
		try {
			uri = URI.create(BingSearch.API_HOST + 
								BingSearch.API_PATH + 
								BingSearch.API_COUNT_ARG  + "=" + resultCount + "&" +
								BingSearch.API_OFFSET_ARG + "=" + offset + "&" + 
								BingSearch.API_MKT_ARG    + "=" + market + "&" +
								BingSearch.API_SAFE_ARG   + "=" + safesearch + "&" + 
								BingSearch.API_QUERY_ARG  + "=" + URLEncoder.encode(queryString, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return uri;
	}
	
	private void checkStatus() throws Exception {
		if(userSubscriptionID == null) {
		   String message = "Trouble with API credentials." + System.lineSeparator() +
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
	public void setOffset(int offset) {
		this.offset = offset;
	}
	public void setMarket(String market) {
		this.market = market;
	}
	public void setSafesearch(String safesearch) {
		this.safesearch = safesearch;
	}
	
	
}
