package similarity;

public class TanimotoSimilarityMeasure implements SimilarityMeasure {

	@Override
	public double getSimilarity(Integer v1Module, Integer v2Module, Integer intersect) {
		return intersect / (v1Module + v2Module - intersect);
	}
	
}
