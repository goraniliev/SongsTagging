package prediction;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import prediction.helper.TrackInfo;
import prediction.helper.HotnessTags;
import prediction.helper.PredictionDataAccess;
import prediction.helper.PredictionDataAccessFactory;
import similarity.SimilarityFactory;
import similarity.SimilarityMeasure;
import utility.Tag;

public class PredictImpl implements Predict {
	private PredictionDataAccess dataAccess;
	private Set<Integer> modelSongs;
	private SimilarityMeasure measure;
	private double similarityTrashold;

	public PredictImpl(Set<Integer> model, double similarityTrashold) {
		dataAccess = PredictionDataAccessFactory.getDataAccess();
		measure = SimilarityFactory.getSimilarityMeasure();
		modelSongs = model;
		this.similarityTrashold = similarityTrashold;
	}
	
	public PredictImpl(Set<Integer> model) {
		this(model, 0.5);
	}

	@Override
	public Double predictHotness(Tag[] tags) {
		List<Integer> tagIds = new LinkedList<Integer>();
		for (Tag tag : tags) 
			tagIds.add(dataAccess.getTagId(tag.getName()));
		return predictHotness(tagIds);
	}

	public Double predictHotness(List<Integer> tags) {
		Integer songModule = tags.size();
		if (songModule == 0)
			return 0.0;
		List<Integer> songs;
		Map<Integer, HotnessTags> finalSongs = new HashMap<Integer, HotnessTags>();
		HotnessTags hotnessTag;
		for (Integer tag : tags) {
			songs = dataAccess.getTracks(tag);
			for (Integer song : songs) {
				if (modelSongs.contains(song)) {
					hotnessTag = finalSongs.get(song);
					if (hotnessTag == null) {
						hotnessTag = new HotnessTags();
						finalSongs.put(song, hotnessTag);
					} else
						hotnessTag.addTag();
				}
			}
		}

		double hotness = 0;
		double similaritySum = 0;
		TrackInfo trackInfo;

		for (Entry<Integer, HotnessTags> song : finalSongs.entrySet()) {
			trackInfo = dataAccess.getTackInfo(song.getKey());
			double similarity = measure.getSimilarity(songModule, trackInfo.getNumerOfTags(),
					song.getValue().getNumberOfTags());
			if (similarity > similarityTrashold) {
				hotness += trackInfo.getHotness() * similarity;
				similaritySum += similarity;
			}
		}
		if (similaritySum == 0)
			return 0.0;
		return hotness / similaritySum;
	}
	
	

	@Override
	public Double predictHotness(String[] tags) {
		List<Integer> tagIds = new LinkedList<Integer>();
		for (String tag : tags) 
			tagIds.add(dataAccess.getTagId(tag));
		return predictHotness(tagIds);
	}

	public static void main(String[] args) {
//		Predict predict = new PredictImpl();
//		Tag[] tags = { new Tag("love", null, null), new Tag("instrument", null, null), new Tag("happi", null, null),
//				new Tag("classic", null, null), new Tag("dream", null, null), new Tag("loung", null, null),
//				new Tag("war", null, null), new Tag("orchestr", null, null) };
//		System.out.println(predict.predictHotness(tags));
	}
}
