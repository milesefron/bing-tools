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
	
	/** 
	 * Adds zero or more words to this {@link data.SearchHit}'s {@link data.FeatureVector}.
	 * @param text zero or more words to be added to this object's feature vector.
	 */
	public void addTextToVector(String text) {
		vector.addText(text);
	}
	
	/**
	 * Provides access to a vector representation of all the text associated with this search hit.
	 * @return An object of type {@link data.FeatureVector}, containing the URL, snippet and title text from this hit.
	 */
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

	/**
	 * Returns the query-document similarity score for this Search Hit.  If we're not re-scoring our
	 * documents, search hits obtained from the Bing API have no value for this field.  Unless you
	 * specifically set a search hit's score, its score value==0;
	 * @return A query-document similarity score.
	 */
	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}
	
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append(score   + System.lineSeparator());
		b.append(url     + System.lineSeparator());
		b.append(title   + System.lineSeparator());
		b.append(snippet + System.lineSeparator());
		return b.toString();
	}
	
}
