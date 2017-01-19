package practice;

import java.util.List;

public class JsonWordListWithSightword extends JsonWordList {

	private List<Sightword> sightwords;

	public JsonWordListWithSightword(String version, String level, String turn) {
		super(version, level, turn);
	}

	public List<Sightword> getSightwords() {
		return sightwords;
	}

	public void setSightwords(List<Sightword> sightwords) {
		this.sightwords = sightwords;
	}

	public void addSightword(Sightword sightword) {
		sightwords.add(sightword);
	}
}