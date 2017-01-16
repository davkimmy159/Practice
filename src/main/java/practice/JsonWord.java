package practice;

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
}
