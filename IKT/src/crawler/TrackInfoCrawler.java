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
		
		String artist = "Metallica";
		String trackName = "Harvester Of Sorrow";
		TrackAllInfo track = allTrackInformations(artist, trackName);
		if (track != null) {
			System.out.println(track);
		} else {
			System.out.println("FAILED IN READING TRACK INFO!");
		}
	}
	
	public static List<TrackAllInfo> readSongs(String fileName) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		String line = "";
		List<TrackAllInfo> result = new ArrayList<>();
		while ((line = br.readLine()) != null) {
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
			// System.out.println("ENTERED 0");
			if (track.getListeners() != null) {
				// System.out.println("ENTERED 1");
				trackInfo.setNumListeners(track.getListeners());	
			} else {
				return null;
			}
			
			if (track.getPlaycount() != null) {
				// System.out.println("ENTERED 2");
				trackInfo.setPlayCount(track.getPlaycount());
			} else {
				return null;
			}
			
			if (track.getWiki() != null) {
				// System.out.println("ENTERED 3");
				trackInfo.setPublishedDate(track.getWiki().getPublishedDate());
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
			// System.out.println("ENTERED 4");
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