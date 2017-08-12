package bing;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import data.SearchHit;

/**
 * Translates search results stored in a JSON object as returned by the Bing API into
 * a (@link java.util.List} of {link data.SearchHit} objects.
 * 
 * @author Miles Efron
 *
 */
public class JsonToSearchHits {

	/**
	 * 
	 * @param json JSON output of a web search
	 * @return
	 * @throws ParseException
	 * @throws UnsupportedEncodingException
	 */
	public static List<SearchHit> toSearchHits(String json) throws ParseException, UnsupportedEncodingException {
		List<SearchHit> hits = new LinkedList<SearchHit>();
		

			JSONParser parser = new JSONParser();
			JSONObject jsonObject = (JSONObject) parser.parse(json);
			JSONObject temp = (JSONObject) jsonObject.get(BingSearch.API_JSON_KEY_1);
			JSONArray jsonArray = (JSONArray) temp.get(BingSearch.API_JSON_KEY_2);
			@SuppressWarnings("unchecked")
			Iterator<JSONObject> it = jsonArray.iterator();
			double rank = 1.0;
			while(it.hasNext()) {
				SearchHit hit = new SearchHit();
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
				
				// use a made-up score for now
				hit.setScore(1.0 / rank++);
				hits.add(hit);
			}
		
			return hits;

	}
}
