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
 * a (@link java.util.List} of {@data SearchHit} objects.
 * 
 * @author Miles Efron
 *
 */
public class JsonToSearchHits {

	public static List<SearchHit> toSearchHits(String json) throws ParseException, UnsupportedEncodingException {
		List<SearchHit> hits = new LinkedList<SearchHit>();
		

			JSONParser parser = new JSONParser();
			JSONObject jsonObject = (JSONObject) parser.parse(json);
			JSONObject temp = (JSONObject) jsonObject.get(BingSearch.API_JSON_KEY_1);
			JSONArray jsonArray = (JSONArray) temp.get(BingSearch.API_JSON_KEY_2);
			@SuppressWarnings("unchecked")
			Iterator<JSONObject> it = jsonArray.iterator();
			while(it.hasNext()) {
				SearchHit hit = new SearchHit();
				jsonObject = it.next();
				hit.setTitle((String)jsonObject.get(BingSearch.RESULTS_FIELD_NAME));
				
				String url = (String)jsonObject.get(BingSearch.RESULTS_FIELD_URL);
				url = url.replaceFirst(BingSearch.REGEX_URL1, "");
				url = url.replaceAll(BingSearch.REGEX_URL2, "");
				url = URLDecoder.decode(url, "UTF-8");
				hit.setUrl(url);

				hit.setSnippet((String)jsonObject.get(BingSearch.RESULTS_FIELD_SNIPPET));
				
				hits.add(hit);
			}
		
			return hits;

	}
}
