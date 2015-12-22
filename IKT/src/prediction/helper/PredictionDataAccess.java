package prediction.helper;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface PredictionDataAccess {
	Map<String, Integer> getTags();
	
	List<Integer> getTracks(Integer tag);
	
	Integer getTagId(String tag);
	
	TrackInfo getTackInfo(Integer id);
	
	Set<Integer> getAllTracks();
}
