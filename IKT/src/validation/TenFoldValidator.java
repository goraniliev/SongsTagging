package validation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Spliterator;

import prediction.Predict;
import prediction.PredictFactory;
import prediction.helper.PredictionDataAccess;
import prediction.helper.PredictionDataAccessFactory;
import prediction.helper.TrackInfo;

public class TenFoldValidator implements Validator {
	private double similarityTrashold;
	private Predict predict;
	private PredictionDataAccess dataAccess;
	
	public TenFoldValidator(double similarityTreshold) {
		this.similarityTrashold = similarityTreshold;
		dataAccess = PredictionDataAccessFactory.getDataAccess();
	}

	@Override
	public void validate() {
		Set<Integer> songs = dataAccess.getAllTracks();
		List<Integer> songsList = new ArrayList<>(songs);
		int step = songsList.size() / 10;
		double dist = 0;
		for (int i = 0; i < 10; i++) {
			List<Integer> sublist = songsList.subList(i * step, (i + 1) * step);
			Set<Integer> subSet = new HashSet<>(sublist);
			Set<Integer> model = new HashSet<Integer>(songsList);
			model.removeAll(sublist);
			dist += validate(model,  subSet);
			System.out.println(((i + 1)*10) + "%");
		}
		
		System.out.println("Result: " + dist / 10);
	}
	
	public double validate(Set<Integer> model, Set<Integer> subset) {
		predict = PredictFactory.getPredict(model, similarityTrashold);
		double predicted = 0;
		double totalErr = 0.0;
		for (Integer song : subset) {
			TrackInfo info = dataAccess.getTackInfo(song);
			predicted = predict.predictHotness(info.getTags());
			totalErr += (predicted - info.getHotness()) * (predicted - info.getHotness());
		}
		
		return totalErr/subset.size();
	}
	
	public static void main(String[] args) {
		Validator validator = new TenFoldValidator(0.1);
		validator.validate();
	}
}
