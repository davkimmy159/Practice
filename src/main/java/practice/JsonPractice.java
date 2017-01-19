package practice;

import java.io.File;
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

	public static Map<String, Map<Integer, WordLocInfo>> initWordInfoLoc(String workPath, String excelName) {
		String excelPath = workPath + excelName;
		XSSFWorkbook wb = Util.loadExcel(excelPath);
		XSSFSheet sheet = wb.getSheetAt(0);
		XSSFRow row = sheet.getRow(1);

		String level = null;
		int wordCnt = 0;
		String discription = null;
		List<Integer> valueList;
		int value = 0;
		
		Map<String, Map<Integer, WordLocInfo>> outerContainer = new HashMap<>();
		Map<Integer, WordLocInfo> innerContainer;
		WordLocInfo wordLocInfo;
		outerContainer.put("CD,EF", new HashMap<>());
		outerContainer.put("GH,IJ,KL", new HashMap<>());
		
		for (int rowCnt = 1;; rowCnt++) {
			row = sheet.getRow(rowCnt);
			
			try {
				level = row.getCell(0).getStringCellValue().trim().replace(" ", "");
			} catch (NullPointerException ex) {
				// break if no more row
				break;
			}
			
			try {
				wordCnt = (int)row.getCell(1).getNumericCellValue();
			} catch (NullPointerException ex) {
			}
			
			try {
				discription = row.getCell(2).getStringCellValue().trim();
			} catch (NullPointerException ex) {
			}
			
			valueList = new ArrayList<>();
			for(int cnt = 0; cnt < 4; cnt++) {
				try {
					value = (int)row.getCell(cnt + 3).getNumericCellValue();
				} catch (NullPointerException ex) {
					value = 0;
				}
				
				if(value > 0) {
					valueList.add(value);	
				}
			}
			
			wordLocInfo = new WordLocInfo(discription, valueList);
			innerContainer = outerContainer.get(level);
			innerContainer.put(wordCnt, wordLocInfo);
		}
		
		return outerContainer;
	}
	
	/* Voca app json */
	private static void vocaAppJsonWork(BasicCondition basicCondition) {
		String workPath = Util.makeBasicDirPath("D:", "work", basicCondition.getWorkingDir());
		String basePath = Util.makeBasicDirPath(workPath + "result");
		String jsonPath = Util.makeBasicDirPath(workPath + "json_result");
		String excelPath = workPath + basicCondition.getWordExcelName();

		String version = basicCondition.getVersion();
		Map<String, Map<String, JsonWordList>> outerContainer = new HashMap<>();
		Map<String, JsonWordList> innerContainer;

		Map<String, Map<String, List<Sightword>>> sightwordContainer;
		sightwordContainer = Sightword.getSightwordContainer(workPath, basicCondition.getSightwordExcelName());
		
		XSSFWorkbook wb = null;
		wb = Util.loadExcel(excelPath);
		
		XSSFSheet sheet;

		XSSFRow row;
		String deleted = null;
		String level;
		String wordType = null;
		boolean isDoubleTurn = false;
		String[] doubleTurn = null;
		String turn = null;
		String word;
		String mean;
		String wordSentence;
		String meanSentence;
		String sentenceAnswer = null;
		String[] avoidType1 = null;
		String avoidType1Str = null;
		String capWord = null;
		
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

		for (char lvCnt = 'A'; lvCnt <= 'L'; lvCnt++) {
			level = String.valueOf(lvCnt);
			sheet = wb.getSheet(level);

			if(sheet == null) {
				continue;
			}
			
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
				
				// turn
				try {
					turn = row.getCell(1).getStringCellValue().trim();
				} catch (IllegalStateException | NumberFormatException ex) {
					turn = String.valueOf((int) (row.getCell(1).getNumericCellValue())).trim();
				} catch (NullPointerException ex) {
					// break if no more row
					break;
				}

				if ("".equals(turn)) {
					break;
				}

				if (turn.contains(",")) {
					isDoubleTurn = true;
					doubleTurn = turn.replace(" ", "").split(",");
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

				image += Util.IMG_EXT;

				// voice
				try {
					voiceClsf = row.getCell(6).getStringCellValue().trim();
				} catch (NullPointerException ex) {
					
				}
				if(null == voiceClsf || "".equals(voiceClsf)) {
					voice = word + Util.VOICE_EXT;	
				} else {
					voice = voiceClsf + Util.VOICE_EXT;
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
						sentenceVoice = level + "_" + doubleTurn[0] + " " + doubleTurn[1] + "_" + sentenceVoiceClsf + Util.VOICE_EXT;
					} else {
						sentenceVoice = level + "_" + turn + "_" + sentenceVoiceClsf + Util.VOICE_EXT;
					}
				}
				
				// sentence_answer_voice;
				if (sentenceAnswer != null) {
					sentenceAnswerVoice = sentenceAnswer + Util.VOICE_EXT;
				}

				// avoidType1
				try {
					avoidType1Str = row.getCell(11).getStringCellValue().trim();
				} catch (NullPointerException ex) {
				}
				if (avoidType1Str != null && !"".equals(avoidType1Str)) {
//					System.out.println(level + " : " + turn + " : " + avoidType1Str);
					avoidType1 = avoidType1Str.split(",");
					for (int avoidTypeCnt = 0; avoidTypeCnt < avoidType1.length; avoidTypeCnt++) {
						avoidType1[avoidTypeCnt] = avoidType1[avoidTypeCnt].trim();
					}
				}

				// jsonWord
				jsonWord = JsonWord.getBuilder()
								   .setWord(word)                   
							       .setWordType(wordType)           
								   .setMean(mean)                   
								   .setSentence(wordSentence)       
								   .setSentence_mean(meanSentence)  
								   .setImage(image)                 
								   .setVoice(voice)                 
								   .setSentence_voice(sentenceVoice)
								   .setAvoid_type1(avoidType1)
								   .setSentence_answer(sentenceAnswer)
								   .setSentence_answer_voice(sentenceAnswerVoice)
								   .build();
								   
				
				jsonWord.putInList(outerContainer, version, level, turn, doubleTurn);
				
				System.out.println(rowCnt + " : " + level + " : " + turn + " : " + word);
				 
				// empty variable for next loop
				isDoubleTurn = false;
				doubleTurn = null;
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
			for(int cnt = basicCondition.getTurnStart(); cnt <= basicCondition.getTurnEnd(); cnt++) {
				turnCnt = String.valueOf(cnt);
				
				innerContainer = outerContainer.get(sightwordLvCnt);
				if(innerContainer != null) {
					wordListWithSt = (JsonWordListWithSightword)innerContainer.get(turnCnt);
					wordListWithSt.setSightwords(sightwordContainer.get(sightwordLvCnt).get(turnCnt));	
				}
			}
		}

		// for order and location info of in main screen
		Map<String, Map<Integer, WordLocInfo>> wordLocContainer = initWordInfoLoc(workPath, basicCondition.getLocInfoExcelName());
		for(char lvCnt = 'C'; lvCnt <= 'L'; lvCnt++) {
			Map<String, JsonWordList> innerContr;
			innerContr = outerContainer.get(String.valueOf(lvCnt));
			
			if(innerContr == null) {
				continue;
			}
			
			for(String turnCnt : basicCondition.getTurns()) {
				innerContr.get(turnCnt).setOrderAndMargin(wordLocContainer);
			}
		}
		
		Gson gson = new GsonBuilder().disableHtmlEscaping()
									 .setPrettyPrinting()
									 .serializeNulls()
									 .create();
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
				
				if(basicCondition.isJsonFileOutput()) {
					Util.fileWrite(json, file1);
					Util.fileWrite(json, file2);
					System.out.println(file1);	
				}
			}
		}
	}
	
	public static void main(String[] args) {
		BasicCondition con1 = BasicCondition.getBuilder()
											.setWorkingDir("work3")
											.setWordExcelName("excel1_8.xlsx")
											.setSightwordExcelName("excel_sightword.xlsx")
											.setLocInfoExcelName("excel_word_margin.xlsx")
											.setInterval(2)
											.setLvStart('A')
											.setTurnRange(1, 8)
											.setVersion("1.0")
											.setJsonFileOutput(true)
											.build();
		
		BasicCondition con2 = BasicCondition.getBuilder()
											.setWorkingDir("work3")
											.setWordExcelName("excel9_16.xlsx")
											.setSightwordExcelName("excel_sightword.xlsx")
											.setLocInfoExcelName("excel_word_margin.xlsx")
											.setInterval(2)
											.setLvStart('A')
											.setTurnRange(9, 16)
											.setVersion("1.0")
											.setJsonFileOutput(true)
											.build();
		
		vocaAppJsonWork(con1);
		vocaAppJsonWork(con2);
		
	}
}