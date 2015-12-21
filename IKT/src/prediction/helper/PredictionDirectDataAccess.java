package prediction.helper;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import dbUtilities.Selects;

public class PredictionDirectDataAccess implements PredictionDataAccess {
	private Selects selects;
	
	public PredictionDirectDataAccess() {
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
		try {
			return selects.songsHotnessForTag(tag);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new HashMap<Integer, Double>();
	}
	
}
