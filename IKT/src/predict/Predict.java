package predict;

import java.sql.SQLException;
import java.util.List;

import dbUtilities.Selects;

public class Predict {
	public static double predictHotnessForTags(List<String> tags)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException {
		/**
		 * Returns predicted hotness for given list of tags which belongs to some new song.
		 */
		Selects selects = new Selects();
		return selects.predictedHotnessForGivenTags(tags);
	}
}
