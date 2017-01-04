package practice;

import java.util.ArrayList;
import java.util.List;

public class JsonWordList {
	private String version;
	private String level;
	private String turn;
	private List<JsonWord> words;
	
	public JsonWordList(String version, String level, String turn) {
		this.version = version;
		this.level = level;
		this.turn = turn;
		words = new ArrayList<>();
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

	public List<JsonWord> getWords() {
		return words;
	}

	public void setWords(List<JsonWord> words) {
		this.words = words;
	}
	
	public void addWord(JsonWord jsonWord) {
		words.add(jsonWord);
	}
}
