package similarity;

public class SimilarityFactory {
	private static SimilarityMeasure measure = new TanimotoSimilarityMeasure();
	
	public static SimilarityMeasure getSimilarityMeasure() {
		return measure;
	}
}
