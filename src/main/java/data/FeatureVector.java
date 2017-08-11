package data;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import text.Stopper;
import text.TextUtils;
import utils.MapUtils;

/**
 * Represents text as a bag of words.  Provides methods that allow clients to
 * alter, access, and pretty-print the data.
 * @author Miles Efron
 *
 */
public class FeatureVector {
	private DecimalFormat decimalFormat = new DecimalFormat("#.#######");
	private Map<String,Double> vector;
	private Stopper stopper = null;
	
	/**
	 * Only used if we are dealing with named documents.  This field stores the title of the 
	 * document we are representing.
	 */
	private String textTitle;
	
	private Boolean containsCountData = true;

	private boolean enforceMinWordLength = true;
	
	

	public FeatureVector() {
		vector = new HashMap<String,Double>();
	}
	
	/**
	 * Allows us to pass in a (possibly long) text string for insertion into this vector.
	 * This method will clean up the text in various ways (case-folding, applying a stoplist,
	 * removing punctuation and extra whitespace, removing very short 'words').
	 * @param text A raw string of 0 or more words
	 */
	public void addText(String text) {
		List<String> terms = TextUtils.clean(text, stopper);
		for(String term : terms)
			addFeature(term);
	}
	
	/**
	 * Adds a single feature (e.g. a word) to the vector.  This is useful if a client
	 * needs to bypass the text cleaning in the addText method.
	 * @param feature A feature to add to the vector.  No cleanup is done on this feature; it
	 * will be inserted exactly as-is.  Additionally, inserting a feature via this method increases
	 * the feature's count in the vector.
	 */
	public void addFeature(String feature) throws IllegalStateException {
		// make sure we're in a state where incrementing feature counts is sensible.
		if(!containsCountData) {
			throw new IllegalStateException("Can't increment the count for " + feature + 
					" in FeatureVector.addFeature because the values in the vector are not count data at this time.");
		}
		Double current = vector.get(feature);
		if(current == null)
			vector.put(feature, 1.0);
		else
			vector.put(feature, current + 1.0);
	}
	
	public void setFeatureValue(String feature, Double value) {
		vector.put(feature, value);
	}
	
	
	/**
	 * Coerces the frequency values in the vector to lie in [0,1] such that all stored values
	 * sum to 1...in other words, this method turns the vector into a language model (i.e. a
	 * multinomial over the indexing vocabulary).
	 */
	public void normalize() {
		vector = MapUtils.normalize(vector);
		
		// note that we are no longer dealing with count data
		containsCountData = false;
	}
	
	/**
	 * Provides item-level access to the values stored in the vector.
	 * @param feature the name of a feature (e.g. a word) that we want to look up in the vector representation
	 * @return the current stored value for this feature
	 */
	public double getValue(String feature) {
		if(vector.containsKey(feature))
			return vector.get(feature);
		return 0.0;
	}
	
	public Set<String> getFeatures() {
		return vector.keySet();
	}
	
	public int getFeatureCount() {
		if(vector == null)
			return 0;
		return vector.size();
	}
	
	
	
	/**
	 * Truncates the vector so that it contains at most k entries.
	 * N.B. This method is relatively expensive.  It involves sorting
	 * all of the vector's key-value pairs and copying the largest k of
	 * them into a brand-new data structure.
	 * 
	 * @param k the maximum number of values to retain in this vector.  
	 */
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
	

	/**
	 * Calculates the L2 norm of this vector (i.e. the vector length).
	 * @return The length of vector expressed in L2 terms.
	 */
	public double l2norm() {
		double norm = 0.0;
		for(String feature : vector.keySet())
			norm += Math.pow(vector.get(feature), 2.0);
		return Math.sqrt(norm);
	}
	
	public void setStopper(Stopper stopper) {
		this.stopper = stopper;
	}
	
	/** 
	 * Static method to take the cosine of the angle between two vectors, x and y.
	 * N.B. This operation is symmetrical, so FeatureVector.cosine(x,y) = FeatureVector.cosine(y,x).
	 * 
	 * @param x an object of class FeatureVector
	 * @param y an object of class FeatureVector
	 * @return the cosine of the angle between x and y
	 */
	public static double cosine(FeatureVector x, FeatureVector y) {
		double xnorm = x.l2norm();
		double ynorm = y.l2norm();
		
		Set<String> vocab = new HashSet<String>();
		vocab.addAll(x.getFeatures());
		vocab.addAll(y.getFeatures());
		
		double dotproduct = 0.0;
		for(String feature : vocab)
			dotproduct += x.getValue(feature) * y.getValue(feature);
		
		return dotproduct / (xnorm * ynorm);
	}
	
	public String getTitle() {
		return textTitle;
	}
	public void setTitle(String textTitle) {
		this.textTitle = textTitle;
	}
	public void setEnforceMinWordLength(boolean enforceMinWordLength) {
		this.enforceMinWordLength = enforceMinWordLength;
	}

	
	public static boolean equalData(FeatureVector x, FeatureVector y) {
		if(x.getFeatureCount() != y.getFeatureCount())
			return false;
		if(!x.getTitle().equals(y.getTitle()))
			return false;
		
		Set<String> vocab = new HashSet<String>();
		vocab.addAll(x.getFeatures());
		vocab.addAll(y.getFeatures());
		//System.out.println(vocab);
		for(String feature : vocab) {
			//System.out.println(feature + "\t\t" + x.getValue(feature) + "\t" + y.getValue(feature));
			if(x.getValue(feature) != y.getValue(feature))
				return false;
		}
		
		return true;
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
}
