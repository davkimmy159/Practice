package practice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sightword {
	private String sightword;
	private String sightword_voice;
	private String sightword_voice_check;
	private String sightword_mean;
	
	private static Object lock = new Object();
	private static SightwordWork sightwordWork;

	@Data
	private static class SightwordWork {
		private Map<String, Map<String, List<Sightword>>> outerContainer;
		private Map<String, List<Sightword>> innerContainerA;
		private Map<String, List<Sightword>> innerContainerB;
		private String turn;
		private int turnCnt;
		private String turnCntStr;
		
		private SightwordWork() {
		}
		
		private static SightwordWork set(String workPath, String sightwordExcel) {
			SightwordWork sightwordWork = new SightwordWork();
			
			String excelPath = workPath + sightwordExcel;
			
			XSSFWorkbook wb = null;
			wb = Util.loadExcel(excelPath);

			XSSFSheet sheet;
			XSSFRow row;

			sheet = wb.getSheetAt(0);
			
			for (int rowCnt = 2;; rowCnt++) {

				row = sheet.getRow(rowCnt);

				// turn
				try {
					row.getCell(0).getStringCellValue();
				} catch (IllegalStateException | NumberFormatException ex) {
					String.valueOf((int) (row.getCell(0).getNumericCellValue()));
				} catch (NullPointerException ex) {
					break;
				}
				
				setAndAddSightword(sightwordWork, row);
			}
			return sightwordWork;
		}
		
		private static void setAndAddSightword(SightwordWork sightwordWork, XSSFRow row) {
			Map<String, Map<String, List<Sightword>>> outerContainer = sightwordWork.getOuterContainer();
			if(outerContainer == null) {
				sightwordWork.setOuterContainer(new HashMap<>());
				outerContainer = sightwordWork.getOuterContainer();
			}
			
			try {
				sightwordWork.turn = row.getCell(0).getStringCellValue().trim();
			} catch (IllegalStateException | NumberFormatException ex) {
				sightwordWork.turn = String.valueOf((int) (row.getCell(0).getNumericCellValue())).trim();
			} catch (NullPointerException ex) {
			}
			
			if(!"".equals(sightwordWork.turn)) {
				sightwordWork.turnCnt++;
				sightwordWork.turnCntStr = String.valueOf(sightwordWork.turnCnt);
			}
			
			int wordRow = 1;
			int meanRow = 2;
			
			String word;
			String sightwordVoice;
			String sightwordMean;
			
			Map<String, List<Sightword>> innerContainer;
			List<Sightword> sightwordList;
			
			for(char lvCnt = 'A'; lvCnt <= 'B'; lvCnt++, wordRow += 2, meanRow += 2) {
				word = row.getCell(wordRow).getStringCellValue().trim();
				sightwordVoice = word + Util.VOICE_EXT;
				sightwordMean = row.getCell(meanRow).getStringCellValue().trim();
				
				Sightword sightword = new Sightword();
				sightword.setSightword(word);
				sightword.setSightword_voice(sightwordVoice);
				sightword.setSightword_mean(sightwordMean);	
				
				innerContainer = outerContainer.get(String.valueOf(lvCnt));
				if(innerContainer == null) {
					outerContainer.put(String.valueOf(lvCnt), new HashMap<>());
					innerContainer = outerContainer.get(String.valueOf(lvCnt));
				}
				
				sightwordList = innerContainer.get(sightwordWork.turnCntStr);
				if(sightwordList == null) {
					sightwordList = new ArrayList<>();
					innerContainer.put(sightwordWork.turnCntStr, sightwordList);
				}
				sightwordList.add(sightword);
			}
		}
	}
	
	public static Map<String, Map<String, List<Sightword>>> getSightwordContainer(String workPath, String sightwordExcel) {
		return getSightwordWork(workPath, sightwordExcel).getOuterContainer();
	}
	
	private static SightwordWork getSightwordWork(String workPath, String sightwordExcel) {
		if(sightwordWork == null) {
			synchronized(lock) {
				if(sightwordWork == null) {
					sightwordWork = SightwordWork.set(workPath, sightwordExcel);
				}
			}
		}
		return sightwordWork;
	}
}
