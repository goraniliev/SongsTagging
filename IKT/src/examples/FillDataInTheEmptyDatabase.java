package examples;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

import dbUtilities.FillDatabase;

//	2. Driver class
public class FillDataInTheEmptyDatabase {
	// Example how to fill the database from the file.
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException, SQLException, InterruptedException {
		long s = new Date().getTime();
//		for(int start = 0; start < 1000000; start += 100000) {
//			FillDatabase.insertTracksFromAPIToDatabase("unique_tracks.txt", start, start + 99999);
//		}
		FillDatabase fd = new FillDatabase();
		fd.insertTags();
		
		//Goran		--> Estimated to finish in less than 4.5 hours
//		fd.insertTracksFromAPIToDatabase("unique_tracks.txt", 0, 350000, 100);
		
		//Kire
//		fd.insertTracksFromAPIToDatabase("unique_tracks.txt", 350001, 700000, 100);
		
		//Viki
//		fd.insertTracksFromAPIToDatabase("unique_tracks.txt", 700001, 850000, 100);
		
		//Ace
//		fd.insertTracksFromAPIToDatabase("unique_tracks.txt", 850001, 1000000, 100);
		
		
		
		System.out.println("Database built took " + (new Date().getTime() - s) + " miliseconds");
	}
	

}
