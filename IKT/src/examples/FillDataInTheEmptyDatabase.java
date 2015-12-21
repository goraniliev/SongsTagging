package examples;

import java.io.IOException;
import java.sql.SQLException;

import dbUtilities.FillDatabase;

//	2. Driver class
public class FillDataInTheEmptyDatabase {
	// Example how to fill the database from the file.
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException, SQLException, InterruptedException {
		FillDatabase.insertTracksFromAPIToDatabase("unique_tracks.txt", 10);
	}
}
