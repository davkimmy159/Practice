package practice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JsonWordList {
	private String version;
	private String level;
	private String turn;
	private String[] turns;

	private List<JsonWord> words;
	
	public JsonWordList(String version, String level, String turn) {
		this.version = version;
		this.level = level;
		this.turn = turn;
		words = new ArrayList<>();
	}

	public void addWord(JsonWord jsonWord) {
		words.add(jsonWord);
	}
	
	public void setOrderAndMargin(Map<String, Map<Integer, WordLocInfo>> wordLocContainer) {
		setOrder();
		Map<Integer, WordLocInfo> innerContainer = null;
		WordLocInfo wordLocInfo = null;
		
		List<Integer> wordCntStds;
		List<Integer> values;
		
		int wordTotCnt = words.size();
		int wordCntCheck = 0;
		int wordIndex = 0;
		boolean found = false;
		int eachWordStd;
		
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
			
			for(int cnt = 0; cnt < wordCntStds.size(); cnt++) {
				eachWordStd = wordCntStds.get(cnt);
				wordCntCheck += eachWordStd;

//				System.out.println("[" + eachWordStd + " : " + wordCntCheck + "] ");
				while(wordIndex < wordCntCheck) {
//					System.out.println(words.get(wordIndex).getWord());
					words.get(wordIndex++).setStart_margin(String.valueOf(values.get(cnt)));
				}
			}
		}
	}
	
	private void setOrder() {
		int orderValue = 1;
		for(JsonWord jsonWord : words) {
			jsonWord.setTrain_number(String.valueOf(orderValue++));
		}
	}
}
