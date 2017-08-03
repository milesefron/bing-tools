package data;

public class Tuple implements Comparable<Tuple> {
	private String key;
	private Double value;
	
	public Tuple(String key, double value) {
		this.key = key;
		this.value = value;
	}
	
	
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
