package com.shinhancard.chatbot.domain;

public enum OverLapCode {
	ALLTIME,
	MINUTE,
	HOUR,
	DAY,
	MONTH,
	YEAR;
	
	public Boolean isAllTime() {
		if (this.equals(ALLTIME)) {
			return true;
		}
		return false;
	}

	public Boolean isMinute() {
		if (this.equals(MINUTE)) {
			return true;
		}
		return false;
	}

	public Boolean isHour() {
		if (this.equals(HOUR)) {
			return true;
		}
		return false;
	}

	public Boolean isDay() {
		if (this.equals(DAY)) {
			return true;
		}
		return false;
	}

	public Boolean isMonth() {
		if (this.equals(MONTH)) {
			return true;
		}
		return false;
	}

	public Boolean isYear() {
		if (this.equals(YEAR)) {
			return true;
		}
		return false;
	}
	
}
