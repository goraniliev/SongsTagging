package examples;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import dbUtilities.FillDatabase;

//3. Driver class. 

public class AddNewSongs {
	
	// Example how to add new songs. Input parameters are name of song, artist and list of tags attached to the song.

	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		List<String> l = new LinkedList<String>();
		l.add("pop");
		l.add("rock");
		FillDatabase.insertNewSong("New pop rock song", "Pop Rock Band", l);

		
		l = new LinkedList<String>();
		l.add("metal");
		l.add("rock");
		l.add("folk");
		FillDatabase.insertNewSong("New Metal-Rock-Folk Song", "Metal-Rock-Folk Band", l);
		
		
		l = new LinkedList<String>();
		l.add("pop");
		FillDatabase.insertNewSong("New Pop Song", "Pop Artist", l);
		
		l = new LinkedList<String>();
		l.add("newgenre");
		FillDatabase.insertNewSong("house rock", "housers & rockers", l);
		
		
		l = new LinkedList<String>();
		l.add("genre2");
		l.add("rock");
		FillDatabase.insertNewSong("house rock", "housers & rockers", l);
	}

}
