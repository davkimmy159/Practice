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

	private static void initWorkRange(List<String> levels, List<String> turns, int oddOrEven, int turnStart, int turnEnd) {
		if (oddOrEven == 1) {
			for (char lv = 'A'; lv <= 'L'; lv += 2) {
				levels.add(String.valueOf(lv));
			}
		} else if (oddOrEven == 2) {
			for (char lv = 'B'; lv <= 'L'; lv += 2) {
				levels.add(String.valueOf(lv));
			}
		} else {
			for (char lv = 'A'; lv <= 'L'; lv++) {
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

	/* Voca app json */
	private static void vocaAppJson(String workingDir) {
		String workDir = "D:" + File.separator + "work" + File.separator + workingDir + File.separator;
		String basePath = (workDir + "result" + File.separator);
		File excel = new File(workDir + "excel.xlsx");

		String version = "1.0";
		List<String> levels = new ArrayList<>();
		List<String> turns = new ArrayList<>();
		Map<String, Map<String, JsonWordList>> outerContainer = new HashMap<>();
		Map<String, JsonWordList> innerContainer;

		XSSFWorkbook wb = null;

		initWorkRange(levels, turns, 1, 1, 8);
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
		String wordType = null;
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
		String sentenceVoice = null;
		String sentenceAnswerVoice = null;
		String imageClsf = null;

		JsonWord jsonWord;

		int sheetLength = wb.getNumberOfSheets();
		for (int sheetCnt = 0; sheetCnt < sheetLength; sheetCnt++) {
			sheet = wb.getSheetAt(sheetCnt);

			for (int rowCnt = 2;; rowCnt++) {

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
				if (isDoubleTurn) {
					sentenceVoice = level + "_" + doubleTurn[0] + " " + doubleTurn[1] + "_" + voice;
				} else {
					sentenceVoice = level + "_" + turn + "_" + voice;
				}

				// sentence_answer_voice;
				if (sentenceAnswer != null) {
					sentenceAnswerVoice = sentenceAnswer + voiceExt;
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

				if (sentenceAnswer != null) {
					jsonWord.setSentence_answer_voice(sentenceAnswerVoice);
				}

				if (sentenceAnswer != null) {
					jsonWord.setSentence_answer(sentenceAnswer);
				}

				if (!isDoubleTurn) {
					putInList(outerContainer, version, level, turn, new String[] { turn }, jsonWord);
				} else {
					for (String eachTurn : doubleTurn) {
						putInList(outerContainer, version, level, eachTurn, doubleTurn, jsonWord);
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

				File file = new File(dirs.getPath() + File.separator + "contents.json");
				if (file.exists()) {
					file.delete();
				}

				file = new File(dirs.getPath() + File.separator + "contents.json");

				fileWrite(json, file);

				System.out.println(file);
			}
		}
	}

	/* Handwrite app rhyme json */
	private static void HandwriteAppRhymeJson(String workingDir) {
		String workDir = "D:" + File.separator + "work" + File.separator + workingDir + File.separator;
		String basePath = (workDir + "result" + File.separator);
		File excel;

		String version = "1.0";
		Map<String, Map<String, Object>> outerContainer = new HashMap<>();
		Map<String, Object> innerContainerA = new HashMap<>();
		Map<String, Object> innerContainerB = new HashMap<>();
		JsonRhymeList2 jsonRhymeList;

		XSSFWorkbook wb = null;

		outerContainer.put("A", innerContainerA);
		outerContainer.put("B", innerContainerB);

		String imgExt = ".png";
		String voiceExt = ".mp3";
		XSSFSheet sheet;

		XSSFRow row;
		XSSFRow nextRow;
		String turn = null;
		String tmpTurn = null;
		String tmpNextRowTurn = null;
		boolean timeToMakeJsonRhyme = false;
		boolean isWorkRow = false;
		boolean isRhymeRow = false;
		String rhymeRowClsf = null;
		String fileName = null;
		String content = null;
		String[] arr = null;
		String arrStr = null;
		int currentOrder = 0;
		

		String rhyme = null;
		String rhyme_make_count = "2";
        String rhyme_make_1 = null;
        String rhyme_make_2 = null;
        String rhyme_make_voice = null;
        String rhyme_sound_voice = null;
        String words_count = null;
        String word1 = null;
        String word1_image = null;
        String word1_voice = null;
        String word1_voice_check = null;
        String word1_name_sound_voice = null;
		
        JsonRhyme2 jsonRhyme2 = null;
        JsonRhyme3 jsonRhyme3 = null;
        JsonRhyme4 jsonRhyme4 = null;
        JsonRhyme5 jsonRhyme5 = null;
        
        JsonRhymeCommonInfo rhymeCommonInfo = null;
        JsonRhymeWordInfo jsonRhymeWordInfo = null;
        
        List<JsonRhymeCommonInfo> rhymeCommonInfoList = new ArrayList<>(5);
        List<JsonRhymeWordInfo> wordInfoList = new ArrayList<>(5);
        for(int i = 0; i < 5; i++) {
        	rhymeCommonInfoList.add(new JsonRhymeCommonInfo());
        	wordInfoList.add(new JsonRhymeWordInfo());
        }
        
        
		String level;
		int sheetStarter;
		for (char repeat = 'A'; repeat <= 'B'; repeat++) {
			level = String.valueOf(repeat);
			sheetStarter = repeat == 'A' ? 2 : 0;

			excel = new File(workDir + "excel_" + level.toLowerCase() + ".xlsx");
			wb = loadExcel(excel);

			int sheetLength = wb.getNumberOfSheets();
			int totRowCnt;
			for (; sheetStarter < sheetLength; sheetStarter++) {
				
				sheet = wb.getSheetAt(sheetStarter);
				totRowCnt = sheet.getPhysicalNumberOfRows();
				for (int rowCnt = 1;; rowCnt++) {
					row = sheet.getRow(rowCnt);
					nextRow = sheet.getRow(rowCnt + 1);

					// turn
					try {
						tmpTurn = row.getCell(0).getStringCellValue().trim();
						if(tmpTurn.contains("23") || tmpTurn.contains("24")) {
							tmpNextRowTurn = nextRow.getCell(0).getStringCellValue().trim();	
						}
						
						if (turn == null || !(tmpTurn.equals(turn) || "".equals(tmpTurn))) {
							turn = tmpTurn.substring(tmpTurn.indexOf("\n") + 1, tmpTurn.indexOf("호"));
							System.out.println("새로운 호 : " + turn);
							isWorkRow = false;
							timeToMakeJsonRhyme = false;
							currentOrder = 0;
						} else {
							if (isWorkRow == false) {
								isWorkRow = true;
							}
							
							if(!"".equals(tmpNextRowTurn)) {
								timeToMakeJsonRhyme = true;
							}
							
							if((rowCnt + 1) == totRowCnt) {
								timeToMakeJsonRhyme = true;
							}
						}
					} catch (NullPointerException ex) {
						// break if no more row
						break;
					}

					// rhymeRow
					if (isWorkRow) {
						rhymeRowClsf = row.getCell(1).getStringCellValue().trim();

						if (Integer.parseInt(turn) % 2 == 1) {
							// odd turn

							if (rhymeRowClsf.contains("영상")) {
								isRhymeRow = true;
							} else if (rhymeRowClsf.contains("단어")) {
								isRhymeRow = false;
							}

							if (isRhymeRow) {
								// rhyme work
								currentOrder++;
								
								fileName = row.getCell(3).getStringCellValue().trim();
								content = row.getCell(4).getStringCellValue().trim();

								if(content.contains(")")) {
									content = content.substring(content.indexOf(')') + 2, content.length());
								}
								
								arr = content.replace(" ", "").split(",");
								
								/*
								for(String arrValue : arr) {
									System.out.print(arrValue + " ");
								}
								System.out.println();
								*/
								
								arrStr = arr[0] + "_" + arr[1] + "_" + arr[2];

								rhyme = arr[2];
								rhyme_make_count = "2";
								rhyme_make_1 = arr[0];
								rhyme_make_2 = arr[1];
								rhyme_make_voice = level + "_" + turn + "_" + currentOrder + "_" + arrStr + voiceExt;
								
								/*
								System.out.println("rhyme            : " + rhyme);
								System.out.println("rhyme_make_count : " + rhyme_make_count);
								System.out.println("rhyme_make_1     : " + rhyme_make_1);
								System.out.println("rhyme_make_2     : " + rhyme_make_2);
								System.out.println("rhyme_make_voice : " + rhyme_make_voice);
								*/

								rhymeCommonInfo = rhymeCommonInfoList.get(currentOrder - 1);
								rhymeCommonInfo.setRhyme(rhyme);
								rhymeCommonInfo.setRhyme_make_count(rhyme_make_count);
								rhymeCommonInfo.setRhyme_make_1(rhyme_make_1);
								rhymeCommonInfo.setRhyme_make_2(rhyme_make_2);
								rhymeCommonInfo.setRhyme_make_voice(rhyme_make_voice);
								
								/*
								rhyme_sound_voice;
								words_count;
								word1;
								word1_image;
								word1_voice;
								word1_voice_check;
								word1_name_sound_voice;
								*/
							}
							
							if (Integer.parseInt(turn) % 2 == 0) {
								// etc work
								fileName = row.getCell(3).getStringCellValue().trim();
								content = row.getCell(4).getStringCellValue().trim();
								words_count = String.valueOf(currentOrder);
							}
						} else {
							// even turn
							
						}

						if(timeToMakeJsonRhyme) {
							/*
							System.out.println("words_count : " + currentOrder);
							System.out.println("cur row cnt : " + (rowCnt + 1));
							System.out.println("tot row cnt : " + totRowCnt);
							*/
							
							/*
							JsonRhymeCommonInfo
					        JsonRhymeWordInfo
					        rhymeCommonInfoList
					        wordInfoList
					        */
							
							switch(currentOrder) {
							case 2:
								for(JsonRhymeCommonInfo jsonRhymeCommonInfo : rhymeCommonInfoList) {
									jsonRhyme2 = new JsonRhyme2();
									jsonRhyme2.setRhyme(rhymeCommonInfo.getRhyme());
									jsonRhyme2.setRhyme_make_count(rhymeCommonInfo.getRhyme_make_count());
									jsonRhyme2.setRhyme_make_1(rhymeCommonInfo.getRhyme_make_1());
									jsonRhyme2.setRhyme_make_2(rhymeCommonInfo.getRhyme_make_2());
									jsonRhyme2.setRhyme_make_voice(rhymeCommonInfo.getRhyme_make_voice());
									outerContainer.get(level).get(turn);
								}
								
								break;
							case 3:
								
								jsonRhyme3 = new JsonRhyme3();
								jsonRhyme3.setRhyme(rhymeCommonInfo.getRhyme());
								jsonRhyme3.setRhyme_make_count(rhymeCommonInfo.getRhyme_make_count());
								jsonRhyme3.setRhyme_make_1(rhymeCommonInfo.getRhyme_make_1());
								jsonRhyme3.setRhyme_make_2(rhymeCommonInfo.getRhyme_make_2());
								jsonRhyme3.setRhyme_make_voice(rhymeCommonInfo.getRhyme_make_voice());
								
								break;
							case 4:
								
								jsonRhyme4 = new JsonRhyme4();
								jsonRhyme4.setRhyme(rhymeCommonInfo.getRhyme());
								jsonRhyme4.setRhyme_make_count(rhymeCommonInfo.getRhyme_make_count());
								jsonRhyme4.setRhyme_make_1(rhymeCommonInfo.getRhyme_make_1());
								jsonRhyme4.setRhyme_make_2(rhymeCommonInfo.getRhyme_make_2());
								jsonRhyme4.setRhyme_make_voice(rhymeCommonInfo.getRhyme_make_voice());
								
								break;
							case 5:
								
								jsonRhyme5 = new JsonRhyme5();
								jsonRhyme5.setRhyme(rhymeCommonInfo.getRhyme());
								jsonRhyme5.setRhyme_make_count(rhymeCommonInfo.getRhyme_make_count());
								jsonRhyme5.setRhyme_make_1(rhymeCommonInfo.getRhyme_make_1());
								jsonRhyme5.setRhyme_make_2(rhymeCommonInfo.getRhyme_make_2());
								jsonRhyme5.setRhyme_make_voice(rhymeCommonInfo.getRhyme_make_voice());
								
								break;
							}
						}
						
//						outerContainer.get(String.valueOf(level)).get(turn).addRhyme(jsonRhyme);
						
						
						for (int turnCnt = 17; turnCnt <= 24; turnCnt++) {
							jsonRhymeList = new JsonRhymeList2(version, "A", String.valueOf(turnCnt), 1);
							jsonRhymeList.setTurns(new String[] { String.valueOf(turnCnt) });
							jsonRhymeList.setIntro_voice("intro" + turnCnt + ".mp3");
							innerContainerA.put(String.valueOf(turnCnt), jsonRhymeList);
						}
						for (int turnCnt = 1; turnCnt <= 24; turnCnt++) {
							jsonRhymeList = new JsonRhymeList2(version, "B", String.valueOf(turnCnt), 1);
							jsonRhymeList.setTurns(new String[] { String.valueOf(turnCnt) });
							jsonRhymeList.setIntro_voice("intro" + turnCnt + ".mp3");
							innerContainerB.put(String.valueOf(turnCnt), jsonRhymeList);
						}

					}
				}
			}
		}
		
		Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().serializeNulls().create();

		for (String levelKey : outerContainer.keySet()) {
			for (String turnKey : outerContainer.get(levelKey).keySet()) {
				String json = gson.toJson(outerContainer.get(levelKey).get(turnKey));
				json = json.replaceAll("  ", "\t").replaceAll("null", "\"\"");

//				System.out.println(json);
				
				/*
				File dirs = new File(basePath + levelKey + File.separator + turnKey + File.separator);
				if (!dirs.exists()) {
					dirs.mkdirs();
				}

				File file = new File(dirs.getPath() + File.separator + "contents.json");
				if (file.exists()) {
					file.delete();
				}
				
				file = new File(dirs.getPath() + File.separator + "contents.json");

				fileWrite(json, file);
				 
				System.out.println(file);
				*/
			}
		}
	}

	public static void main(String[] args) {
		/* Voca app json */
//		 vocaAppJson("work3");

		/* Handwrite app rhyme json */
		HandwriteAppRhymeJson("work4");
	}
}