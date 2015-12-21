package prediction;

import java.util.HashMap;
import java.util.Map;

import prediction.helper.HotnessTags;
import prediction.helper.PredictionDataAccess;
import prediction.helper.PredictionDataAccessFactory;
import similarity.SimilarityFactory;
import similarity.SimilarityMeasure;
import utility.Tag;

public class PredictImpl implements Predict {
	private PredictionDataAccess dataAccess;
	private Map<String, Integer> finalTags;
	private Map<Integer, Integer> songNumberOfTags;
	private SimilarityMeasure measure;

	public PredictImpl() {
		dataAccess = PredictionDataAccessFactory.getDataAccess();
		finalTags = dataAccess.getTags();
		songNumberOfTags = new HashMap<Integer, Integer>();
		measure = SimilarityFactory.getSimilarityMeasure();
		calculateNumberOfTags();
	}
	
	public void calculateNumberOfTags() {
		for (Map.Entry<String, Integer> tag : finalTags.entrySet()) {
			Map<Integer, Double> songs = dataAccess.getTracks(tag.getValue());
			for (Map.Entry<Integer, Double> song : songs.entrySet()) {
				Integer prev = songNumberOfTags.get(song.getKey());
				if (prev == null)
					prev = 0;
				songNumberOfTags.put(song.getKey(), prev + 1);
			}
		}
	}
	
	@Override
	public Double predictHotness(Tag[] tags) {
		Integer songModule = tags.length;
		if (songModule == 0)
			return 0.0;
		Map<Integer, HotnessTags> songHotness = new HashMap<Integer, HotnessTags>();
		HotnessTags hotnessTag;
		for (Tag tag : tags) {
			Map<Integer, Double> songs = dataAccess.getTracks(finalTags.get(tag.getName()));
			for (Map.Entry<Integer, Double> song : songs.entrySet()) {
				hotnessTag = songHotness.get(song.getKey());
				if (hotnessTag == null)
					songHotness.put(song.getKey(), new HotnessTags(song.getValue()));
				else
					hotnessTag.addTag();
			}
		}
		
		double hotness = 0;
		double similaritySum = 0;
		
		for (Map.Entry<Integer, HotnessTags> song : songHotness.entrySet()) {
			System.out.println(song.getValue().getHotness());
			double similarity = measure.getSimilarity(songModule, songNumberOfTags.get(song.getKey()), song.getValue().getNumberOfTags());
			hotness += song.getValue().getHotness() * similarity;
			similaritySum += similarity;
		}
		if (similaritySum == 0)
			return 0.0;
		return hotness/similaritySum;
	}
	
	public static void main(String[] args) {
		Predict predict = new PredictImpl();
		Tag[] tags = {new Tag("love", null, null), new Tag("rock", null, null)};
		System.out.println(predict.predictHotness(tags));
	}
}
