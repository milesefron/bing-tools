package demo;

import java.util.LinkedList;
import java.util.List;

import bing.BingSearch;
import bing.JsonToSearchHits;
import config.Parameters;
import config.ParametersTabImpl;
import data.SearchHit;
import text.Stopper;
import text.StopperBasic;

/**
 * Demo class that retrieved many results for a keyword query by repeated calls to the
 * Bing API using the paging/offset functionality of the API.
 * 
 * @author Miles Efron
 *
 */
public class SearchPagingDemo {
	public static final int NUMBER_OF_SEARCHES_TO_RUN = 2;
	public static final int NUMBER_OF_HITS_PER_SEARCH = 10;
	
	/**
	 * 
	 * @param args A resolvable path to a parameter file formatted for {@link config.ParametersTabImpl}
	 * @throws Exception If the parameter file is ill-formed
	 */
	public static void main(String[] args) throws Exception {
		// parse our parameter file
		String pathToParams = args[0];
		ParametersTabImpl params = new ParametersTabImpl();
		params.setPathToParamSource(pathToParams);
		params.readParams();
		
		// initialize our Bing search object
		BingSearch search = new BingSearch(params.getParamValue(Parameters.PARAM_NAME_USER_KEY));
		search.setResultCount(SearchPagingDemo.NUMBER_OF_HITS_PER_SEARCH);
		int offset = 0;
		search.setOffset(offset);
		
		List<SearchHit> results = new LinkedList<SearchHit>();
		String query = params.getParamValue(Parameters.PARAM_NAME_QUERY_TO_RUN);
		Stopper stopper = new StopperBasic();
		
		// run our searches against the API
		for(int i=0; i<SearchPagingDemo.NUMBER_OF_SEARCHES_TO_RUN; i++) {
			search.setOffset(offset);
			search.setResultCount(SearchPagingDemo.NUMBER_OF_HITS_PER_SEARCH);
			search.runQuery(query);
			String json = search.getResultsAsJson();
			List<SearchHit> rr = JsonToSearchHits.toSearchHits(json, stopper);
			results.addAll(rr);
			System.out.println((offset+1) + " - " + ((int)offset+rr.size()));
			offset += rr.size();
		}
		
		int i=1;
		for(SearchHit hit : results) {
			System.out.println(i++ + ") " + hit.getTitle());
			System.out.println(hit.getUrl() + System.lineSeparator());
		}
	}
}
