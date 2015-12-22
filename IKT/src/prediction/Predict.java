package prediction;

import java.util.List;
import java.util.Map;

import prediction.helper.TrackInfo;
import utility.Tag;

public interface Predict {
	Double predictHotness(String[] tags);
	Double predictHotness(Tag[] tags);
	Double predictHotness(List<Integer> tags);
}
