package com.shinhancard.chatbot.domain.past;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class EventHistory {
	@Id
	private String id;

	@NotEmpty @NotBlank 
	private String eventId;
	
	@NotEmpty @NotBlank
	@Size(min=10,max=10,message="고객번호는 10자 이여야 합니다.")
	private String clnn;
	private String displayName;

	private Integer lastOrder;
	private List<EventHistoryLog> logs;
	// TODO :: 최소한의 필드만 넣어놨으니 더 추가할 것

	private LocalDateTime lastModDt;
	
	public EventHistory() {
		this.logs = new ArrayList<EventHistoryLog>();
	}

	public void addLog(EventHistoryLog log) {
		this.logs.add(log);
	}

	public void update(EventHistory newEvent) {
		this.eventId = newEvent.getEventId();
		this.displayName = newEvent.getDisplayName();

	}

	public void updateLastOrder(int count) {
		this.lastOrder = count;
		this.lastModDt = LocalDateTime.now();
	}

	public EventHistory setEventHistory(EventHistoryLog eventHistoryLog) {
		this.lastOrder = eventHistoryLog.getOrder();
		this.lastModDt = eventHistoryLog.getRegDate();
		this.logs.add(eventHistoryLog);
		return this;
	}
	
	public EventHistoryLog getLastHistory() {
		EventHistoryLog result =this.logs.get(this.logs.size()-1);
		return result;
	}
	
	
	public Boolean canApplyOverLap(EventInfo findEventInfo, EventHistoryLog eventHistoryLog) {
		// include인 경우 로직
		if(findEventInfo.getIncludeDateTF()) {
			if(!canIncludeOverLap(findEventInfo, eventHistoryLog)) {
				return false;
			}
		}
		// include가 아닌 경우 로직
		else {
			if(!canNoIncludeOverLap(findEventInfo, eventHistoryLog)) {
				return false;
			}
		}
		return true;
	}
	
	public Boolean canNoIncludeOverLap(EventInfo findEventInfo, EventHistoryLog eventHistoryLog) {
		if (findEventInfo.getOverLapDateType().isMinute() && eventHistoryLog.getRegDate()
				.isBefore(this.lastModDt.plusMinutes(findEventInfo.getOverLapDateCount()))) {
			return false;
		} else if (findEventInfo.getOverLapDateType().isHour() && eventHistoryLog.getRegDate()
				.isBefore(this.lastModDt.plusHours(findEventInfo.getOverLapDateCount()))) {
			return false;
		} else if (findEventInfo.getOverLapDateType().isDay() && eventHistoryLog.getRegDate()
				.isBefore(this.lastModDt.plusDays(findEventInfo.getOverLapDateCount()))) {
			return false;
		} else if (findEventInfo.getOverLapDateType().isMonth() && eventHistoryLog.getRegDate()
				.isBefore(this.lastModDt.plusMonths(findEventInfo.getOverLapDateCount()))) {
			return false;
		} else if (findEventInfo.getOverLapDateType().isYear() && eventHistoryLog.getRegDate()
				.isBefore(this.lastModDt.plusYears(findEventInfo.getOverLapDateCount()))) {
			return false;
		}
		return true;
	}

	public Boolean canIncludeOverLap(EventInfo findEventInfo, EventHistoryLog eventHistoryLog) {
		log.info("minute : {}",ChronoUnit.MINUTES.between(this.lastModDt.withSecond(0).withNano(0),eventHistoryLog.getRegDate().withSecond(0).withNano(0)));
		log.info("minute : {}",this.lastModDt);
		log.info("minute : {}",eventHistoryLog.getRegDate());
		log.info("minute : {}",this.lastModDt.withSecond(0).withNano(0));
		log.info("minute : {}",this.lastModDt.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0));
		log.info("minute : {}",this.lastModDt.withDayOfYear(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0));
		
		log.info("minute : {}",eventHistoryLog.getRegDate().withSecond(0).withNano(0));
		if (findEventInfo.getOverLapDateType().isMinute()&& ChronoUnit.MINUTES.between(this.lastModDt.withSecond(0).withNano(0),
				eventHistoryLog.getRegDate().withSecond(0).withNano(0)) < findEventInfo.getOverLapDateCount()) {
			return false;
		}else if (findEventInfo.getOverLapDateType().isHour() && ChronoUnit.HOURS.between(this.lastModDt.withMinute(0).withSecond(0).withNano(0),
				eventHistoryLog.getRegDate().withMinute(0).withSecond(0).withNano(0)) < findEventInfo.getOverLapDateCount()) {
			return false;
		} else if (findEventInfo.getOverLapDateType().isDay() && ChronoUnit.DAYS.between(this.lastModDt.withHour(0).withMinute(0).withSecond(0).withNano(0),
				eventHistoryLog.getRegDate().withHour(0).withMinute(0).withSecond(0).withNano(0)) < findEventInfo.getOverLapDateCount()) {
			return false;
		} else if (findEventInfo.getOverLapDateType().isMonth() && ChronoUnit.MONTHS.between(this.lastModDt.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0),
				eventHistoryLog.getRegDate().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0)) < findEventInfo.getOverLapDateCount()) {
			return false;
		} else if (findEventInfo.getOverLapDateType().isYear() && ChronoUnit.YEARS.between(this.lastModDt.withDayOfYear(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0),
				eventHistoryLog.getRegDate().withDayOfYear(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0)) < findEventInfo.getOverLapDateCount()) {
			return false;
		}
		return true;
	}

	public Boolean getValidationEventIdInput() {
		if (this.eventId.isEmpty()) {
			return false;
		}
		return true;
	}

	public Boolean getValidationClnnInput() {
		if (this.clnn.isEmpty()) {
			return false;
		}
		return true;
	}

}
