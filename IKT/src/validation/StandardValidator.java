package validation;

import prediction.Predict;
import prediction.PredictFactory;
import prediction.helper.HotnessListTags;

public class StandardValidator implements Validator {
	Predict predict;
	
	public StandardValidator() {
		predict = PredictFactory.getPredict();
	}
	
	@Override
	public void validate() {
		double predicted = 0;
		double original = 0;
		double error = 0;
		double pom;
		int numberOfSongs = predict.getSongTags().size();
		int i = 0;
		int step = numberOfSongs / 100;
		for (HotnessListTags listTags : predict.getSongTags().values()){
			if (i % step == 0) 
				System.out.println(i / step + "%");
			i++;
			predicted = predict.predictHotness(listTags.getTags());
			original = listTags.getHotness();
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