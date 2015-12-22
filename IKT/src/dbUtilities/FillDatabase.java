package dbUtilities;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import readers.FinalTagsReader;
import utility.TrackAllInfo;
import crawler.TrackInfoCrawler;

public class FillDatabase extends DbAccess {
	// Empirically calculated
	/**
	 * Determined emprically, aiming maximum dispersion on the sample of first
	 * 100000 songs read. Has been calculated in
	 * ChooseCoefficientForTanhFunction.
	 */
	final static double COEFFICIENT = 0.000100;

	static double tanh(double x, double coeff) {
		/**
		 * Returns tanh(x * coeff) = [e^(x) - e^(-x)] / [e^x + e^(-x)].
		 */
		return Math.tanh(x * coeff);
	}

	static double hotness(double sum) {
		/**
		 * For a given parameter sum which is sum of number of plays and number
		 * of listeners returns a value in interval (0, 1) obtained by applying
		 * the tanh function to the input.
		 */
		return tanh(sum, COEFFICIENT);
	}

	static double calculateHotness(List<TrackAllInfo> tracks, double coeff) {
		/**
		 * For given list of tracks returns an average of hotness of all tracks.
		 */
		double hotness = 0.0;
		for (TrackAllInfo track : tracks) {
			double popularity = track.getNumListeners() + track.getPlayCount();
			hotness += hotness(popularity);
		}
		return 1.0 * hotness / tracks.size();
	}

	static double mean(List<Double> values) {
		/**
		 * Given list of double values, returns the arithmetic mean (average) of
		 * those values.
		 */
		double m = 0.0;
		for (double v : values) {
			m += v;
		}
		return 1.0 * m / values.size();
	}

	static double variance(List<Double> values) {
		/**
		 * For a given list of values returns the variance (dispersion) of the
		 * sample.
		 */
		double mean = mean(values);
		double var = 0.0;
		for (double v : values) {
			double diff = v - mean;
			var += diff * diff;
		}
		return Math.sqrt(1.0 * var / values.size());
	}

	public static double chooseCoefficientForSigmoidalExponent(String file,
			int maxSongs) throws IOException {
		/**
		 * Used to calculate the best coefficient for the sigmoidal (tanh)
		 * function. Best is the one which gives highest variance on the sample
		 * which is used for training of the model (in this case the first
		 * maxSongs from the given list of songs).
		 */
		List<TrackAllInfo> tracks = TrackInfoCrawler.crawl("unique_tracks.txt",
				0, maxSongs);

		double bestVariance = 0.0;
		double bestCoeff = 1.0;

		for (double coeff = 0.000001; coeff <= 1.0; coeff = coeff * 10.0) {
			List<Double> values = new LinkedList<Double>();
			for (TrackAllInfo track : tracks) {
				values.add(tanh(
						1.0 * track.getNumListeners() + track.getPlayCount(),
						coeff));
			}
			double var = variance(values);
			System.out.println(coeff + "\t" + var);
			if (var > bestVariance) {
				bestVariance = var;
				bestCoeff = coeff;
			}
		}

		return bestCoeff;
	}

	void insertSongsAndTags(StringBuilder songsBuilder,
			StringBuilder songsTagBuilder) throws SQLException {

		String insertSongs = "insert into song(idsong, name, artist, numPlays, numListeners, hotness) values "
				+ songsBuilder.substring(0, songsBuilder.length() - 1)
						.toString() + ";";

		String insertSongTags = "insert into songtag(idsong, idtag) values "
				+ songsTagBuilder.substring(0, songsTagBuilder.length() - 1)
						.toString() + ";";

		// System.out.println(insertSongs);
		// System.out.println(insertSongTags);

		Statement st = conn.createStatement();
		st.addBatch(insertSongs);
		st.addBatch(insertSongTags);

		st.executeBatch();
	}

	public FillDatabase() throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		conn = DriverManager.getConnection(
				"jdbc:mysql://localhost/songstagging", userDB, pass);
	}

	public String quoteDuplicatedString(String s) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			sb.append(c);
			if (c == '\'') {
				sb.append(c);
			}
		}

		return sb.toString();
	}

	public void insertTags() throws SQLException {
		FinalTagsReader tagsReader = FinalTagsReader.getInstance();

		Map<String, Integer> tagNameToId = tagsReader.getTags();

		StringBuilder sql = new StringBuilder(
				"insert into tag (idtag,name) values");
		for (Entry<String, Integer> entry : tagNameToId.entrySet()) {
			sql.append("(" + entry.getValue() + ",\'" + entry.getKey() + "\'),");
		}
		Statement st = conn.createStatement();
		// System.out.println(sql.toString());
		st.executeUpdate(sql.substring(0, sql.length() - 1).toString() + ";");
	}

	public void insertTracksFromAPIToDatabase(String file, int start, int end,
			int batchSize) throws IOException, InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException,
			InterruptedException {

		/**
		 * Used to insert up to maxSongs in the database.
		 */
		
		FinalTagsReader tagsReader = FinalTagsReader.getInstance();

		Map<String, Integer> tagNameToId = tagsReader.getTags();

		long s = new Date().getTime();
		List<TrackAllInfo> tracks = TrackInfoCrawler.crawl_threaded(
				"unique_tracks.txt", start, end, 90);
		long e = new Date().getTime();
		System.out.printf("crawling finished in %f seconds",
				1.0 * (e - s) / 1000.0);
		s = new Date().getTime();

		s = new Date().getTime();
		// System.out.println("\nOpening connection: " + (e - s));
		int count = 0;

		int idsong = start;

		StringBuilder songBuilder = new StringBuilder();
		StringBuilder songTagBuilder = new StringBuilder();

		for (int i = 0; i < tracks.size(); i++) {
			TrackAllInfo track = tracks.get(i);
			songBuilder.append("(");
			songBuilder.append(idsong);
			songBuilder.append(",\'");
			songBuilder.append(quoteDuplicatedString(track.getTrackName()));
			songBuilder.append("\',\'");
			songBuilder.append(quoteDuplicatedString(track.getArtistName()));
			songBuilder.append("\',");
			songBuilder.append(track.getPlayCount());
			songBuilder.append(",");
			songBuilder.append(track.getNumListeners());
			songBuilder.append(",");
			songBuilder.append(hotness(track.getPlayCount()
					+ track.getNumListeners()));
			songBuilder.append("),");

			for (String tag : track.getTags()) {
				songTagBuilder.append("(");
				songTagBuilder.append(idsong);
				songTagBuilder.append(",");
				songTagBuilder.append(tagNameToId.get(tag));
				songTagBuilder.append("),");
			}

			if (i % batchSize == 0) {
				if (songTagBuilder.length() > 0) {
					try {
						insertSongsAndTags(songBuilder, songTagBuilder);
					} catch (Exception ex) {
						count++;
					}
				}
				songBuilder = new StringBuilder();
				songTagBuilder = new StringBuilder();
			}

			idsong++;

		}

		try {
			insertSongsAndTags(songBuilder, songTagBuilder);
		} catch (Exception ex) {
			count++;
		}

		e = new Date().getTime();

		System.out.println("Storing took: " + (e - s));

		System.out.println(count + " batches have been skipped");

	}

	// numPlays and numListeners are 0 at the start
	public static void insertNewSong(String name, String artist,
			List<String> tags) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException {
		/**
		 * Inserts a new song with numListeners = 0, numPlays = 0 and hotness
		 * calculated as average of the hotnesses of the tags attached to the
		 * song.
		 */

		Inserts insert = new Inserts();
		insert.insertNewSong(name, artist, tags);
	}

}
