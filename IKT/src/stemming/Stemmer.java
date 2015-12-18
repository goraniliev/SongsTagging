package stemming;

import org.tartarus.snowball.ext.englishStemmer;

public class Stemmer {

	static String stem(String word) {
		englishStemmer stemmer = new englishStemmer();
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
