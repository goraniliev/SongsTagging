package validation;

import java.util.Set;

import prediction.Predict;
import prediction.PredictFactory;
import prediction.helper.PredictionDataAccess;
import prediction.helper.PredictionDataAccessFactory;
import prediction.helper.TrackInfo;

public class StandardValidator implements Validator {
	private Predict predict;
	private PredictionDataAccess dataAccess;
	
	public StandardValidator() {
		dataAccess = PredictionDataAccessFactory.getDataAccess();
	}
	
	@Override
	public void validate() {
		Set<Integer> songs = dataAccess.getAllTracks();
		predict = PredictFactory.getPredict(songs);
		double predicted = 0;
		double original = 0;
		double error = 0;
		double pom;
		int numberOfSongs = songs.size();
		int i = 0;
		int step = numberOfSongs / 100;
		for (Integer song : songs){
			if (i % step == 0) 
				System.out.println(i / step + "%");
			i++;
			TrackInfo info = dataAccess.getTackInfo(song);
			predicted = predict.predictHotness(info.getTags());
			original = info.getHotness();
			pom = predicted - original;
			error += pom * pom;
		}
		
		System.out.println(error / numberOfSongs);
	}
	
	public static void main(String[] args) {
		Validator validator = new StandardValidator();
		validator.validate();
	}
}