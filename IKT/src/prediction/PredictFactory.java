package prediction;

public class PredictFactory {
	public static Predict getPredict() {
		return new PredictImpl();
	}
}
