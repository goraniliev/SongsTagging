package filters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import readers.FinalTagsReader;
import utility.Tag;

public class FilterFactory {
	private static FinalTagsReader reader = FinalTagsReader.getInstance();
	
	public static TagFilter getTagFilter() {
		return new StandardTagFilter();
	}
	
	public static Tag[] getFinalTags() {
		return reader.getTags();
	}
}
