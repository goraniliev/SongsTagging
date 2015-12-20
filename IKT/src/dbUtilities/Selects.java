package dbUtilities;

import java.sql.CallableStatement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import utility.TrackAllInfo;

public class Selects extends DbAccess {
	
	public Selects() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
	}
	
	private double getAverageHotnessForTag(String tag) throws SQLException {
		/**
		 * For a given tag returns the average of hotnesses of all songs to which this tag is attached.
		 */
		double res = 0;
		
		conn = DriverManager.getConnection("jdbc:mysql://localhost/songstagging", userDB, pass);
		
		String sql = "{call get_average_hotness_for_tag(?)}";
		CallableStatement st = conn.prepareCall(sql);
		st.setString("tagname", tag);
		st.execute();
		
		ResultSet resultSet = st.getResultSet();
		
		while(resultSet.next()) {
			res = resultSet.getDouble("avg");
		}
		
		conn.close();
		
		return res;
	}
	
	public double predictedHotnessForGivenTags(List<String> tags) throws SQLException {
		/**
		 * For a given list of tags, predicts how hot would be a song which will have ale these tags. Returs the predicted hotness.
		 */
		if(tags == null || tags.size() == 0) {
			return 0.1;
		}
		double avg = 0.0;
		
		for(String tag : tags) {
			avg += getAverageHotnessForTag(tag);
		}
		
		if(avg - 0.0 < 0.0000001) {
			return 0.2;
		}
		
		return 1.0 * avg / tags.size();
	}
	
	public double predictedHotnessForGivenTrack(TrackAllInfo track) throws SQLException {
		/**
		 * For a given track as argument returns the predicted hotness for that track.
		 */
		return predictedHotnessForGivenTags(track.getTags());
	}
	
}
