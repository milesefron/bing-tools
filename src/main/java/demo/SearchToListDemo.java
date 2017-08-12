package demo;

import java.util.Collections;
import java.util.List;

import bing.BingSearch;
import bing.JsonToSearchHits;
import config.Parameters;
import config.ParametersTabImpl;
import data.SearchHit;
import data.SearchHitComparator;
import text.Stopper;
import text.StopperBasic;

/**
 * Demo class that runs a query against the Bing API.  The class retrieves the results as a
 * {@link java.util.List} containing one {@link data.SearchHit} per result.
 * 
 * @author Miles Efron
 *
 */
public class SearchToListDemo {

	
	public static void main(String[] args) throws Exception {
		// setup our parameters
		String pathToParams = args[0];
		ParametersTabImpl params = new ParametersTabImpl();
		params.setPathToParamSource(pathToParams);
		params.readParams();
		
		// run the query against the API
		BingSearch search = new BingSearch(params.getParamValue(Parameters.PARAM_NAME_USER_KEY));
		search.setResultCount(Integer.parseInt(params.getParamValue(Parameters.PARAM_NAME_COUNT)));
		search.setOffset(Integer.parseInt(params.getParamValue(Parameters.PARAM_NAME_OFFSET)));
		search.runQuery(params.getParamValue(Parameters.PARAM_NAME_QUERY_TO_RUN));
		List<SearchHit> hits = JsonToSearchHits.toSearchHits(search.getResultsAsJson());
		
		// technically we don't need to sort these since they are given to us in order...this line
		// is here simply to show how clients can sort SearchHits if they get re-scored.
		Collections.sort(hits, new SearchHitComparator());
		System.out.println(hits);
	}
}
