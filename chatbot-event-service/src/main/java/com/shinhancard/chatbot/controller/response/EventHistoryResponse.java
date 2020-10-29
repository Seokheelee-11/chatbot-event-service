package com.shinhancard.chatbot.controller.response;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shinhancard.chatbot.domain.EventInfo;
import com.shinhancard.chatbot.domain.ResultCode;

import lombok.Data;

@Data
public class EventHistoryResponse {

	// 결과메세지
	private String resultCode;
	private String resultMessage;

	// request 기본 입력값
	private String eventId;
	private String clnn;
	private String displayName;

	private Integer lastOrder;
	private LocalDateTime lastModDt;

	private List<String> param;
	private String rewardName;
	private String responseMessage;

	private Integer eventAppliedOrder;
	private long dDateCount;

	private Map<String, String> resultInfo = new HashMap<String, String>();

	public EventHistoryResponse() {
		param = new ArrayList<String>();
	}

	public void setResult(ResultCode result) {
		this.resultCode = result.getResultCode();
		this.resultMessage = result.getResultMessage();
	}

	public void setDDateCount(EventInfo eventInfo) {
		long dDateCount = 0;
		if (eventInfo.getOverLapDateType().isMinute()) {
			dDateCount = ChronoUnit.MINUTES.between(eventInfo.getStartDt().withSecond(0).withNano(0),
					this.lastModDt.withSecond(0).withNano(0));
		} else if (eventInfo.getOverLapDateType().isHour()) {
			dDateCount = ChronoUnit.HOURS.between(eventInfo.getStartDt().withMinute(0).withSecond(0).withNano(0),
					this.lastModDt.withMinute(0).withSecond(0).withNano(0));
		} else if (eventInfo.getOverLapDateType().isDay()) {
			dDateCount = ChronoUnit.DAYS.between(
					eventInfo.getStartDt().withHour(0).withMinute(0).withSecond(0).withNano(0),
					this.lastModDt.withHour(0).withMinute(0).withSecond(0).withNano(0));
		} else if (eventInfo.getOverLapDateType().isMonth()) {
			dDateCount = ChronoUnit.MONTHS.between(
					eventInfo.getStartDt().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0),
					this.lastModDt.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0));
		} else if (eventInfo.getOverLapDateType().isYear()) {
			dDateCount = ChronoUnit.YEARS.between(
					eventInfo.getStartDt().withDayOfYear(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0)
							.withNano(0),
					this.lastModDt.withDayOfYear(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0)
							.withNano(0));
		}
		this.dDateCount = dDateCount;

	}
}