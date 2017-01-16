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
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonPractice {

	private static void initWorkRange(List<String> levels, List<String> turns, int interval, char lvStart, int turnStart, int turnEnd) {
		if (interval == 2) {
			for (char lv = lvStart; lv <= 'L'; lv += 2) {
				levels.add(String.valueOf(lv));
			}
		} else {
			for (char lv = lvStart; lv <= 'L'; lv++) {
				levels.add(String.valueOf(lv));
			}
		}

		for (int turn = turnStart; turn <= turnEnd; turn++) {
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

	private static void putInList(Map<String, Map<String, JsonWordList>> container, String version, String level, String turn, String[] turns, JsonWord jsonWord) {
		JsonWordList jsonWordList = container.get(level).get(turn);

		if (jsonWordList == null) {
			jsonWordList = new JsonWordList(version, level, turn);
		}

		jsonWordList.setTurns(turns);
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

	private static Sightword makeSightword(XSSFRow row, int wordRow, int meanRow, String voiceExt) {
		String word;
		String sightwordVoice;
		String sightwordMean;
		
		word = row.getCell(wordRow).getStringCellValue().trim();
		sightwordVoice = word + voiceExt;
		sightwordMean = row.getCell(meanRow).getStringCellValue().trim();
		
		Sightword sightword = new Sightword();
		sightword.setSightword(word);
		sightword.setSightword_voice(sightwordVoice);
		sightword.setSightword_mean(sightwordMean);
		
		return sightword;
	}
	
	private static Map<String, Map<String, List<Sightword>>> sightwordWork(String workPath) {
		File excel = new File(workPath + "excel_sightword.xlsx");
		
		Map<String, Map<String, List<Sightword>>> outerContainer = new HashMap<>();
		Map<String, List<Sightword>> innerContainerA = new HashMap<>();
		Map<String, List<Sightword>> innerContainerB = new HashMap<>();

		outerContainer.put("A", innerContainerA);
		outerContainer.put("B", innerContainerB);
		
		XSSFWorkbook wb = null;
		wb = loadExcel(excel);

		XSSFSheet sheet;
		XSSFRow row;

		String voiceExt = ".mp3";

		String turn = null;
		String tmpTurn = null;
		Sightword sightword;
		
		int wordRow = 1;
		int meanRow = 2;
		
		List<Sightword> list;
		
		sheet = wb.getSheetAt(0);
		
		for (int rowCnt = 2;; rowCnt++) {

			row = sheet.getRow(rowCnt);

			// turn
			try {
				tmpTurn = row.getCell(0).getStringCellValue().trim();
			} catch (IllegalStateException | NumberFormatException ex) {
				tmpTurn = String.valueOf((int) (row.getCell(0).getNumericCellValue())).trim();
			} catch (NullPointerException ex) {
				break;
			}
			
			if(!"".equals(tmpTurn)) {
				turn = tmpTurn;
			}
			
			// A
			sightword = makeSightword(row, wordRow, meanRow, voiceExt);
			
			list = innerContainerA.get(turn);
			if(list == null) {
				list = new ArrayList<>();
				innerContainerA.put(turn, list);
			}
			list.add(sightword);
			
			// B
			sightword = makeSightword(row, wordRow + 2, meanRow + 2, voiceExt);
			list = innerContainerB.get(turn);
			if(list == null) {
				list = new ArrayList<>();
				innerContainerB.put(turn, list);
			}
			list.add(sightword);
			
			list = null;
		}
		
		return outerContainer;
	}
	
	/* Voca app json */
	private static void vocaAppJson(String workingDir, String excelName, int interval, char lvStart, int turnStart, int turnEnd) {
		String workPath = "D:" + File.separator + "work" + File.separator + workingDir + File.separator;
		String basePath = workPath + "result" + File.separator;
		String jsonPath = workPath + "json_result" + File.separator;
		File excel = new File(workPath + excelName);

		String version = "1.0";
		List<String> levels = new ArrayList<>();
		List<String> turns = new ArrayList<>();
		Map<String, Map<String, JsonWordList>> outerContainer = new HashMap<>();
		Map<String, JsonWordList> innerContainer;

		Map<String, Map<String, List<Sightword>>> sightwordContainer;
		sightwordContainer = sightwordWork(workPath);
		
		XSSFWorkbook wb = null;

		initWorkRange(levels, turns, interval, lvStart, turnStart, turnEnd);
		wb = loadExcel(excel);

		for (String level : levels) {
			innerContainer = new HashMap<>();
			for (String turn : turns) {
				if ("A".equals(level) || "B".equals(level)) {
					innerContainer.put(turn, new JsonWordListWithSightword(version, level, turn));
				} else {
					innerContainer.put(turn, new JsonWordList(version, level, turn));
				}
			}
			outerContainer.put(level, innerContainer);
		}

		String imgExt = ".png";
		String voiceExt = ".mp3";
		XSSFSheet sheet;

		XSSFRow row;
		String deleted = null;
		String level;
		String wordType = null;
		boolean isDoubleTurn = false;
		String[] doubleTurn = null;
		String turn;
		String word;
		String mean;
		String wordSentence;
		String meanSentence;
		String sentenceAnswer = null;
		String[] avoidType1 = null;
		String avoidType1Str = null;
		String capWord = null;
		char behindChar;
		int sentenceLength = 0;
		
		// 파일
		String image = null;
		String voice = null;
		String voiceClsf = null;
		String sentenceVoice = null;
		String sentenceVoiceClsf = null;
		String sentenceAnswerVoice = null;
		String imageClsf = null;

		JsonWord jsonWord;
		JsonWordListWithSightword wordListWithSt;

		int sheetLength = wb.getNumberOfSheets();
		for (int sheetCnt = 0; sheetCnt < sheetLength; sheetCnt++) {
			sheet = wb.getSheetAt(sheetCnt);

			for (int rowCnt = 1;; rowCnt++) {

				row = sheet.getRow(rowCnt);

				// deleted words
				try {
					deleted = row.getCell(10).getStringCellValue().trim();
				} catch (NullPointerException ex) {
					
				}
				if (deleted != null && deleted.contains("삭제")) {
					continue;
				}
				
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

				if ("".equals(turn)) {
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

				// wordType
				if (!word.contains(" ")) {
					wordType = String.valueOf(0);
				} else {
					wordType = String.valueOf(1);
				}

				// mean
				mean = row.getCell(3).getStringCellValue().trim();

				// wordSentence
				wordSentence = row.getCell(4).getStringCellValue().trim();

				if (wordSentence.contains("(")) {
					sentenceAnswer = wordSentence.substring(wordSentence.indexOf("(") + 1, wordSentence.indexOf(")"));
				} else {
					capWord = StringUtils.capitalize(word);
					if (wordSentence.contains(capWord)) {
						wordSentence = wordSentence.replace(capWord, "(" + capWord + ")");
					} else if (wordSentence.contains(word)) {
						wordSentence = wordSentence.replace(word, "(" + word + ")");
					}
				}

				/*
				behindChar = wordSentence.charAt(wordSentence.indexOf(word) + word.length());
				if(behindChar == 'i' || behindChar == 'e'|| behindChar == 's') {
					System.out.println(word + " : " + wordSentence);
				}
				*/
				
				
				meanSentence = row.getCell(5).getStringCellValue().trim();

				// image
				try {
					imageClsf = row.getCell(8).getStringCellValue().trim();
				} catch (IllegalStateException | NumberFormatException ex) {
					imageClsf = String.valueOf((int) (row.getCell(8).getNumericCellValue())).trim();
				} catch (NullPointerException ex) {
				}

				if ("1".equals(imageClsf) || "2".equals(imageClsf) || "3".equals(imageClsf)) {
					// image file name with number at end
					image = word + imageClsf;
				} else if ("0".equals(imageClsf)) {
					// no difference from word name
					image = word;
				} else {
					// totally different image file name
					image = imageClsf;
				}

				image += imgExt;

				// voice
				try {
					voiceClsf = row.getCell(6).getStringCellValue().trim();
				} catch (NullPointerException ex) {
					
				}
				if(voiceClsf == null || "".equals(voiceClsf)) {
					voice = word + voiceExt;	
				} else {
					voice = voiceClsf + voiceExt;
				}
				
				// sentence_voice;
				try {
					sentenceVoiceClsf = row.getCell(7).getStringCellValue().trim();
				} catch (NullPointerException ex) {
				}
				if(sentenceVoiceClsf == null || "".equals(sentenceVoiceClsf)) {
					if (isDoubleTurn) {
						sentenceVoice = level + "_" + doubleTurn[0] + " " + doubleTurn[1] + "_" + voice;
					} else {
						sentenceVoice = level + "_" + turn + "_" + voice;
					}
				} else {
					if (isDoubleTurn) {
						sentenceVoice = level + "_" + doubleTurn[0] + " " + doubleTurn[1] + "_" + sentenceVoiceClsf + voiceExt;
					} else {
						sentenceVoice = level + "_" + turn + "_" + sentenceVoiceClsf + voiceExt;
					}
				}
				
				// sentence_answer_voice;
				if (sentenceAnswer != null) {
					sentenceAnswerVoice = sentenceAnswer + voiceExt;
				}

				// avoidType1
				try {
					avoidType1Str = row.getCell(11).getStringCellValue().trim();
				} catch (NullPointerException ex) {
				}
				if (avoidType1Str != null && !"".equals(avoidType1Str)) {
					System.out.println(level + " : " + turn + " : " + avoidType1Str);
					avoidType1 = avoidType1Str.split(",");
					for (int avoidTypeCnt = 0; avoidTypeCnt < avoidType1.length; avoidTypeCnt++) {
						avoidType1[avoidTypeCnt] = avoidType1[avoidTypeCnt].trim();
					}
				}

				// jsonWord
				jsonWord = new JsonWord();
				jsonWord.setWord(word);
				jsonWord.setWordType(wordType);
				jsonWord.setMean(mean);
				jsonWord.setSentence(wordSentence);
				jsonWord.setSentence_mean(meanSentence);

				jsonWord.setImage(image);
				jsonWord.setVoice(voice);
				jsonWord.setSentence_voice(sentenceVoice);

				jsonWord.setAvoid_type1(avoidType1);

				if (sentenceAnswer != null) {
					jsonWord.setSentence_answer_voice(sentenceAnswerVoice);
				}

				if (sentenceAnswer != null) {
					jsonWord.setSentence_answer(sentenceAnswer);
				}

				if (!isDoubleTurn) {
					// System.out.println("----- " + level + " : " + turn + " :
					// " + word + " -----");
					putInList(outerContainer, version, level, turn, new String[] { turn }, jsonWord);
				} else {
					for (String eachTurn : doubleTurn) {
						putInList(outerContainer, version, level, eachTurn, doubleTurn, jsonWord);
					}
				}
				
				// empty variable for next loop
				isDoubleTurn = false;
				sentenceAnswer = null;
				avoidType1Str = null;
				avoidType1 = null;
			}
		}

		String sightwordLvCnt;
		// Sightword input
		for(char sightwordLv = 'A'; sightwordLv <= 'B'; sightwordLv++) {
			sightwordLvCnt = String.valueOf(sightwordLv);
			String turnCnt;
			for(int cnt = turnStart; cnt <= turnEnd; cnt++) {
				turnCnt = String.valueOf(cnt);
				
				innerContainer = outerContainer.get(sightwordLvCnt);
				if(innerContainer != null) {
					wordListWithSt = (JsonWordListWithSightword)innerContainer.get(turnCnt);
					wordListWithSt.setSightwords(sightwordContainer.get(sightwordLvCnt).get(turnCnt));	
				}
			}
		}
		
		Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().serializeNulls().create();
		String json; 
		File dirs1;
		File dirs2;
		File file1;
		File file2;
		String dirName;
		for (String levelKey : outerContainer.keySet()) {
			for (String turnKey : outerContainer.get(levelKey).keySet()) {
				json = gson.toJson(outerContainer.get(levelKey).get(turnKey));
				json = json.replaceAll("  ", "\t").replaceAll("null", "\"\"");

				dirName = "g_" + levelKey.toLowerCase() + "_" + turnKey;
				
				dirs1 = new File(basePath + levelKey.toLowerCase() + File.separator + dirName + File.separator);
				if (!dirs1.exists()) {
					dirs1.mkdirs();
				}

				dirs2 = new File(jsonPath + dirName + File.separator);
				if (!dirs2.exists()) {
					dirs2.mkdirs();
				}
				
				file1 = new File(dirs1.getPath() + File.separator + "contents.json");
				if (file1.exists()) {
					file1.delete();
				}
				file1 = new File(dirs1.getPath() + File.separator + "contents.json");
				
				file2 = new File(dirs2.getPath() + File.separator + "contents.json");
				if (file2.exists()) {
					file2.delete();
				}
				file2 = new File(dirs2.getPath() + File.separator + "contents.json");
				
				fileWrite(json, file1);
				fileWrite(json, file2);
				System.out.println(file1);
			}
		}
	}
	
	public static void main(String[] args) {
		/* Voca app json */
		vocaAppJson("work3", "excel1_8.xlsx", 2, 'A', 1, 8);

//		vocaAppJson("work3", "excel9_16.xlsx", 2, 'A', 9, 16);
	}
}