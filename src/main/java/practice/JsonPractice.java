package practice;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonPractice {

	private static void initWorkRange(List<String> levels, List<String> turns) {
		for (char lv = 'A'; lv <= 'L'; lv += 2) {
			levels.add(String.valueOf(lv));
		}
		for (int turn = 1; turn <= 8; turn++) {
			turns.add(String.valueOf(turn));
		}
	}

	private static XSSFWorkbook loadExcel(File excel) {
		XSSFWorkbook wb = null;

		try {
			wb = new XSSFWorkbook(new FileInputStream(excel));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return wb;
	}

	private static void putInList(Map<String, Map<String, JsonWordList>> container, String level, String turn, JsonWord jsonWord) {
		JsonWordList jsonWordList = container.get(level).get(turn);
		if (jsonWordList == null) {
			jsonWordList = new JsonWordList("1.0.0", level, turn);
			container.get(level).put(turn, jsonWordList);
		}

		jsonWordList.addWord(jsonWord);
	}

	public static String getFileName(File file) {
		return file.getName().substring(0, file.getName().indexOf('.')).trim();
	}

	public static void fileWrite(String source, File dest) {
		try {
			// BufferedWriter 와 FileWriter를 조합하여 사용 (속도 향상)
			BufferedWriter fw = new BufferedWriter(new FileWriter(dest, true));

			// 파일안에 문자열 쓰기
			fw.write(source);
			fw.flush();

			// 객체 닫기
			fw.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* Voca app json */
	private static void vocaAppJson() {
		String workDir = "D:" + File.separator + "work" + File.separator + "work3" + File.separator;
		String basePath = (workDir + "result" + File.separator);
		File excel = new File(workDir + "excel.xlsx");

		String version = "1.0";
		List<String> levels = new ArrayList<>();
		List<String> turns = new ArrayList<>();
		Map<String, Map<String, JsonWordList>> outerContainer = new HashMap<>();
		Map<String, JsonWordList> innerContainer;
		XSSFWorkbook wb = null;

		initWorkRange(levels, turns);
		wb = loadExcel(excel);

		for (String level : levels) {
			innerContainer = new HashMap<>();
			for (String turn : turns) {
				innerContainer.put(turn, new JsonWordList(version, level, turn));
			}
			outerContainer.put(level, innerContainer);
		}

		String imgExt = ".png";
		String voiceExt = ".mp3";
		XSSFSheet sheet;

		XSSFRow row;
		String level;
		boolean isDoubleTurn = false;
		String[] doubleTurn = null;
		String turn;
		String word;
		String mean;
		String wordSentence;
		String meanSentence;
		String sentenceAnswer = null;

		// 파일
		String image;
		String voice;
		String sentenceVoice;
		String sentenceAnswerVoice = null;
		String imageClsf = null;

		JsonWord jsonWord;

		int sheetLength = wb.getNumberOfSheets();
		for (int sheetCnt = 0; sheetCnt < sheetLength; sheetCnt++) {
			sheet = wb.getSheetAt(sheetCnt);

			int rowTotCnt = sheet.getPhysicalNumberOfRows();
			for (int rowCnt = 2; rowCnt <= rowTotCnt; rowCnt++) {

				row = sheet.getRow(rowCnt);

				// level
				try {
					level = row.getCell(0).getStringCellValue().trim();
				} catch (NullPointerException ex) {
					// break if no more row
					break;
				}

				// turn
				try {
					turn = row.getCell(1).getStringCellValue().trim();
				} catch (IllegalStateException | NumberFormatException ex) {
					turn = String.valueOf((int) (row.getCell(1).getNumericCellValue())).trim();
				} catch (NullPointerException ex) {
					break;
				}

				if (turn.contains(",")) {
					isDoubleTurn = true;
					doubleTurn = turn.split(",");
					for (int turnCnt = 0; turnCnt < doubleTurn.length; turnCnt++) {
						doubleTurn[turnCnt] = doubleTurn[turnCnt].trim();
					}
				}

				// word
				word = row.getCell(2).getStringCellValue().trim();

				// mean
				mean = row.getCell(3).getStringCellValue().trim();

				// wordSentence
				wordSentence = row.getCell(4).getStringCellValue().trim();

				if (wordSentence.contains("(")) {
					sentenceAnswer = wordSentence.substring(wordSentence.indexOf("(") + 1, wordSentence.indexOf(")"));
				}

				if (wordSentence.contains(word)) {
					wordSentence = wordSentence.replace(word, "(" + word + ")");
				} else {
					String capWord = StringUtils.capitalize(word);
					if (wordSentence.contains(capWord)) {
						wordSentence = wordSentence.replace(capWord, "(" + capWord + ")");
					}
				}

				meanSentence = row.getCell(5).getStringCellValue().trim();

				// turn
				try {
					turn = row.getCell(1).getStringCellValue().trim();
				} catch (IllegalStateException | NumberFormatException ex) {
					turn = String.valueOf((int) (row.getCell(1).getNumericCellValue()));
				} catch (NullPointerException ex) {
					break;
				}

				// image
				try {
					imageClsf = row.getCell(6).getStringCellValue().trim();
				} catch (IllegalStateException | NumberFormatException ex) {
					imageClsf = String.valueOf((int) (row.getCell(6).getNumericCellValue())).trim();
				} catch (NullPointerException ex) {
					break;
				}

				if ("1".equals(imageClsf) || "2".equals(imageClsf)) {
					image = word + imageClsf;
				} else if ("0".equals(imageClsf)) {
					image = word;
				} else {
					image = imageClsf;
				}

				image += imgExt;

				// voice
				voice = word + voiceExt;

				// sentence_voice;
				sentenceVoice = level + "_" + turn + "_" + voice;

				// sentence_answer_voice;
				if (sentenceAnswer != null) {
					sentenceAnswerVoice = sentenceAnswer + voiceExt;
				}

				// jsonWord
				jsonWord = new JsonWord();
				jsonWord.setWord(word);
				jsonWord.setMean(mean);
				jsonWord.setSentence(wordSentence);
				jsonWord.setSentence_mean(meanSentence);

				jsonWord.setImage(image);
				jsonWord.setVoice(voice);
				jsonWord.setSentence_voice(sentenceVoice);

				if (sentenceAnswer != null) {
					jsonWord.setSentence_answer_voice(sentenceAnswerVoice);
				}

				if (sentenceAnswer != null) {
					jsonWord.setSentence_answer(sentenceAnswer);
				}

				if (!isDoubleTurn) {
					putInList(outerContainer, level, turn, jsonWord);
				} else {
					for (String eachTurn : doubleTurn) {
						putInList(outerContainer, level, eachTurn, jsonWord);
					}
				}

				// empty variable for next loop
				isDoubleTurn = false;
				sentenceAnswer = null;
			}
		}

		Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().serializeNulls().create();

		for (String levelKey : outerContainer.keySet()) {
			for (String turnKey : outerContainer.get(levelKey).keySet()) {
				String json = gson.toJson(outerContainer.get(levelKey).get(turnKey));
				json = json.replaceAll("  ", "\t").replaceAll("null", "\"\"");

				File dirs = new File(basePath + levelKey + File.separator + turnKey + File.separator);
				if (!dirs.exists()) {
					dirs.mkdirs();
				}

				File file = new File(dirs.getPath() + File.separator + "content.json");
				if (file.exists()) {
					file.delete();
				}

				file = new File(dirs.getPath() + File.separator + "content.json");

				fileWrite(json, file);

				System.out.println(file);
			}
		}
	}

	/* Handwrite app rhyme json */
	private static void HandwriteAppRhymeJson() {
		String workDir = "D:" + File.separator + "work" + File.separator + "work4" + File.separator;
		String basePath = (workDir + "result" + File.separator);
		File excel = new File(workDir + "excel.xlsx");

		String version = "1.0";
		List<String> levels = new ArrayList<>();
		List<String> turns = new ArrayList<>();
		Map<String, Map<String, JsonWordList>> outerContainer = new HashMap<>();
		Map<String, JsonWordList> innerContainer;
		XSSFWorkbook wb = null;

		initWorkRange(levels, turns);
		wb = loadExcel(excel);
	}
	
	public static void main(String[] args) {
		/* Voca app json */
//		vocaAppJson();
		
		/* Handwrite app rhyme json */
		HandwriteAppRhymeJson();
	}
}