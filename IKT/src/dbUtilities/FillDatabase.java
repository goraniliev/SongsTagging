package dbUtilities;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import utility.TrackAllInfo;
import crawler.TrackInfoCrawler;

public class FillDatabase {
	final double COEFFICIENT = 0.000100;
	
	double tanh(double x, double coeff) {
		return Math.tanh(x * coeff);
	}
	
	double hotness(double sum) {
		return tanh(sum, COEFFICIENT);
	}
	
	double calculateHotness(List<TrackAllInfo> tracks, double coeff) {
		double hotness = 0.0;
		for(TrackAllInfo track : tracks) {
			double popularity = track.getNumListeners() + track.getPlayCount();
			hotness += hotness(popularity);
		}
		return 1.0 * hotness / tracks.size();
	}
	
	double mean(List<Double> values) {
		double m = 0.0;
		for(double v : values) {
			m += v;
		}
		return 1.0 * m / values.size();
	}
	
	double variance(List<Double> values) {
		double mean = mean(values);
		double var = 0.0;
		for(double v : values) {
			double diff = v - mean;
			var += diff * diff;
		}
		return Math.sqrt(1.0 * var / values.size());
	}
	
	double chooseCoefficientForSigmoidalExponent(String file, int maxSongs) throws IOException {
		List<TrackAllInfo> tracks = TrackInfoCrawler.crawl("unique_tracks.txt", 0, maxSongs);
		
		double bestVariance = 0.0;
		double bestCoeff = 1.0;
		
		for(double coeff = 0.000001; coeff <= 1.0; coeff = coeff * 10.0) {
			List<Double> values = new LinkedList<Double>();
			for(TrackAllInfo track : tracks) {
				values.add(tanh(1.0 * track.getNumListeners() + track.getPlayCount(), coeff));
			}
			double var = variance(values);
			System.out.println(coeff + "\t" + var);
			if(var > bestVariance) {
				bestVariance = var;
				bestCoeff = coeff;
			}
		}
		
		return bestCoeff;
	}
	
	void insertTracksToDatabase(String file, int maxSongs) throws IOException,
			InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException {
		
		List<TrackAllInfo> tracks = TrackInfoCrawler.crawl("unique_tracks.txt", 0, maxSongs);
		Inserts inserts = new Inserts();
		
		for(TrackAllInfo track : tracks) {
//			System.out.println(track.getTrackName());
			int idSong = inserts.insertSong(track.getTrackName(), track.getArtistName(), 
								track.getPlayCount(), track.getNumListeners(), COEFFICIENT);
			for(String tag : track.getTags()) {
				int idTag = inserts.getTagIdByName(tag);
				if(idTag == -1) {
					idTag = inserts.insertTag(tag);
				}
				inserts.insertSongTag(idSong, idTag);
			}
		}
		
		inserts.close();
	}
	
	void updateTagHotness() {
		
	}
	
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException, SQLException {
		FillDatabase fd = new FillDatabase();
		
		// This block was run only once, to determine the coefficient which gives best dispersion of hotness in the interval (0, 1).
//		System.out.printf("Best value to use as coefficient in the tanh function is %f. "
//				+ "For this value we obtain best dispersion of the values in the interval (0, 1)", 
//				fd.chooseCoefficientForSigmoidalExponent("unique_tracks.txt", 1000));
		// Printed:
		// Best value to use as coefficient in the tanh function is 0.000100. For this value we obtain best dispersion of the values in the interval (0, 1)

//		Best value chosen for coeff is 0.000010, so we are putting this value as constant field to the class and we will use it in our predictions of hotness.
		
		
		fd.insertTracksToDatabase("unique_tracks.txt", 1);
	}
}
