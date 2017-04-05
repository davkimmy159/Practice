package practice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class JsonWordList {

	private static Map<String, Map<String, JsonWordList>> outerContainer = new HashMap<>();
	
	private String version;
	private String level;
	private String turn;
	private String[] turns;

	private List<JsonWord> words;
	
	public JsonWordList() {
	}

	public JsonWordList(String version, String level, String turn) {
		this.version = version;
		this.level = level;
		this.turn = turn;
		words = new ArrayList<>();
	}
	
	public JsonWordList(String version, String level, String turn, String[] turns, List<JsonWord> words) {
		this.version = version;
		this.level = level;
		this.turn = turn;
		this.turns = turns;
		this.words = words;
	}

	public static Map<String, Map<String, JsonWordList>> getContainer() {
		return outerContainer;
	}
	
	public void addWord(JsonWord jsonWord) {
		words.add(jsonWord);
	}
	
	public void setOrderAndMargin(Map<String, Map<Integer, WordLocInfo>> wordLocContainer) {
		Map<Integer, WordLocInfo> innerContainer = null;
		WordLocInfo wordLocInfo = null;
		
		List<Integer> wordCntStds;
		List<Integer> values;
		
		int wordTotCnt = words.size();
		int wordCntCheck = 0;
		int wordIndex = 0;
		boolean found = false;
		int eachWordStd;
		int size;
		int[] startPositionFor3 = {1, 5, 9};
		int[] startPositionFor4 = {1, 5, 8, 12};
		
		JsonWord jsonWord;
		
		for(String key : wordLocContainer.keySet()) {
			if(key.contains(level)) {
				innerContainer = wordLocContainer.get(key);
				found = true;
			}
		}
		
		if(found) {
//			System.out.println(level + " : " + turn);
//			System.out.println(wordCntStds);
//			System.out.println(values);

			wordLocInfo = innerContainer.get(wordTotCnt);
			wordCntStds = wordLocInfo.getWordStd();
			values = wordLocInfo.getValues();
			size = wordCntStds.size();
			
			for(int cnt = 0; cnt < size; cnt++) {
				eachWordStd = wordCntStds.get(cnt);
				
				wordCntCheck += eachWordStd;
				
				jsonWord = words.get(wordIndex);
				jsonWord.setStart_margin(String.valueOf(values.get(cnt)));	

//				System.out.println(level + " - " + turn + " - " + words.size() + " - " + wordIndex + " - " + values.get(cnt) + " - " + jsonWord.getWord());
				
//				System.out.println("[" + eachWordStd + " : " + wordCntCheck + "] ");
				while(wordIndex < wordCntCheck) {
//					System.out.println(words.get(wordIndex).getWord());
					jsonWord = words.get(wordIndex++);
//					jsonWord.setStart_margin(String.valueOf(values.get(cnt)));
					
					if(size == 3) {
						jsonWord.setTrain_number(String.valueOf(startPositionFor3[cnt]++));
					} else {
						jsonWord.setTrain_number(String.valueOf(startPositionFor4[cnt]++));
					}
				}
			}
		}
	}

	public static Map<String, Map<String, JsonWordList>> getOuterContainer() {
		return outerContainer;
	}

	public static void setOuterContainer(Map<String, Map<String, JsonWordList>> outerContainer) {
		JsonWordList.outerContainer = outerContainer;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getTurn() {
		return turn;
	}

	public void setTurn(String turn) {
		this.turn = turn;
	}

	public String[] getTurns() {
		return turns;
	}

	public void setTurns(String[] turns) {
		this.turns = turns;
	}

	public List<JsonWord> getWords() {
		return words;
	}

	public void setWords(List<JsonWord> words) {
		this.words = words;
	}
}
