package crawler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
	
	static class CrawlerThread extends Thread {
		
		private List<TrackAllInfo> tracks;
		private List<String[]> trackParameters;
		private String threadName;
		
		public CrawlerThread(List<String[]> trackParameters, String threadName) {
			this.trackParameters = trackParameters;
			this.tracks = new ArrayList<>();
			this.threadName = threadName;
		}
		
		@Override
		public void run() {
			int counter = 0;
			for (String[] pair : this.trackParameters) {
				TrackAllInfo trackInfo = null;
				try {
					counter++;
					trackInfo = allTrackInformations(pair[0], pair[1]);
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (trackInfo != null) {
						this.tracks.add(trackInfo);
					}	
					if (counter % 5 == 0) {
						System.out.println(threadName + " - Tracks crawled: " + counter);	
					}
				}
			}
		}
		
		public List<TrackAllInfo> getTracks() {
			return this.tracks;
		}
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		/*
		List<TrackAllInfo> tracks = crawl("unique_tracks.txt", 0, 3);
		System.out.println("Number of tracks: " + tracks.size());
		System.out.println(tracks);
		*/
		
		List<TrackAllInfo> tracks = crawl_threaded("unique_tracks.txt", 0, 499, 18);
		System.out.println("Number of tracks: " + tracks.size());
		System.out.println(tracks);
	}
	
	public static List<TrackAllInfo> crawl_threaded(String fileName, int start, int end, int numThreads) 
			throws IOException, InterruptedException {
		
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		for (int i = 0; i < start; i++) {
			br.readLine();
		}
		String line = "";
		List<String[]> trackParameters = new ArrayList<>();
		int counter = 0;
		while ((line = br.readLine()) != null) {
			if (counter == (end - start + 1)) {
				break;
			}
			counter++;
			
			String[] parts = line.split("<SEP>");
			if (parts.length != 4) {
				continue;
			}
			
			trackParameters.add(new String[] { parts[2], parts[3] });
		}
		br.close();
		
		Set<CrawlerThread> threads = new HashSet<>();
		int tracksCount = trackParameters.size() / numThreads;
		for (int i = 0; i < numThreads; i++) {
			if (i == numThreads - 1) {
				threads.add(new CrawlerThread(
						trackParameters.subList(i * tracksCount, trackParameters.size()),
						"Thread " + i
						)
				);
			} else {
				threads.add(new CrawlerThread(
					trackParameters.subList(i * tracksCount, i * tracksCount + tracksCount),
					"Thread " + i
					)
				);
			}
		}
		
		for (CrawlerThread thread : threads) {
			thread.start();
		}
		
		for (CrawlerThread thread : threads) {
			thread.join();
		}
		
		List<TrackAllInfo> result = new ArrayList<>();
		for (CrawlerThread thread : threads) {
			result.addAll(thread.getTracks());
		}
		
		return result;
	}
	
	public static List<TrackAllInfo> crawl(String fileName, int start, int end) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		for (int i = 0; i < start; i++) {
			br.readLine();
		}
		String line = "";
		List<TrackAllInfo> result = new ArrayList<>();
		int counter = 0;
		while ((line = br.readLine()) != null) {
			if (counter % 10 == 0) {
				System.out.println("Number of tracks crawled: " + counter);
			}
			if (counter == (end - start + 1)) {
				break;
			}
			
			String[] parts = line.split("<SEP>");
			String artist = parts[2];
			String trackName = parts[3];
			TrackAllInfo trackInfo = allTrackInformations(artist, trackName);
			if (trackInfo != null) {
				result.add(trackInfo);
			}
			
			counter++;
		}
		br.close();
		return result;
	}
	
	public static TrackAllInfo allTrackInformations(String artist, String trackName) throws IOException {
		
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

	public static Track getTrackInfo(String query) throws IOException {
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
	

	public static Tags getTrackTags(String query) throws IOException {
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