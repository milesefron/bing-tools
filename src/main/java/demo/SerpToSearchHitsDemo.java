package demo;

import java.io.File;
import java.io.FileInputStream;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import bing.BingSearch;
import data.SearchHit;
import data.SearchHitComparator;

/**
 * Reads the result of a single web search (i.e. one SERP) and uses the results to populate
 * a list of {@link data.SearchHit} objects, where they would be easy to manipulate.
 * 
 * @author Miles Efron
 *
 */
public class SerpToSearchHitsDemo {


	/**
	 * 
	 * @param args args[0] should contain a resolveable path to a file containing
	 * a single JSON file returned by the a single call to the Bing search API.
	 * @throws Exception This may be encountered if the supplied file is bogus.  
	 */
	public static void main(String[] args) throws Exception {
		File inFile = new File(args[0]);

		List<SearchHit> hits = new LinkedList<SearchHit>();



		Scanner in = new Scanner(new FileInputStream(inFile));
		String json = in.nextLine();
		in.close();

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

		Collections.sort(hits, new SearchHitComparator());
		System.out.println(hits);


	}
}
