package data;

/**
 * Simple container for representing a single web search result.
 * @author Miles Efron
 *
 */
public class SearchHit {
	private String url;
	private String title;
	private String snippet;
	private FeatureVector vector;
	
	private double score = 0.0;

	public SearchHit() {
		vector = new FeatureVector();
	}
	
	public void addTextToVector(String text) {
		vector.addText(text);
	}
	
	public FeatureVector getTextVector() {
		return vector;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSnippet() {
		return snippet;
	}

	public void setSnippet(String snippet) {
		this.snippet = snippet;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}
	
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append(score   + System.lineSeparator());
		b.append(url     + System.lineSeparator());
		b.append(title   + System.lineSeparator());
		b.append(snippet + System.lineSeparator());
		return b.toString();
	}
	
}
