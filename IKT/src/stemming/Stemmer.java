package stemming;

import org.tartarus.snowball.ext.englishStemmer;

public class Stemmer {

	private static englishStemmer stemmer = new englishStemmer();
	
	public static String stem(String word) {
		stemmer.setCurrent(word);
		if (stemmer.stem()){
		    return stemmer.getCurrent();
		}
		return word;
	}
	
	public static void main(String[] args) {
		System.out.println(Stemmer.stem("abc"));

	}

}
