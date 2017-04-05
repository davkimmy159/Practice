package practice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class WordLocInfo {
	private List<Integer> wordStd;
	private List<Integer> values;

	public WordLocInfo() {
	}
	public WordLocInfo(List<Integer> wordStd, List<Integer> values) {
		this.wordStd = wordStd;
		this.values = values;
	}

	public WordLocInfo(String discription, List<Integer> valueList) {
		setLocInfo(discription, valueList);
	}

	private void setLocInfo(String discription, List<Integer> valueList) {
		List<Integer> list = new ArrayList<>();
		Arrays.asList(discription.split("\n")).forEach(str -> {
			if (!str.contains("요")) {
				list.add((int) str.charAt(str.indexOf("개") - 1) - 48);
			}
		});

		wordStd = list;
		values = valueList;
	}

	public List<Integer> getWordStd() {
		return wordStd;
	}

	public void setWordStd(List<Integer> wordStd) {
		this.wordStd = wordStd;
	}

	public List<Integer> getValues() {
		return values;
	}

	public void setValues(List<Integer> values) {
		this.values = values;
	}
}
