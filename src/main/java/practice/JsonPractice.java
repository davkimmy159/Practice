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

	public static void dummyImageGenerate(BasicCondition basicCondition) {
		String workPath = Util.makeBasicDirPath("D:", "work", basicCondition.getWorkingDir());
		String basePath = Util.makeBasicDirPath(workPath + "result");
		String excelPath = workPath + basicCondition.getWordExcelName();

		File dummyImg = new File(workPath + "dummy.png");
		
		Map<String, Map<String, List<String>>> outerContainer = new HashMap<>();
		Map<String, List<String>> innerContainer;
		List<String> imgList;
			
		XSSFWorkbook wb = null;
		wb = Util.loadExcel(excelPath);
		
		XSSFSheet sheet;

		XSSFRow row;
		String deleted = null;
		String level;
		boolean isDoubleTurn = false;
		String[] doubleTurn = null;
		String turn = null;
		String word;

		for (char lvCnt = basicCondition.getLvStart(); lvCnt <= basicCondition.getLvEnd(); lvCnt++) {
			level = String.valueOf(lvCnt);
			sheet = wb.getSheet(level);

			if(sheet == null) {
				continue;
			}
			
			for (int rowCnt = 1;; rowCnt++) {

				row = sheet.getRow(rowCnt);

				if(row == null) {
					break;
				}
				
				// deleted words
				try {
					deleted = row.getCell(10).getStringCellValue().trim();
				} catch (NullPointerException ex) {
				}
				if (deleted != null && deleted.contains("삭제")) {
					deleted = null;
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
				if(word.contains("..")) {
					word = word.replace("\\.\\.", ".");
				}
				
				innerContainer = outerContainer.get(level);
				if(innerContainer == null) {
					innerContainer = new HashMap<>();
					outerContainer.put(level, innerContainer);
				}
				
				if(isDoubleTurn) {
					for(String eachTurn : doubleTurn) {
						imgList = innerContainer.get(eachTurn);
						if(imgList == null) {
							imgList = new ArrayList<>();
							innerContainer.put(eachTurn, imgList);
						}
						imgList.add(word);
					}
				} else {
					imgList = innerContainer.get(turn);
					if(imgList == null) {
						imgList = new ArrayList<>();
						innerContainer.put(turn, imgList);
					}
					imgList.add(word);
				}
				
				/*
				if(isDoubleTurn) {
					for(String eachTurn : doubleTurn) {
						System.out.println(level + " : " + eachTurn + " : " + word);	
					}
				} else {
					System.out.println(level + " : " + turn + " : " + word);
				}
				*/
				
				// initialize
				isDoubleTurn = false;
				doubleTurn = null;
			}
		}
		
		for(String key : outerContainer.keySet()) {
			innerContainer = outerContainer.get(key);
			for(String innerKey : innerContainer.keySet()) {
				imgList = innerContainer.get(innerKey);
				System.out.println(key + " - " + innerKey + " - " + imgList.size());
			}
		}

		File dirs;
		String dirName;
		File destImg;
		for (String levelKey : outerContainer.keySet()) {
			innerContainer = outerContainer.get(levelKey);
			for (String turnKey : innerContainer.keySet()) {
				imgList = innerContainer.get(turnKey);
				
				dirName = "g_" + levelKey.toLowerCase() + "_" + turnKey;
				
				dirs = new File(basePath + dirName + File.separator);
				if (!dirs.exists()) {
					dirs.mkdirs();
				}
				
				for(String imgFileName : imgList) {
					destImg = new File(dirs.toString() + File.separator + imgFileName + ".png");
					System.out.println(destImg);
					System.out.println(dirs);
					System.out.println(dummyImg);
					Util.fileCopy(dummyImg, destImg);
				}
			}
		}
	}
	
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
		Map<String, Map<String, JsonWordList>> outerContainer = JsonWordList.getContainer();
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
		String[] transformedWordArray = null;
		
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

		for (char lvCnt = basicCondition.getLvStart(); lvCnt <= basicCondition.getLvEnd(); lvCnt++) {
			level = String.valueOf(lvCnt);
			sheet = wb.getSheet(level);

//			System.out.println(lvCnt);
			
			if(sheet == null) {
				continue;
			}
			
			for (int rowCnt = 1;; rowCnt++) {

				row = sheet.getRow(rowCnt);

				if(row == null) {
					break;
				}
				
				// deleted words
				try {
					deleted = row.getCell(10).getStringCellValue().trim();
				} catch (NullPointerException ex) {
				}
				if (deleted != null && deleted.contains("삭제")) {
					System.out.println("--------------------- " + deleted + " ---------------------");
					System.out.println("--------------------- " + row.getCell(2).getStringCellValue().trim() + " ---------------------");
					deleted = null;   
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
					
					wordSentence = wordSentence.replace("(", "[");
					wordSentence = wordSentence.replace(")", "]");
					transformedWordArray = word.split(" ");
					for(String eachTrWord : transformedWordArray) {
						if(wordSentence.contains(eachTrWord)) {
							wordSentence = wordSentence.replace(eachTrWord, "(" + eachTrWord + ")");
						}
					}
					
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
				} else if ("0".equals(imageClsf) || "".equals(imageClsf) || imageClsf == null) {
					// no difference from word name
					image = word;
				} else {
					// totally different image file name
					image = imageClsf;
				}

				image += Util.IMG_EXT;

				if(image.contains("..")) {
					image = image.replaceAll("\\.\\.", ".");
				}
				
				// voice
				try {
					voiceClsf = row.getCell(6).getStringCellValue().trim();
				} catch (NullPointerException ex) {
					voiceClsf = null;
				}
				if(null == voiceClsf || "".equals(voiceClsf)) {
					voice = word + Util.VOICE_EXT;	
				} else {
					voice = voiceClsf + Util.VOICE_EXT;
				}
				
				if(voice.contains("..")) {
					voice = voice.replaceAll("\\.\\.", ".");
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
				
				if(sentenceVoice.contains("..")) {
					sentenceVoice = sentenceVoice.replaceAll("\\.\\.", ".");
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
				
				System.out.println(word);
				System.out.println(voice);
				
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
								   .treatSpecificWord(level, turn)
								   .build();
				
				
				/*
				if(jsonWord.getWord().contains("~") || jsonWord.getWord().contains("?")) {
					System.out.println(level + " - " + turn + " - " + jsonWord.getWord()+ " - " + jsonWord.getVoice() + " - " + jsonWord.getImage() + " - " + jsonWord.getSentence_voice());
				}
				
				if(jsonWord.getWord().contains("~") || jsonWord.getWord().contains("?")) {
					System.out.println(level + " - " + turn + " - " + jsonWord.getWord()+ " - " + jsonWord.getSentence());
				}

				if(jsonWord.getVoice().contains("~") || jsonWord.getVoice().contains("?") || jsonWord.getImage().contains("~") || jsonWord.getImage().contains("?") || jsonWord.getSentence_voice().contains("~") || jsonWord.getSentence_voice().contains("?") ) {
					System.out.println(level + " - " + turn + " - " + jsonWord.getWord()+ " - " + jsonWord.getVoice()+ " - " + jsonWord.getImage()+ " - " + jsonWord.getSentence_voice());
				}
				*/

				/*
				if("K".equals(level)) {
					System.out.println(level + " : " + turn + " : " + word);
				} 
				*/
				
				jsonWord.putInList(outerContainer, version, level, turn, doubleTurn);
				
				// empty variable for next loop
				isDoubleTurn = false;
				doubleTurn = null;
				sentenceAnswer = null;
				sentenceAnswerVoice = null;
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
					if(wordListWithSt != null) {
						wordListWithSt.setSightwords(sightwordContainer.get(sightwordLvCnt).get(turnCnt));	
					}
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
				
				/*
				dirs1 = new File(basePath + levelKey.toLowerCase() + File.separator + dirName + File.separator);
				if (!dirs1.exists()) {
					dirs1.mkdirs();
				}
				 */
				
				dirs2 = new File(basePath + dirName + File.separator);
				if (!dirs2.exists()) {
					dirs2.mkdirs();
				}
				
				/*
				file1 = new File(dirs1.getPath() + File.separator + "contents.json");
				if (file1.exists()) {
					file1.delete();
				}
				file1 = new File(dirs1.getPath() + File.separator + "contents.json");
				*/
				
				file2 = new File(dirs2.getPath() + File.separator + "contents.json");
				if (file2.exists()) {
					file2.delete();
				}
				file2 = new File(dirs2.getPath() + File.separator + "contents.json");
				
				if(basicCondition.isJsonFileOutput()) {
//					Util.fileWrite(json, file1);
					Util.fileWrite(json, file2);
					System.out.println(file2);	
				}
			}
		}
	}
	
	public static void dummyImgGen() {
		/*
		BasicCondition con1 = BasicCondition.getBuilder()
											.setWorkingDir("work3")
											.setWordExcelName("excel9_16.xlsx")
											.setInterval(2)
											.setLvStart('A')
											.setLvEnd('K')
											.setTurnRange(9, 16)
											.build();
		dummyImageGenerate(con1);

		BasicCondition con2 = BasicCondition.getBuilder()
											.setWorkingDir("work3")
											.setWordExcelName("excel17_24.xlsx")
											.setInterval(2)
											.setLvStart('A')
											.setLvEnd('K')
											.setTurnRange(17, 24)
											.build();
		dummyImageGenerate(con2);
		*/
		
		BasicCondition con3 = BasicCondition.getBuilder()
											.setWorkingDir("work3")
											.setWordExcelName("단어&예문_짝수단계1~12호.xlsx")
											.setInterval(2)
											.setLvStart('B')
											.setLvEnd('L')
											.setTurnRange(1, 12)
											.build();
		
		dummyImageGenerate(con3);
	}
	
	public static void main(String[] args) {
		String swExcel = "excel_sightword.xlsx";
		String marginExcel = "단어 기차 배치.xlsx";
		String excel_odd_1_8 = "단어 리스트 홀수레벨 1 ~ 8.xlsx";
		String excel_odd_9_16 = "단어 리스트 홀수레벨 9 ~ 16.xlsx";
		String excel_odd_17_24 = "단어 리스트 홀수레벨 17 ~ 24.xlsx";
		
		/*
		BasicCondition con_odd_1_8 =      BasicCondition.getBuilder()
														.setWorkingDir("work3")
														.setWordExcelName(excel_odd_1_8)
														.setSightwordExcelName(swExcel)
														.setLocInfoExcelName(marginExcel)
														.setInterval(2)
														.setLvStart('C')
														.setLvEnd('K')
														.setTurnRange(1, 8)
														.setVersion("1.0")
														.setJsonFileOutput(true)
														.build();
		
		BasicCondition con_odd_9_16 =     BasicCondition.getBuilder()
														.setWorkingDir("work3")
														.setWordExcelName(excel_odd_9_16)
														.setSightwordExcelName(swExcel)
														.setLocInfoExcelName(marginExcel)
														.setInterval(2)
														.setLvStart('A')
														.setLvEnd('K')
														.setTurnRange(9, 16)
														.setVersion("1.0")
														.setJsonFileOutput(true)
														.build();
		
		BasicCondition con_odd_17_24 =    BasicCondition.getBuilder()
														.setWorkingDir("work3")
														.setWordExcelName(excel_odd_17_24)
														.setSightwordExcelName(swExcel)
														.setLocInfoExcelName(marginExcel)
														.setInterval(2)
														.setLvStart('C')
														.setLvEnd('K')
														.setTurnRange(17, 24)
														.setVersion("1.0")
														.setJsonFileOutput(true)
														.build();
		*/

//		vocaAppJsonWork(con_odd_1_8);
//		vocaAppJsonWork(con_odd_9_16);
//		vocaAppJsonWork(con_odd_17_24);
		
		String excel_even_1_12 = "단어&예문_짝수단계1~12호.xlsx";
		String excel_even_13_24 = "단어&예문_짝수단계13~24호.xlsx";
		
		/*
		BasicCondition con_even_1_12 =    BasicCondition.getBuilder()
														.setWorkingDir("work3")
														.setWordExcelName(excel_even_1_12)
														.setSightwordExcelName(swExcel)
														.setLocInfoExcelName(marginExcel)
														.setInterval(2)
														.setLvStart('B')
														.setLvEnd('L')
														.setTurnRange(1, 12)
														.setVersion("1.0")
														.setJsonFileOutput(true)
														.build();
		
		vocaAppJsonWork(con_even_1_12);
		*/
		
		BasicCondition con_even_13_24 =   BasicCondition.getBuilder()
														.setWorkingDir("work3")
														.setWordExcelName(excel_even_13_24)
														.setSightwordExcelName(swExcel)
														.setLocInfoExcelName(marginExcel)
														.setInterval(2)
														.setLvStart('B')
														.setLvEnd('L')
														.setTurnRange(13, 24)
														.setVersion("1.0")
														.setJsonFileOutput(true)
														.build();
		
		vocaAppJsonWork(con_even_13_24);
		
		
		
//		dummyImgGen();
	}
}