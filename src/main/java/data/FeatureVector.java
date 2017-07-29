package data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import text.TextUtils;
import utils.MapUtils;

public class FeatureVector {
	private Map<String,Double> vector;
	private boolean cleanText = true;
	private boolean stopText  = true;
	
	public FeatureVector() {
		vector = new HashMap<String,Double>();
	}
	
	public void addText(String text) {
		if(cleanText)
			text = TextUtils.clean(text);
		List<String> terms;
		
		
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
}
