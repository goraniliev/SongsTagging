package dbUtilities;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import utility.TrackAllInfo;
import crawler.TrackInfoCrawler;

public class FillDatabase {
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

	public static void insertTracksFromAPIToDatabase(String file, int maxSongs)
			throws IOException, InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException {
		/**
		 * Used to insert up to maxSongs in the database.
		 */

		List<TrackAllInfo> tracks = TrackInfoCrawler.crawl("unique_tracks.txt",
				0, maxSongs);
		Inserts inserts = new Inserts();

		for (TrackAllInfo track : tracks) {
			// System.out.println(track.getTrackName());
			int idSong = inserts.insertSong(track.getTrackName(),
					track.getArtistName(), track.getPlayCount(),
					track.getNumListeners(), COEFFICIENT);
			for (String tag : track.getTags()) {
				int idTag = inserts.getTagIdByName(tag);
				if (idTag == -1) {
					idTag = inserts.insertTag(tag);
				}
				inserts.insertSongTag(idSong, idTag);
			}
		}

		inserts.close();
	}

	// numPlays and numListeners are 0 at the start
	public static void insertNewSong(String name, String artist,
			List<String> tags) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException {
		/**
		 * Inserts a new song with numListeners = 0, numPlays = 0 and hotness calculated as average of the hotnesses of the tags attached to the song.
		 */
		
		Inserts insert = new Inserts();
		insert.insertNewSong(name, artist, tags);
	}

}
