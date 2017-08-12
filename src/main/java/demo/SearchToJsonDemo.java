package demo;

import java.util.List;

import bing.BingSearch;
import bing.JsonToSearchHits;
import config.Parameters;
import config.ParametersTabImpl;
import data.SearchHit;
import text.Stopper;
import text.StopperBasic;

/**
 * Demo class that runs a query against the Bing API.  The class prints the raw JSON returned
 * by the API call and cleaned-up versions of the ranked URLs.
 * 
 * @author Miles Efron
 *
 */
public class SearchToJsonDemo {

	/**
	 * 
	 * @param args A resolvable path to a parameter file formatted for {@link config.ParametersTabImpl}
	 * @throws Exception If the parameter file is ill-formed
	 */
	public static void main(String[] args) throws Exception {
		// setup our parameters
		String pathToParams = args[0];
		ParametersTabImpl params = new ParametersTabImpl();
		params.setPathToParamSource(pathToParams);
		params.readParams();
		
		// initialize and run our call to the API.
		BingSearch search = new BingSearch(params.getParamValue(Parameters.PARAM_NAME_USER_KEY));
		search.setResultCount(Integer.parseInt(params.getParamValue(Parameters.PARAM_NAME_COUNT)));
		search.setOffset(Integer.parseInt(params.getParamValue(Parameters.PARAM_NAME_OFFSET)));
		search.runQuery(params.getParamValue(Parameters.PARAM_NAME_QUERY_TO_RUN));
		String json = search.getResultsAsJson();
		

		// print the raw JSON output
		System.out.println(json);
		
		// grab the URLs of our retrieved documents.
		Stopper stopper = new StopperBasic();
	    List<SearchHit> hits = JsonToSearchHits.toSearchHits(json, stopper);
	    for(SearchHit hit : hits)
	    		System.out.println(hit.getUrl());
	}
}
