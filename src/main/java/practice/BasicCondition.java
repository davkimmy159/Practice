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
	}

	public static class ConditionBuilder {
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
		
		private List<String> levels;
		private List<String> turns;
		
		private ConditionBuilder() {
		}
		
		public BasicCondition build() {
			this.setLevels();
			this.setTurns();
			return new BasicCondition(this);
		}

		public ConditionBuilder setWorkingDir(String workingDir) {
			this.workingDir = workingDir;
			return this;
		}

		public ConditionBuilder setWordExcelName(String wordExcelName) {
			this.wordExcelName = wordExcelName;
			return this;
		}

		public ConditionBuilder setSightwordExcelName(String sightwordExcelName) {
			this.sightwordExcelName = sightwordExcelName;
			return this;
		}
		
		public ConditionBuilder setLocInfoExcelName(String locInfoExcelName) {
			this.locInfoExcelName = locInfoExcelName;
			return this;
		}

		public ConditionBuilder setInterval(int interval) {
			this.interval = interval;
			return this;
		}

		public ConditionBuilder setLvStart(char lvStart) {
			this.lvStart = lvStart;
			return this;
		}
		
		public ConditionBuilder setLvEnd(char lvEnd) {
			this.lvEnd = lvEnd;
			return this;
		}

		public ConditionBuilder setTurnRange(int turnStart, int turnEnd) {
			this.turnStart = turnStart;
			this.turnEnd = turnEnd;
			return this;
		}
		
		public ConditionBuilder setVersion(String version) {
			this.version = version;
			return this;
		}
		
		public ConditionBuilder setJsonFileOutput(boolean jsonFileOutput) {
			this.jsonFileOutput = jsonFileOutput;
			return this;
		}
		
		private void setLevels() {
			this.levels = new ArrayList<>();
			this.interval = this.interval < 1 || 2 < this.interval ? 2 : this.interval;
					
			if (this.interval == 2) {
				for (char lv = this.lvStart; lv <= 'L'; lv += 2) {
					this.levels.add(String.valueOf(lv));
				}
			} else {
				for (char lv = this.lvStart; lv <= 'L'; lv++) {
					this.levels.add(String.valueOf(lv));
				}
			}
		}
		
		private void setTurns() {
			this.turns = new ArrayList<>();
			this.lvStart = this.lvStart != 'A' && this.lvStart != 'B' ? 'A' : this.lvStart;
			
			for (int turn = this.turnStart; turn <= this.turnEnd; turn++) {
				this.turns.add(String.valueOf(turn));
			}
		}
	}
	
	public static ConditionBuilder builder() {
		return new ConditionBuilder();
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
}
