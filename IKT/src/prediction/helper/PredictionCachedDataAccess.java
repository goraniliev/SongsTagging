package prediction.helper;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import dbUtilities.Selects;

public class PredictionCachedDataAccess implements PredictionDataAccess {
	private Selects selects;
	private Map<String, Integer> finalTags;
	private Map<Integer, TrackInfo> songTags;
	private Map<Integer, List<Integer>> tagSongs;
	
	public PredictionCachedDataAccess() {
		songTags = new HashMap<Integer, TrackInfo>();
		tagSongs = new HashMap<Integer, List<Integer>>();
		try {
			selects = new Selects();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		readData();
	}
	
	private void readData() {
		System.out.println("Start reading data");
		try {
			readTags();
			System.out.println("Reading tags finished");
			readSongs();
			System.out.println("Reading data finished");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void readTags() throws SQLException {
		finalTags = selects.getAllTags();
	}
	
	private void readSongs() throws SQLException {
		List<Integer> songs;
		for (Entry<String, Integer> tag : finalTags.entrySet()) {
			songs = new LinkedList<Integer>();
			tagSongs.put(tag.getValue(), songs);
			System.out.println(tag);
			Map<Integer, Double> songsForTag = selects.songsHotnessForTag(tag.getValue());
			System.out.println(tag);
			for (Entry<Integer, Double> song : songsForTag.entrySet()) {
				songs.add(song.getKey());
				TrackInfo listTags = songTags.get(song.getKey());
				if (listTags == null) {
					listTags = new TrackInfo(song.getValue());
					songTags.put(song.getKey(), listTags);
				}
				listTags.addTag(tag.getValue());
			}
		}
	}
	
	@Override
	public Map<String, Integer> getTags() {
		return finalTags;
	}

	@Override
	public List<Integer> getTracks(Integer tag) {
		return tagSongs.get(tag);
	}

	@Override
	public Integer getTagId(String tag) {
		return finalTags.get(tag);
	}

	@Override
	public TrackInfo getTackInfo(Integer id) {
		return songTags.get(id);
	}

	@Override
	public Set<Integer> getAllTracks() {
		return songTags.keySet();
	}
	
	
}
