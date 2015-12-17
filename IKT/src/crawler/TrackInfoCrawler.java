package crawler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import utility.Tag;
import utility.Tags;
import utility.TopTags;
import utility.Track;
import utility.TrackAllInfo;
import utility.TrackWrapper;

import com.google.gson.Gson;

import filters.FilterFactory;

public class TrackInfoCrawler {

	public static void main(String[] args) throws IllegalStateException, IOException {
		
		/*
		String artist = "Der Mystic";
		String trackName = "Tangle Of Aspens";
		TrackAllInfo track = allTrackInformations(artist, trackName);
		if (track != null) {
			System.out.println(track);
		} else {
			System.out.println("FAILED IN READING TRACK INFO!");
		}
		*/
	
		List<TrackAllInfo> tracks = readTracks("unique_tracks.txt");
		System.out.println("Number of tracks:" + tracks.size());
		System.out.println(tracks);
	}
	
	public static List<TrackAllInfo> readTracks(String fileName) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		String line = "";
		List<TrackAllInfo> result = new ArrayList<>();
		int counter = 0;
		while ((line = br.readLine()) != null) {
			if (counter % 10 == 0) {
				System.out.println("Number of tracks crawled: " + counter);
			}
			if (counter == 100) {
				break;
			}
			counter++;
			
			String[] parts = line.split("<SEP>");
			String artist = parts[2];
			String trackName = parts[3];
			TrackAllInfo trackInfo = allTrackInformations(artist, trackName);
			if (trackInfo != null) {
				result.add(trackInfo);
			}
		}
		br.close();
		return result;
	}
	
	public static TrackAllInfo allTrackInformations(String artist, String trackName) 
			throws ClientProtocolException, IOException {
		
		TrackAllInfo trackInfo = new TrackAllInfo();
		trackInfo.setTrackName(trackName);
		trackInfo.setArtistName(artist);
		
		// For track info crawling
		String query = "http://ws.audioscrobbler.com/2.0/?method=track.getInfo"
				+ "&artist=" 
				+ URLEncoder.encode(artist, "utf-8") 
				+ "&track="
				+ URLEncoder.encode(trackName, "utf-8")
				+ "&api_key=dd499d6c71a646deb54725b96bf4baa8&format=json";
		
		Track track = getTrackInfo(query);
		if (track != null) {
			if (track.getListeners() != null) {
				trackInfo.setNumListeners(track.getListeners());	
			} else {
				return null;
			}
			
			if (track.getPlaycount() != null) {
				trackInfo.setPlayCount(track.getPlaycount());
			} else {
				return null;
			}
		} else {
			return null;
		}
		
		// For tags crawling
		query = "http://ws.audioscrobbler.com/2.0/?method=track.getToptags"
				+ "&artist=" 
				+ URLEncoder.encode(artist, "utf-8") 
				+ "&track=" 
				+ URLEncoder.encode(trackName, "utf-8")
				+ "&api_key=dd499d6c71a646deb54725b96bf4baa8&format=json";
		
		Tags tags = getTrackTags(query);
		if (tags != null && tags.getTag() != null && tags.getTag().length > 0) { 
			Tag[] filteredTags = FilterFactory.getTagFilter().filterTags(tags.getTag());
			List<String> filteredTagsNames = new ArrayList<>();
			for (Tag tag : filteredTags) {
				filteredTagsNames.add(tag.getName());
			}
			trackInfo.setTags(filteredTagsNames);
		} else {
			return null;
		}
		
		return trackInfo;
	}

	public static Track getTrackInfo(String query) throws IllegalStateException, IOException {
		HttpClient client = new DefaultHttpClient();
		HttpGet getMethod = new HttpGet(query);
		HttpResponse response = client.execute(getMethod);

		if (response.getStatusLine().getStatusCode() != 200) {
			return null;
		}

		InputStream is = response.getEntity().getContent();
		if (is == null) {
			return null;
		}

		TrackWrapper track = new Gson().fromJson(new InputStreamReader(is), TrackWrapper.class);
		if (track == null) {
			return null;
		}

		return track.getTrack();
	}
	

	public static Tags getTrackTags(String query) throws ClientProtocolException, IOException {
		HttpClient client = new DefaultHttpClient();
		HttpGet getMethod = new HttpGet(query);
		HttpResponse response = client.execute(getMethod);

		if (response.getStatusLine().getStatusCode() != 200) {
			return null;
		}

		InputStream is = response.getEntity().getContent();
		if (is == null) {
			return null;
		}
		
		TopTags topTags = new Gson().fromJson(new InputStreamReader(is), TopTags.class);
		if (topTags == null) {
			return null;
		}
		
		return topTags.getToptags();
	}
}