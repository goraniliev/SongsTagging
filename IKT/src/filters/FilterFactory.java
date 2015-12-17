package filters;

public class FilterFactory {
	public static TagFilter getTagFilter() {
		return new StandardTagFilter();
	}
}
