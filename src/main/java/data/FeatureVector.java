package data;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import text.TextUtils;
import utils.MapUtils;

public class FeatureVector {
	private DecimalFormat decimalFormat = new DecimalFormat("#.#######");
	
	private Map<String,Double> vector;

	
	public FeatureVector() {
		vector = new HashMap<String,Double>();
	}
	
	public void addText(String text) {
		List<String> terms = TextUtils.clean(text);
		for(String term : terms)
			addFeature(term);
	}
	
	public void addFeature(String feature) {
		Double current = vector.get(feature);
		if(current == null)
			vector.put(feature, 1.0);
		else
			vector.put(feature, current + 1.0);
	}
	
	public void normalize() {
		vector = MapUtils.normalize(vector);
	}
	
	public double getValue(String feature) {
		if(vector.containsKey(feature))
			return vector.get(feature);
		return 0.0;
	}
	
	public Set<String> getFeatures() {
		return vector.keySet();
	}
	
	public void clip(int k) {
		List<Tuple> tuples = getOrderedTuples();
		vector.clear();
		int i=0;
		for(Tuple tuple : tuples) {
			if(++i > k)
				return;
			vector.put(tuple.getKey(), tuple.getValue());
		}
	}
	
	public String toString() {
		List<Tuple> tuples = getOrderedTuples();
		
		StringBuilder builder = new StringBuilder();
		
		for(Tuple tuple : tuples)
			builder.append(decimalFormat.format(tuple.getValue()) + "\t\t" + tuple.getKey() + System.lineSeparator());
		
		return builder.toString();
	}
	
	private List<Tuple> getOrderedTuples() {
		List<Tuple> tuples = new LinkedList<Tuple>();
		for(String feature : vector.keySet()) {
			Tuple tuple = new Tuple(feature, vector.get(feature));
			tuples.add(tuple);
		}
		
		Collections.sort(tuples, new TupleComparator());
		
		return tuples;
	}
	
	
	public double l2norm() {
		double norm = 0.0;
		for(String feature : vector.keySet())
			norm += Math.pow(vector.get(feature), 2.0);
		return Math.sqrt(norm);
	}
	
	public static double cosine(FeatureVector x, FeatureVector y) {
		double xnorm = x.l2norm();
		double ynorm = y.l2norm();
		
		Set<String> vocab = x.getFeatures();
		vocab.addAll(y.getFeatures());
		
		double dotproduct = 0.0;
		for(String feature : vocab)
			dotproduct += x.getValue(feature) * y.getValue(feature);
		
		return dotproduct / (xnorm * ynorm);
	}
}
