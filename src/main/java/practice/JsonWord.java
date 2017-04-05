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

	public JsonWord(){
	}
	public JsonWord(String word, String wordType, String mean, String image, String image_check, String voice, String voice_check, String sentence, String sentence_mean, String sentence_voice, String sentence_voice_check, String sentence_answer, String sentence_answer_voice, String sentence_answer_voice_check, String[] avoid_type1, String train_number, String start_margin) {
		this.word = word;
		this.wordType = wordType;
		this.mean = mean;
		this.image = image;
		this.image_check = image_check;
		this.voice = voice;
		this.voice_check = voice_check;
		this.sentence = sentence;
		this.sentence_mean = sentence_mean;
		this.sentence_voice = sentence_voice;
		this.sentence_voice_check = sentence_voice_check;
		this.sentence_answer = sentence_answer;
		this.sentence_answer_voice = sentence_answer_voice;
		this.sentence_answer_voice_check = sentence_answer_voice_check;
		this.avoid_type1 = avoid_type1;
		this.train_number = train_number;
		this.start_margin = start_margin;
	}

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
		this.avoid_type1 = builder.avoid_type1;
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
		builder.avoid_type1 = null;
		builder.train_number = "";
		builder.start_margin = "";
	}

	public static class JsonWordBuilder {
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

		private JsonWordBuilder() {
		}
		
		public JsonWord build() {
			return new JsonWord(builder);
		}
		
		public JsonWordBuilder treatSpecificWord(String level, String turn) {
			JsonWord.helpTreatSpecificWord(level, turn, this.word);
			return builder;
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

		public String getWord() {
			return word;
		}

		public String getWordType() {
			return wordType;
		}

		public String getMean() {
			return mean;
		}

		public String getImage() {
			return image;
		}

		public String getImage_check() {
			return image_check;
		}

		public String getVoice() {
			return voice;
		}

		public String getVoice_check() {
			return voice_check;
		}

		public String getSentence() {
			return sentence;
		}

		public String getSentence_mean() {
			return sentence_mean;
		}

		public String getSentence_voice() {
			return sentence_voice;
		}

		public String getSentence_voice_check() {
			return sentence_voice_check;
		}

		public String getSentence_answer() {
			return sentence_answer;
		}

		public String getSentence_answer_voice() {
			return sentence_answer_voice;
		}

		public String getSentence_answer_voice_check() {
			return sentence_answer_voice_check;
		}

		public String[] getAvoid_type1() {
			return avoid_type1;
		}

		public String getTrain_number() {
			return train_number;
		}

		public String getStart_margin() {
			return start_margin;
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
	
	public static boolean doesMatch(String targetLevel, String targetTurn, String targetWord, String level, String turn, String word) {
		boolean flag;
		if (targetLevel.equals(level) && targetTurn.equals(turn) && targetWord.equals(word)) {
			flag = true;
		} else {
			flag = false;
		}
		return flag;
	}

	private static void helpTreatSpecificWord(String level, String turn, String word) {
		
		// treat specific word here
		if(doesMatch("C", "3", "You're welcome.", level, turn, word)) {
			builder.image = builder.image.replace("..", ".");
		} else if(doesMatch("G", "5", "do", level, turn, word)) {
			builder.sentence = builder.sentence.replace("(do) y", "do y");
		} else if(doesMatch("G", "6", "How about ~?", level, turn, word)) {
			builder.image = builder.image.replace(" ~?", "");
		} else if(doesMatch("G", "19", "Why don't you~?", level, turn, word)) {
			builder.voice = builder.voice.replace("~?", "");
			builder.image = builder.image.replace("~?", "");
			builder.sentence_voice = builder.sentence_voice.replace("~?", "");
		} else if(doesMatch("I", "7", "nod", level, turn, word)) {
			builder.sentence = builder.sentence.replace("(nodding)", "[(nod)ding]");
		} else if(doesMatch("I", "7", "hug", level, turn, word)) {
			builder.sentence= builder.sentence.replace("(hugging)", "[(hug)ging]");
		} else if(doesMatch("I", "8", "duck", level, turn, word)) {
			builder.sentence = builder.sentence.replace("(duck)ling", "duckling");
		} else if(doesMatch("F", "4", "bed", level, turn, word)) {
			builder.sentence = builder.sentence.replace("(bed)room", "bedroom");
		} else if(doesMatch("J", "1", "quiz", level, turn, word)) {
			builder.sentence = builder.sentence.replace("(quizzing)", "(quiz)");
		} else if(doesMatch("J", "10", "can", level, turn, word)) {
			builder.sentence = builder.sentence.replace("(can)", "can");
		} else if(doesMatch("J", "11", "weigh", level, turn, word)) {
			builder.sentence = builder.sentence.replace("(weigh)t", "weight");
		} else if(doesMatch("H", "4", "brush your hair", level, turn, word)) {
			builder.sentence = builder.sentence.replace("(brush)", "brush");
			builder.sentence = builder.sentence.replace("(hair)", "hair");
			builder.sentence = builder.sentence.replace("[", "(");
			builder.sentence = builder.sentence.replace("]", ")");
		} else if(doesMatch("H", "4", "brush your teeth", level, turn, word)) {
			builder.sentence = builder.sentence.replace("(brush)", "brush");
			builder.sentence = builder.sentence.replace("(teeth)", "teeth");
			builder.sentence = builder.sentence.replace("[", "(");
			builder.sentence = builder.sentence.replace("]", ")");
		} else if(doesMatch("H", "1", "That's all", level, turn, word)) {
			builder.sentence = builder.sentence.replace(").", ".)");
		} else if(doesMatch("H", "1", "That's why", level, turn, word)) {
			builder.sentence = builder.sentence.replace(").", ".)");
		}
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getWordType() {
		return wordType;
	}

	public void setWordType(String wordType) {
		this.wordType = wordType;
	}

	public String getMean() {
		return mean;
	}

	public void setMean(String mean) {
		this.mean = mean;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getImage_check() {
		return image_check;
	}

	public void setImage_check(String image_check) {
		this.image_check = image_check;
	}

	public String getVoice() {
		return voice;
	}

	public void setVoice(String voice) {
		this.voice = voice;
	}

	public String getVoice_check() {
		return voice_check;
	}

	public void setVoice_check(String voice_check) {
		this.voice_check = voice_check;
	}

	public String getSentence() {
		return sentence;
	}

	public void setSentence(String sentence) {
		this.sentence = sentence;
	}

	public String getSentence_mean() {
		return sentence_mean;
	}

	public void setSentence_mean(String sentence_mean) {
		this.sentence_mean = sentence_mean;
	}

	public String getSentence_voice() {
		return sentence_voice;
	}

	public void setSentence_voice(String sentence_voice) {
		this.sentence_voice = sentence_voice;
	}

	public String getSentence_voice_check() {
		return sentence_voice_check;
	}

	public void setSentence_voice_check(String sentence_voice_check) {
		this.sentence_voice_check = sentence_voice_check;
	}

	public String getSentence_answer() {
		return sentence_answer;
	}

	public void setSentence_answer(String sentence_answer) {
		this.sentence_answer = sentence_answer;
	}

	public String getSentence_answer_voice() {
		return sentence_answer_voice;
	}

	public void setSentence_answer_voice(String sentence_answer_voice) {
		this.sentence_answer_voice = sentence_answer_voice;
	}

	public String getSentence_answer_voice_check() {
		return sentence_answer_voice_check;
	}

	public void setSentence_answer_voice_check(String sentence_answer_voice_check) {
		this.sentence_answer_voice_check = sentence_answer_voice_check;
	}

	public String[] getAvoid_type1() {
		return avoid_type1;
	}

	public void setAvoid_type1(String[] avoid_type1) {
		this.avoid_type1 = avoid_type1;
	}

	public String getTrain_number() {
		return train_number;
	}

	public void setTrain_number(String train_number) {
		this.train_number = train_number;
	}

	public String getStart_margin() {
		return start_margin;
	}

	public void setStart_margin(String start_margin) {
		this.start_margin = start_margin;
	}

	public static Object getLock() {
		return lock;
	}

	public static void setLock(Object lock) {
		JsonWord.lock = lock;
	}

	public static void setBuilder(JsonWordBuilder builder) {
		JsonWord.builder = builder;
	}
}
