package validation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import prediction.Predict;
import prediction.PredictFactory;
import prediction.helper.PredictionDataAccess;
import prediction.helper.PredictionDataAccessFactory;
import prediction.helper.TrackInfo;

public class CrossValidation implements Validator {
	private double similarityTrashold;
	private Predict predict;
	private PredictionDataAccess dataAccess;
	
	public CrossValidation(double similarityTreshold) {
		this.similarityTrashold = similarityTreshold;
		dataAccess = PredictionDataAccessFactory.getDataAccess();
	}

	@Override
	public void validate() {
		Random random = new Random();
		Set<Integer> songs = dataAccess.getAllTracks();
		List<Integer> songsList = new ArrayList<>(songs);
		int size = songsList.size() - 1;
		Set<Integer> model = new HashSet<Integer>();
		Set<Integer> subSet = new HashSet<Integer>(songsList);
		for (int i = 0; i <= size; i++) {
			model.add(songsList.get(random.nextInt(size)));
		}
		subSet.removeAll(model);
			
		System.out.println(validate(model, subSet));
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
		Validator validator = new CrossValidation(0.1);
		validator.validate();
	}
}
