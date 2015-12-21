package prediction.helper;

import java.util.Map;

public interface PredictionDataAccess {
	Map<String, Integer> getTags();
	
	Map<Integer, Double> getTracks(Integer tag);
}
