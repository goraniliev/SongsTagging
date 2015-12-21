package prediction;

import java.util.List;
import java.util.Map;

import prediction.helper.HotnessListTags;
import utility.Tag;

public interface Predict {
	Double predictHotness(Tag[] tags);
	Map<Integer, HotnessListTags> getSongTags();
	double predictHotness(List<Integer> tags);
}
