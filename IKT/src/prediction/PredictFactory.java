package prediction;

import java.util.Set;

public class PredictFactory {
	public static Predict getPredict(Set<Integer> songs) {
		return new PredictImpl(songs);
	}
	
	public static Predict getPredict(Set<Integer> songs, double similarityTrashold) {
		return new PredictImpl(songs, similarityTrashold);
	}
}
