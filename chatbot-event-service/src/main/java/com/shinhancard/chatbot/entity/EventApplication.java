package com.shinhancard.chatbot.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;

import com.shinhancard.chatbot.domain.EventApplicationLog;

import lombok.Data;

@Data
public class EventApplication {
	@Id
	private String id;

//	@NotEmpty @NotBlank 
	private String eventId;

//	@NotEmpty @NotBlank
//	@Size(min=10,max=10,message="고객번호는 10자 이여야 합니다.")
	private String clnn;

	private List<EventApplicationLog> applicationLogs = new ArrayList<EventApplicationLog>();

	public EventApplication() {

	}

	public EventApplicationLog getLastApplicationLog() {
		return this.applicationLogs.get(this.applicationLogs.size() - 1);
	}

	public EventApplicationLog getLastApplicationLog(String channel) {
		EventApplicationLog lastEventApplicationLog = new EventApplicationLog();
		for (EventApplicationLog applicationLog : this.applicationLogs) {
			if(StringUtils.equals(channel, applicationLog.getChannel())){
				lastEventApplicationLog = applicationLog;
			}
			
		}
		return lastEventApplicationLog;
	}

	public List<EventApplicationLog> getApplicationLogs(String channel) {
		List<EventApplicationLog> eventApplicationLogs = new ArrayList<>();
		for (EventApplicationLog applicationLog : this.applicationLogs) {
			if(StringUtils.equals(channel, applicationLog.getChannel())){
				eventApplicationLogs.add(applicationLog);
			}
		}
		return eventApplicationLogs;
	}

	public Integer getApplicationCount() {
		return this.applicationLogs.size();
	}

	public LocalDateTime getLastApplyDate() {
		return this.applicationLogs.get(this.applicationLogs.size() - 1).getApplyDate();
	}

	public Integer getRewardAppliedNumber(String rewardName) {
		Integer totalNumber = 0;
		for (EventApplicationLog applicationLog : this.applicationLogs) {
			if(StringUtils.equals(rewardName, applicationLog.getRewardName())) {
				totalNumber++;
			}
		}
		return totalNumber;
	}

	public void addApplicationLogs(EventApplicationLog eventApplicationLog) {
		this.applicationLogs.add(eventApplicationLog);
	}

}
