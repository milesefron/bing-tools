package utils;

import java.util.ArrayList;
import java.util.List;

public class StatUtils {
	
	/**
	 * Calculates the exponential distribution's density.
	 * 
	 * @param x The value that we want to calculate the density for.
	 * @param lambda The rate parameter of this exponential distribution
	 * @return
	 */
	public static double dexp(double x, double lambda) {
		if(x < 0)
			return 0.0;
		
		return lambda * Math.pow(Math.E, -1.0 * lambda * x);
	}
	
	public static List<Double> normalize(List<Double> x) {
		List<Double> normalized = new ArrayList<Double>(x.size());
		double sum = 0.0;
		for(Double d : x)
			sum += d;
		
		for(Double d: x)
			normalized.add(d / sum);
		
		return normalized;
	}
}
