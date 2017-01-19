package practice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class SightWordWork {
	
	
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
		String excelPath = workPath + "excel_sightword.xlsx";

		Map<String, Map<String, List<Sightword>>> outerContainer = new HashMap<>();
		Map<String, List<Sightword>> innerContainerA = new HashMap<>();
		Map<String, List<Sightword>> innerContainerB = new HashMap<>();

		outerContainer.put("A", innerContainerA);
		outerContainer.put("B", innerContainerB);

		XSSFWorkbook wb = null;
		wb = Util.loadExcel(excelPath);

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

			if (!"".equals(tmpTurn)) {
				turn = tmpTurn;
			}

			// A
			sightword = makeSightword(row, wordRow, meanRow, voiceExt);

			list = innerContainerA.get(turn);
			if (list == null) {
				list = new ArrayList<>();
				innerContainerA.put(turn, list);
			}
			list.add(sightword);

			// B
			sightword = makeSightword(row, wordRow + 2, meanRow + 2, voiceExt);
			list = innerContainerB.get(turn);
			if (list == null) {
				list = new ArrayList<>();
				innerContainerB.put(turn, list);
			}
			list.add(sightword);

			list = null;
		}

		return outerContainer;
	}
}
