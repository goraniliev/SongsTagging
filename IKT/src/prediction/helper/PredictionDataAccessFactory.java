package prediction.helper;

public class PredictionDataAccessFactory {
	public static PredictionDataAccess getDataAccess() {
		return new PredictionCachedDataAccess();
	}
}
