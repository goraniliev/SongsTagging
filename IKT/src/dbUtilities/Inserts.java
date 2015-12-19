package dbUtilities;

import java.sql.CallableStatement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import predict.Predict;

public class Inserts extends DbAccess{

	
	public Inserts() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		conn = DriverManager.getConnection("jdbc:mysql://localhost/songstagging", userDB, pass);
		Class.forName("com.mysql.jdbc.Driver").newInstance();
	}

	int insertTag(String tagName) throws SQLException {
		/**
		 * Inserts a new tag.
		 */
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
		/**
		 * Inserts a new song. This method is used when filling the database in the start, with old songs which already have known number
		 * of plays and listeners.
		 */
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
	
	public void insertNewSong(String name, String artist, List<String> tags) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		/**
		 * Inserts a new song. The number of plays and listeners is 0. The hotness is calculated based on the predict function which takes as argument
		 * list of song's tag.
		 */
		
		double hotness = Predict.predictHotnessForTags(tags);
		System.out.printf("Predicted hotness for %s is %f\n", name, hotness);
		String sql = "{call insert_new_song(?, ?, ?)}";
		CallableStatement st = conn.prepareCall(sql);
		st.setString("songname", name);
		st.setString("artist", artist);
		st.setDouble("hotness", hotness);
//		st.setString("releaseDate", releaseDate);
		st.execute();
		
	}
	
	boolean isNewTag(String tagName) throws SQLException {
		/**
		 * Returns true if there is already such tag in the Tag table in database.
		 */
		String sql = "{call is_tag_present(?)}";
		CallableStatement st = conn.prepareCall(sql);
		st.setString("tagname", tagName);
		st.execute();
		
		int count = 0;
		
		ResultSet resultSet = st.getResultSet();
		
		while(resultSet.next()) {
			count = resultSet.getInt("cnt");
		}
		
		return count == 0;
	}
	
	int getTagIdByName(String tagName) throws SQLException {
		/**
		 * Returns tag id for given tagName.
		 */
		
		String sql = "{call get_id_tag_by_name(?)}";
		CallableStatement st = conn.prepareCall(sql);
		st.setString("name", tagName);
		st.execute();
		
		int id = -1;
		
		ResultSet resultSet = st.getResultSet();
		
		while(resultSet.next()) {
			id = resultSet.getInt("id");
		}
		
		return id;	
	}
	
	void insertSongTag(int idSong, int idTag) throws SQLException {
		/**
		 * Inserts new row in the SongTag table in database.
		 */
		String sql = "{call insert_song_tag(?, ?)}";
		CallableStatement st = conn.prepareCall(sql);
		st.setInt("idsong", idSong);
		st.setInt("idtag", idTag);
		st.execute();
	}
	
	void close() throws SQLException {
		/**
		 * Close connection to database.
		 */
		conn.close();
	}
}
