package practice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

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

	private static Object lock = new Object();
	private static JsonWordBuilder builder;

	private JsonWord(JsonWordBuilder builder) {
		this.word = builder.word;
		this.wordType = builder.wordType;
		this.mean = builder.mean;
		this.image = builder.image;
		this.image_check = builder.image_check;
		this.voice = builder.voice;
		this.voice_check = builder.voice_check;
		this.sentence = builder.sentence;
		this.sentence_mean = builder.sentence_mean;
		this.sentence_voice = builder.sentence_voice;
		this.sentence_voice_check = builder.sentence_voice_check;
		this.sentence_answer = builder.sentence_answer;
		this.sentence_answer_voice = builder.sentence_answer_voice;
		this.sentence_answer_voice_check = builder.sentence_answer_voice_check;
		this.train_number = builder.train_number;
		this.start_margin = builder.start_margin;

		builder.word = "";
		builder.wordType = "";
		builder.mean = "";
		builder.image = "";
		builder.image_check = "";
		builder.voice = "";
		builder.voice_check = "";
		builder.sentence = "";
		builder.sentence_mean = "";
		builder.sentence_voice = "";
		builder.sentence_voice_check = "";
		builder.sentence_answer = "";
		builder.sentence_answer_voice = "";
		builder.sentence_answer_voice_check = "";
		builder.train_number = "";
		builder.start_margin = "";
	}

	@EqualsAndHashCode
	@ToString
	@AllArgsConstructor
	public static class JsonWordBuilder {
		@Getter
		private String word;
		@Getter
		private String wordType;
		@Getter
		private String mean;
		@Getter
		private String image;
		@Getter
		private String image_check;
		@Getter
		private String voice;
		@Getter
		private String voice_check;
		@Getter
		private String sentence;
		@Getter
		private String sentence_mean;
		@Getter
		private String sentence_voice;
		@Getter
		private String sentence_voice_check;
		@Getter
		private String sentence_answer;
		@Getter
		private String sentence_answer_voice;
		@Getter
		private String sentence_answer_voice_check;
		@Getter
		private String[] avoid_type1;
		@Getter
		private String train_number;
		@Getter
		private String start_margin;

		private JsonWordBuilder() {
		}
		
		public JsonWord build() {
			return new JsonWord(builder);
		}

		public JsonWordBuilder setWord(String word) {
			this.word = word;
			return builder;
		}

		public JsonWordBuilder setWordType(String wordType) {
			this.wordType = wordType;
			return builder;
		}

		public JsonWordBuilder setMean(String mean) {
			this.mean = mean;
			return builder;
		}

		public JsonWordBuilder setImage(String image) {
			this.image = image;
			return builder;
		}

		public JsonWordBuilder setImage_check(String image_check) {
			this.image_check = image_check;
			return builder;
		}

		public JsonWordBuilder setVoice(String voice) {
			this.voice = voice;
			return builder;
		}

		public JsonWordBuilder setVoice_check(String voice_check) {
			this.voice_check = voice_check;
			return builder;
		}

		public JsonWordBuilder setSentence(String sentence) {
			this.sentence = sentence;
			return builder;
		}

		public JsonWordBuilder setSentence_mean(String sentence_mean) {
			this.sentence_mean = sentence_mean;
			return builder;
		}

		public JsonWordBuilder setSentence_voice(String sentence_voice) {
			this.sentence_voice = sentence_voice;
			return builder;
		}

		public JsonWordBuilder setSentence_voice_check(String sentence_voice_check) {
			this.sentence_voice_check = sentence_voice_check;
			return builder;
		}

		public JsonWordBuilder setSentence_answer(String sentence_answer) {
			if (sentence_answer != null) {
				this.sentence_answer = sentence_answer;
			}
			return builder;
		}

		public JsonWordBuilder setSentence_answer_voice(String sentence_answer_voice) {
			if (sentence_answer_voice != null) {
				this.sentence_answer_voice = sentence_answer_voice;
			}
			return builder;
		}

		public JsonWordBuilder setSentence_answer_voice_check(String sentence_answer_voice_check) {
			this.sentence_answer_voice_check = sentence_answer_voice_check;
			return builder;
		}

		public JsonWordBuilder setAvoid_type1(String[] avoid_type1) {
			this.avoid_type1 = avoid_type1;
			return builder;
		}

		public JsonWordBuilder setTrain_number(String train_number) {
			this.train_number = train_number;
			return builder;
		}

		public JsonWordBuilder setStart_margin(String start_margin) {
			this.start_margin = start_margin;
			return builder;
		}
	}

	public static JsonWordBuilder getBuilder() {
		if (builder == null) {
			synchronized (lock) {
				if (builder == null) {
					builder = new JsonWordBuilder();
					builder.word = "";
					builder.wordType = "";
					builder.mean = "";
					builder.image = "";
					builder.image_check = "";
					builder.voice = "";
					builder.voice_check = "";
					builder.sentence = "";
					builder.sentence_mean = "";
					builder.sentence_voice = "";
					builder.sentence_voice_check = "";
					builder.sentence_answer = "";
					builder.sentence_answer_voice = "";
					builder.sentence_answer_voice_check = "";
					builder.train_number = "";
					builder.start_margin = "";
				}
			}
		}
		return builder;
	}

	public void putInList(Map<String, Map<String, JsonWordList>> container, String version, String level, String turn, String[] doubleTurn) {
		Map<String, JsonWordList> innerContainer = container.get(level);
		if (innerContainer == null) {
			innerContainer = new HashMap<>();
			container.put(level, innerContainer);
			// System.out.println(level + " : " + turn);
		}

		JsonWordList jsonWordList;
		if (doubleTurn == null) {
			// C ~ L
			addWordToJsonWordList(innerContainer, version, level, turn, doubleTurn);
		} else {
			// A, B
			for (String eachTurn : doubleTurn) {
				addWordToJsonWordList(innerContainer, version, level, eachTurn, doubleTurn);
			}
		}
	}
	
	private void addWordToJsonWordList(Map<String, JsonWordList> innerContainer, String version, String level, String turn, String[] doubleTurn) {
		JsonWordList jsonWordList = innerContainer.get(turn);
		if (jsonWordList == null) {
			if(!"A".equals(level) && !"B".equals(level)) {
				jsonWordList = new JsonWordList(version, level, turn);
				jsonWordList.setTurns(new String[] { turn });
			} else {
				jsonWordList = new JsonWordListWithSightword(version, level, turn);
				jsonWordList.setTurns(doubleTurn);
			}
			innerContainer.put(turn, jsonWordList);
		}
		jsonWordList.addWord(this);
	}
}
