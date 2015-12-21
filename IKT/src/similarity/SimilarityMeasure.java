package similarity;

public interface SimilarityMeasure {
	double getSimilarity(Integer v1Module, Integer v2Module, Integer intersect);
}
