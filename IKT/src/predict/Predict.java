package predict;

import java.sql.SQLException;
import java.util.List;

import dbUtilities.Selects;

public class Predict {
	double predictHotnessForTags(List<String> tags)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException {
		Selects selects = new Selects();
		return selects.predictedHotnessForGivenTags(tags);
	}
}
