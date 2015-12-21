package prediction.helper;

import java.util.HashMap;
import java.util.Map;

public class PredictionDirectDataAccess implements PredictionDataAccess {

	@Override
	public Map<String, Integer> getTags() {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("love", 1);
		map.put("war", 2);
		// TODO connect this method to database
		return map;
	}

	@Override
	public Map<Integer, Double> getTracks(Integer tag) {
		Map<Integer, Double> map = new HashMap<Integer, Double>();
		map.put(1, 0.5);
		map.put(2, .9);
		return map;
	}
	
}
