package MainWin;

import java.util.ArrayList;

public class MyVocabulary {

	private String word;
	private ArrayList<String> translation = new ArrayList<String>();
	private ArrayList<String> phrase = new ArrayList<String>();
	private ArrayList<String> expSts = new ArrayList<String>();
	
	public MyVocabulary(String a) {
        word = a;
   }
	
	public String getWord() {
		return word;
	}
	
	public void setWord(String word) {
		this.word = word;
	}

	public void addTranslation(String str){
		translation.add(str);
	}
	
	public void addPhrase(String str){
		phrase.add(str);
	}
	
	public void addExpSts(String str){
		expSts.add(str);
	}
	
	public String[] getTranslation() {
		
		String[] strs = new String[translation.size()];
		
		for(int i = 0; i < translation.size(); i++) {
			strs[i] = translation.get(i);
		}
		return strs;
	}
	
	public String[] getPhrase() {
		
		String[] strs = new String[phrase.size()];
		
		for(int i = 0; i < phrase.size(); i++) {
			strs[i] = phrase.get(i);
		}
		return strs;
	}
	
	public String[] getExpSts() {
		
		String[] strs = new String[expSts.size()];
		
		for(int i = 0; i < expSts.size(); i++) {
			strs[i] = expSts.get(i);
		}
		return strs;
	}
	
	
}
