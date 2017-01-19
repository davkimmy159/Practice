package practice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
public class JsonWord {
	private String word;
	private String wordType;
	private String mean;
	private String image;
	private String image_check;
	private String voice;
	private String voice_check;
	private String sentence;
	private String sentence_mean;
	private String sentence_voice;
	private String sentence_voice_check;
	private String sentence_answer;
	private String sentence_answer_voice;
	private String sentence_answer_voice_check;
	private String[] avoid_type1;
	private String train_number;
	private String start_margin;
	
	public JsonWord() {
		word = "";
		wordType = "";
		mean = "";
		image = "";
		image_check = "";
		voice = "";
		voice_check = "";
		sentence = "";
		sentence_mean = "";
		sentence_voice = "";
		sentence_voice_check = "";
		sentence_answer = "";
		sentence_answer_voice = "";
		sentence_answer_voice_check = "";
		train_number = "";
		start_margin = "";
	}
}
