package ir;

import java.util.ArrayList;
import java.util.List;

import data.FeatureVector;
import data.SearchHit;
import utils.StatUtils;

public class RelevanceModel {
	/**
	 * Since Bing doesn't give us query-document scores/probabilities, we approximate them
	 * with an exponential distribution on the rank of each search result.
	 */
	public double exponentialRateParam = 1.0;
	
	public FeatureVector getModel(List<SearchHit> hits) {
		FeatureVector rm = new FeatureVector();
		List<Double> scores = new ArrayList<Double>(hits.size());
		double i=1.0;
		for(@SuppressWarnings("unused") SearchHit hit : hits)
			scores.add(StatUtils.dexp(i++, exponentialRateParam));
		scores = StatUtils.normalize(scores);
		
		List<FeatureVector> vectors = new ArrayList<FeatureVector>(hits.size());
		for(SearchHit hit : hits) {
			FeatureVector v = hit.getTextVector();
			v.normalize();
			vectors.add(v);
		}
		rm = FeatureVector.interpolate(scores, vectors);
		return rm;
	}
	
	public void setExponentialRateParam(double lambda) {
		this.exponentialRateParam = lambda;
	}
}
