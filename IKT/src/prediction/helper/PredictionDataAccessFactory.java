package prediction.helper;

public class PredictionDataAccessFactory {
	private static PredictionDataAccess dataAccess = new PredictionCachedDataAccess();
	
	public static PredictionDataAccess getDataAccess() {
		return dataAccess;
	}
}
