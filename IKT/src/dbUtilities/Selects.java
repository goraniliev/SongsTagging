package dbUtilities;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import utility.TrackAllInfo;

public class Selects extends DbAccess {
	
	public Selects() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		conn = DriverManager.getConnection("jdbc:mysql://localhost/songstagging", userDB, pass);
		Class.forName("com.mysql.jdbc.Driver").newInstance();
	}
	
	private double getAverageHotnessForTag(String tag) throws SQLException {
		double res = 0;
		
		String sql = "{call get_average_hotness_for_tag(?)}";
		CallableStatement st = conn.prepareCall(sql);
		st.setString("tagname", tag);
		st.execute();
		
		ResultSet resultSet = st.getResultSet();
		
		while(resultSet.next()) {
			res = resultSet.getDouble("avg");
		}
		
		return res;
	}
	
	public double predictedHotnessForGivenTags(List<String> tags) throws SQLException {
		double avg = 0.0;
		
		for(String tag : tags) {
			avg += getAverageHotnessForTag(tag);
		}
		
		conn.close();
		
		return 1.0 * avg / tags.size();
	}
	
	public double predictedHotnessForGivenTrack(TrackAllInfo track) throws SQLException {
		return predictedHotnessForGivenTags(track.getTags());
	}
	
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		Selects s = new Selects();
		List<String> l = new LinkedList<String>();
		l.add("dubstep");
		System.out.println(s.predictedHotnessForGivenTags(l));
	}

}
