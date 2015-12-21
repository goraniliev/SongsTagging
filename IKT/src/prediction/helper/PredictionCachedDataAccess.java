package prediction.helper;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import dbUtilities.Selects;

public class PredictionCachedDataAccess implements PredictionDataAccess {
	private Selects selects;
	private Map<Integer, Map<Integer, Double>> tracks;
	
	public PredictionCachedDataAccess() {
		tracks = new HashMap<Integer, Map<Integer, Double>>();
		try {
			selects = new Selects();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public Map<String, Integer> getTags() {
		try {
			return selects.getAllTags();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new HashMap<String, Integer>();
	}

	@Override
	public Map<Integer, Double> getTracks(Integer tag) {
		Map<Integer, Double> result = tracks.get(tag);
		if (result != null)
			return result;
		try {
			result = selects.songsHotnessForTag(tag);
			tracks.put(tag, result);
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new HashMap<Integer, Double>();
	}
}
