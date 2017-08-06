package data;

/**
 * Mainly a utility class for various tasks needed by FeatureVector.  This class simply provides
 * support for storing and (more importantly) comparing key-value pairs.
 * @author Miles Efron
 *
 */
public class Tuple implements Comparable<Tuple> {
	private String key;
	private Double value;
	
	public Tuple(String key, double value) {
		this.key = key;
		this.value = value;
	}
	
	/**
	 * Used to support reverse sorting of a vector's key-value pairs in the FeatureVector class.
	 * If fed into a Comparator, this method will lead to sorting key-value pairs in DECREASING order
	 * of their stored values.
	 */
	public int compareTo(Tuple other) {
		return Double.compare(this.value, other.value);
	}
	public String getKey() {
		return key;
	}
	public Double getValue() {
		return value;
	}

}
