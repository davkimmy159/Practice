package practice;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

public class BasicCondition {
	private String workingDir;
	private String wordExcelName;
	private String sightwordExcelName;
	private String locInfoExcelName;
	private int interval;
	private char lvStart;
	private char lvEnd;
	private int turnStart;
	private int turnEnd;
	private String version;
	private boolean jsonFileOutput;
	
	List<String> levels;
	List<String> turns;
	
	private static ConditionBuilder builder;
	private static Object lock = new Object();

	private BasicCondition(ConditionBuilder builder) {
		this.workingDir = builder.workingDir;
		this.wordExcelName = builder.wordExcelName;
		this.sightwordExcelName = builder.sightwordExcelName;
		this.locInfoExcelName = builder.locInfoExcelName;
		this.interval = builder.interval;
		this.lvStart = builder.lvStart;
		this.lvEnd = builder.lvEnd;
		this.turnStart = builder.turnStart;
		this.turnEnd = builder.turnEnd;
		this.version = builder.version;
		this.jsonFileOutput = builder.jsonFileOutput;

		this.levels = new ArrayList<>(builder.levels);
		this.turns = new ArrayList<>(builder.turns);
		
		builder.workingDir = null;
		builder.wordExcelName = null;
		builder.locInfoExcelName = null;
		builder.interval = 0;
		builder.lvStart = 0;
		builder.lvEnd = 0;
		builder.turnStart = 0;
		builder.turnEnd = 0;
		builder.version = null;
		builder.jsonFileOutput = false;
		
		builder.levels = null;
		builder.turns = null;
	}

	@EqualsAndHashCode
	@ToString
	@AllArgsConstructor
	public static class ConditionBuilder {
		@Getter
		private String workingDir;
		@Getter
		private String wordExcelName;
		@Getter
		private String sightwordExcelName;
		@Getter
		private String locInfoExcelName;
		@Getter
		private int interval;
		@Getter
		private char lvStart;
		@Getter
		private char lvEnd;
		@Getter
		private int turnStart;
		@Getter
		private int turnEnd;
		@Getter
		private String version;
		@Getter
		private boolean jsonFileOutput;
		
		@Getter
		private List<String> levels;
		@Getter
		private List<String> turns;
		
		private ConditionBuilder() {
		}
		
		public BasicCondition build() {
			builder.setLevels();
			builder.setTurns();
			return new BasicCondition(builder);
		}

		public ConditionBuilder setWorkingDir(String workingDir) {
			this.workingDir = workingDir;
			return builder;
		}

		public ConditionBuilder setWordExcelName(String wordExcelName) {
			this.wordExcelName = wordExcelName;
			return builder;
		}

		public ConditionBuilder setSightwordExcelName(String sightwordExcelName) {
			this.sightwordExcelName = sightwordExcelName;
			return builder;
		}
		
		public ConditionBuilder setLocInfoExcelName(String locInfoExcelName) {
			this.locInfoExcelName = locInfoExcelName;
			return builder;
		}

		public ConditionBuilder setInterval(int interval) {
			this.interval = interval;
			return builder;
		}

		public ConditionBuilder setLvStart(char lvStart) {
			this.lvStart = lvStart;
			return builder;
		}
		
		public ConditionBuilder setLvEnd(char lvEnd) {
			this.lvEnd = lvEnd;
			return builder;
		}

		public ConditionBuilder setTurnRange(int turnStart, int turnEnd) {
			this.turnStart = turnStart;
			this.turnEnd = turnEnd;
			return builder;
		}
		
		public ConditionBuilder setVersion(String version) {
			this.version = version;
			return builder;
		}
		
		public ConditionBuilder setJsonFileOutput(boolean jsonFileOutput) {
			this.jsonFileOutput = jsonFileOutput;
			return builder;
		}
		
		private void setLevels() {
			levels = new ArrayList<>();
			interval = interval < 1 || 2 < interval ? 2 : interval;
					
			if (interval == 2) {
				for (char lv = lvStart; lv <= 'L'; lv += 2) {
					levels.add(String.valueOf(lv));
				}
			} else {
				for (char lv = lvStart; lv <= 'L'; lv++) {
					levels.add(String.valueOf(lv));
				}
			}
		}
		
		private void setTurns() {
			turns = new ArrayList<>();
//			lvStart = lvStart != 'A' && lvStart != 'B' ? 'A' : lvStart;
			
			for (int turn = turnStart; turn <= turnEnd; turn++) {
				turns.add(String.valueOf(turn));
			}
		}
	}
	
	public static ConditionBuilder getBuilder() {
		if (builder == null) {
			synchronized (lock) {
				if (builder == null)
					builder = new ConditionBuilder();
			}
		}
		return builder;
	}

	public String getWorkingDir() {
		return workingDir;
	}

	public void setWorkingDir(String workingDir) {
		this.workingDir = workingDir;
	}

	public String getWordExcelName() {
		return wordExcelName;
	}

	public void setWordExcelName(String wordExcelName) {
		this.wordExcelName = wordExcelName;
	}

	public String getSightwordExcelName() {
		return sightwordExcelName;
	}

	public void setSightwordExcelName(String sightwordExcelName) {
		this.sightwordExcelName = sightwordExcelName;
	}

	public String getLocInfoExcelName() {
		return locInfoExcelName;
	}

	public void setLocInfoExcelName(String locInfoExcelName) {
		this.locInfoExcelName = locInfoExcelName;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public char getLvStart() {
		return lvStart;
	}

	public void setLvStart(char lvStart) {
		this.lvStart = lvStart;
	}

	public char getLvEnd() {
		return lvEnd;
	}

	public void setLvEnd(char lvEnd) {
		this.lvEnd = lvEnd;
	}

	public int getTurnStart() {
		return turnStart;
	}

	public void setTurnStart(int turnStart) {
		this.turnStart = turnStart;
	}

	public int getTurnEnd() {
		return turnEnd;
	}

	public void setTurnEnd(int turnEnd) {
		this.turnEnd = turnEnd;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public boolean isJsonFileOutput() {
		return jsonFileOutput;
	}

	public void setJsonFileOutput(boolean jsonFileOutput) {
		this.jsonFileOutput = jsonFileOutput;
	}

	public List<String> getLevels() {
		return levels;
	}

	public void setLevels(List<String> levels) {
		this.levels = levels;
	}

	public List<String> getTurns() {
		return turns;
	}

	public void setTurns(List<String> turns) {
		this.turns = turns;
	}

	public static Object getLock() {
		return lock;
	}

	public static void setLock(Object lock) {
		BasicCondition.lock = lock;
	}

	public static void setBuilder(ConditionBuilder builder) {
		BasicCondition.builder = builder;
	}
}
