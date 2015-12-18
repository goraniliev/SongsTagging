package dbUtilities;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import utility.TrackAllInfo;
import crawler.TrackInfoCrawler;

public class FillDatabase {
	void insertTracksToDatabase(String file, int maxSongs) throws IOException,
			InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException {
		
		List<TrackAllInfo> tracks = TrackInfoCrawler.readTracks("unique_tracks.txt", maxSongs);
		Inserts inserts = new Inserts();
		
		for(TrackAllInfo track : tracks) {
			System.out.println(track.getTrackName());
			int idSong = inserts.insertSong(track.getTrackName(), track.getArtistName(), 
								track.getPlayCount(), track.getNumListeners());
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
	
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException, SQLException {
		FillDatabase fd = new FillDatabase();
		fd.insertTracksToDatabase("unique_tracks.txt", 3);
	}
}
