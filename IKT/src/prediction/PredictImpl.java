package prediction;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import prediction.helper.HotnessListTags;
import prediction.helper.HotnessTags;
import prediction.helper.PredictionDataAccess;
import prediction.helper.PredictionDataAccessFactory;
import similarity.SimilarityFactory;
import similarity.SimilarityMeasure;
import utility.Tag;

public class PredictImpl implements Predict {
	private PredictionDataAccess dataAccess;
	private Map<String, Integer> finalTags;
	private Map<Integer, HotnessListTags> songTags;
	private Map<Integer, Map<Integer, Double>> tagSongs;
	private SimilarityMeasure measure;

	public PredictImpl() {
		dataAccess = PredictionDataAccessFactory.getDataAccess();
		finalTags = dataAccess.getTags();
		measure = SimilarityFactory.getSimilarityMeasure();
		songTags = new HashMap<Integer, HotnessListTags>();
		tagSongs = new HashMap<Integer, Map<Integer, Double>>();
		calculateNumberOfTags();
	}
	
	public void calculateNumberOfTags() {
		for (Map.Entry<String, Integer> tag : finalTags.entrySet()) {
			Map<Integer, Double> songs = dataAccess.getTracks(tag.getValue());
			tagSongs.put(tag.getValue(), songs);
			for (Map.Entry<Integer, Double> song : songs.entrySet()) {
				HotnessListTags listTags = songTags.get(song.getKey());
				if (listTags == null) {
					listTags = new HotnessListTags(song.getValue());
					songTags.put(song.getKey(), listTags);
				}
				listTags.addTag(tag.getValue());
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
			double similarity = measure.getSimilarity(songModule, songTags.get(song.getKey()).getTags().size(), song.getValue().getNumberOfTags());
			hotness += song.getValue().getHotness() * similarity;
			similaritySum += similarity;
		}
		if (similaritySum == 0)
			return 0.0;
		return hotness/similaritySum;
	}
	
	public double predictHotness(List<Integer> tags) {
		Integer songModule = tags.size();
		if (songModule == 0)
			return 0.0;
		Map<Integer, HotnessTags> songHotness = new HashMap<Integer, HotnessTags>();
		HotnessTags hotnessTag;
		for (Integer tag : tags) {
			Map<Integer, Double> songs = tagSongs.get(tag);
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
			double similarity = measure.getSimilarity(songModule, songTags.get(song.getKey()).getTags().size(), song.getValue().getNumberOfTags());
			hotness += song.getValue().getHotness() * similarity;
			similaritySum += similarity;
		}
		if (similaritySum == 0)
			return 0.0;
		return hotness/similaritySum;
	}
	
	public Map<Integer, HotnessListTags> getSongTags() {
		return songTags;
	}
	
	public static void main(String[] args) {
		Predict predict = new PredictImpl();
		Tag[] tags = {new Tag("love", null, null), new Tag("femal", null, null)};
		System.out.println(predict.predictHotness(tags));
	}
}
