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
		System.out.println(songs.size());
		List<Integer> songsList = new ArrayList<>(songs);
		int size = songsList.size() - 1;
		Set<Integer> model = new HashSet<Integer>();
		Set<Integer> subSet = new HashSet<Integer>(songsList);
		for (int i = 0; i <= size; i++) {
			model.add(songsList.get(random.nextInt(size)));
		}
		subSet.removeAll(model);
		System.out.println("Result: " + validate(model, subSet));
		System.out.println("Model: " + ((model.size() * 1.0 / songs.size()) * 100) + "%");
		System.out.println("Model: " + ((subSet.size() * 1.0 / songs.size()) * 100) + "%");
	}
	
	public double validate(Set<Integer> model, Set<Integer> subset) {
		double eps = 0.0000000001;
		predict = PredictFactory.getPredict(model, similarityTrashold);
		double predicted = 0;
		double totalErr = 0.0;
		int step = subset.size() / 100;
		int i = 0;
		int count = 0;
		for (Integer song : subset) {
			if (i % step == 0) {
				System.out.println((i / step) + "%");
			}
			i++;
			TrackInfo info = dataAccess.getTackInfo(song);
			predicted = predict.predictHotness(info.getTags());
//			totalErr += (predicted - info.getHotness()) * (predicted - info.getHotness());
			double absError = Math.abs(predicted - info.getHotness());
			
			// This is almost equal to the relative mean squared error which would be absError / (predicted + info.getHotness()) 
			// when the differences between the predicted and true value are not too very big 
//			if(absError > eps) {
//				totalErr += 2 * absError / (predicted + info.getHotness());
//			}
			
			if(info.getHotness() < eps) {
				continue;
			}
			totalErr += absError / info.getHotness();
			count++;
		}
		
		return totalErr/count;
	}
	
	public static void main(String[] args) {
		Validator validator = new CrossValidation(0.1);
		validator.validate();
	}
}
