package dbUtilities;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Inserts extends DbAccess{

	
	public Inserts() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		conn = DriverManager.getConnection("jdbc:mysql://localhost/songstagging", userDB, pass);
		Class.forName("com.mysql.jdbc.Driver").newInstance();
	}

	int insertTag(String tagName) throws SQLException {
		String sql = "{call insert_tag(?)}";
		CallableStatement st = conn.prepareCall(sql);
		st.setString("name", tagName);
		st.execute();
		
		int last_tag_id = 0;
		
		ResultSet resultSet = st.getResultSet();

		while(resultSet.next()) {
			last_tag_id = resultSet.getInt("last_tag_id");
		}
		return last_tag_id;
	}
	
	int insertSong(String name, String artist, long numPlays, long numListeners, double coeff/*, String releaseDate*/) throws SQLException {
		int last_id = 0;
		
		String sql = "{call insert_song(?, ?, ?, ?, ?)}";
		CallableStatement st = conn.prepareCall(sql);
		st.setString("name", name);
		st.setString("artist", artist);
		st.setLong("numPlays", numPlays);
		st.setLong("numListeners", numListeners);
		st.setDouble("coeff", coeff);
//		st.setString("releaseDate", releaseDate);
		st.execute();
		
		ResultSet resultSet = st.getResultSet();
//		
		while(resultSet.next()) {
			last_id = resultSet.getInt("last_song_id");
		}

		return last_id;
	}
	
	boolean isNewTag(String tagName) throws SQLException {
		String sql = "{call is_tag_present(?)}";
		CallableStatement st = conn.prepareCall(sql);
		st.setString("tagname", tagName);
		st.execute();
		
		int count = 0;
		
		ResultSet resultSet = st.getResultSet();
//		
		while(resultSet.next()) {
			count = resultSet.getInt("cnt");
		}
		
		return count == 0;
	}
	
	int getTagIdByName(String tagName) throws SQLException {
		String sql = "{call get_id_tag_by_name(?)}";
		CallableStatement st = conn.prepareCall(sql);
		st.setString("name", tagName);
		st.execute();
		
		int id = -1;
		
		ResultSet resultSet = st.getResultSet();
//		
		while(resultSet.next()) {
			id = resultSet.getInt("id");
		}
		
		return id;	
	}
	
	void insertSongTag(int idSong, int idTag) throws SQLException {
		String sql = "{call insert_song_tag(?, ?)}";
		CallableStatement st = conn.prepareCall(sql);
		st.setInt("idsong", idSong);
		st.setInt("idtag", idTag);
		st.execute();
	}
	

	
	void close() throws SQLException {
		conn.close();
	}
}
