package practice;

import java.util.ArrayList;
import java.util.List;

public class JsonRhymeList2 {
	private String version;
	private String level;
	private String turn;
	private String[] turns;
	private String intro_voice;
	private String intro_voice_check;
	private List<JsonRhyme2> rhymes;

	public JsonRhymeList2(String version, String level, String turn, int turns) {
		this.version = version;
		this.level = level;
		this.turn = turn;
		this.turns = new String[turns];
		this.intro_voice = "intro" + turn + ".mp3";
		rhymes = new ArrayList<>();
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

	public String getIntro_voice() {
		return intro_voice;
	}

	public void setIntro_voice(String intro_voice) {
		this.intro_voice = intro_voice;
	}

	public String getIntro_voice_check() {
		return intro_voice_check;
	}

	public void setIntro_voice_check(String intro_voice_check) {
		this.intro_voice_check = intro_voice_check;
	}

	public List<JsonRhyme2> getRhymes() {
		return rhymes;
	}

	public void setRhymes(List<JsonRhyme2> rhymes) {
		this.rhymes = rhymes;
	}
	
	public void addRhyme(JsonRhyme2 jsonRhyme) {
		rhymes.add(jsonRhyme);
	}
}
