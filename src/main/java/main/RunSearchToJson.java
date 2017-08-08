package main;

import java.util.List;

import bing.BingSearch;
import bing.JsonToSearchHits;
import config.Parameters;
import config.ParametersTabImpl;
import data.SearchHit;

/**
 * Demo class that runs a query against the Bing API.  The class prints the raw JSON returned
 * by the API call and cleaned-up versions of the ranked URLs.
 * 
 * @author Miles Efron
 *
 */
public class RunSearchToJson {

	/**
	 * 
	 * @param args A resolvable path to a parameter file formatted for {@link config.ParametersTabImpl}
	 * @throws Exception If the parameter file is ill-formed
	 */
	public static void main(String[] args) throws Exception {
		String pathToParams = args[0];
		ParametersTabImpl params = new ParametersTabImpl();
		params.setPathToParamSource(pathToParams);
		params.readParams();
		
		BingSearch search = new BingSearch(params.getParamValue(Parameters.PARAM_NAME_USER_KEY));
		search.setResultCount(Integer.parseInt(params.getParamValue(Parameters.PARAM_NAME_COUNT)));
		search.setOffset(Integer.parseInt(params.getParamValue(Parameters.PARAM_NAME_OFFSET)));
		String json = search.runQueryToJson(params.getParamValue(Parameters.PARAM_NAME_QUERY_TO_RUN));
		
		System.out.println(json);
		
	    List<SearchHit> hits = JsonToSearchHits.toSearchHits(json);
	    for(SearchHit hit : hits)
	    		System.out.println(hit.getUrl());
	}
}
