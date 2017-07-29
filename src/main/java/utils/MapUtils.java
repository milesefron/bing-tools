package utils;

import java.util.HashMap;
import java.util.Map;

public class MapUtils {
	
	public static double sum(Map<String,Double> map) {
		double sum = 0.0;
		for(String key : map.keySet())
			sum += map.get(key);
		return sum;
	}
	
	public static Map<String,Double> normalize(Map<String,Double> input) {
		Map<String,Double> output = new HashMap<String,Double>(input.size());
		double sum = MapUtils.sum(input);
		for(String key : input.keySet())
			output.put(key, input.get(key) / sum);
		return output;
	}
}
