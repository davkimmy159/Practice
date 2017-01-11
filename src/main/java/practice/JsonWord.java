package practice;

public class JsonWord {
	private String word;
	private String wordType;
	private String mean;
	private String image;
	private String image_size;
	private String voice;
	private String voice_size;
	private String sentence;
	private String sentence_mean;
	private String sentence_voice;
	private String sentence_voice_size;
	private String sentence_answer;
	private String sentence_answer_voice;
	private String sentence_answer_voice_check;
	private String[] avoid_type1;
	
	public JsonWord() {
		word = "";
		wordType = "";
		mean = "";
		image = "";
		image_size = "";
		voice = "";
		voice_size = "";
		sentence = "";
		sentence_mean = "";
		sentence_voice = "";
		sentence_voice_size = "";
		sentence_answer = "";
		sentence_answer_voice = "";
		sentence_answer_voice_check = "";
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

	public String getImage_size() {
		return image_size;
	}

	public void setImage_size(String image_size) {
		this.image_size = image_size;
	}

	public String getVoice() {
		return voice;
	}

	public void setVoice(String voice) {
		this.voice = voice;
	}

	public String getVoice_size() {
		return voice_size;
	}

	public void setVoice_size(String voice_size) {
		this.voice_size = voice_size;
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

	public String getSentence_voice_size() {
		return sentence_voice_size;
	}

	public void setSentence_voice_size(String sentence_voice_size) {
		this.sentence_voice_size = sentence_voice_size;
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
}
