package com.shinhancard.chatbot.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shinhancard.chatbot.domain.EventApplicationLog;
import com.shinhancard.chatbot.domain.OverLap;
import com.shinhancard.chatbot.domain.OverLapCode;
import com.shinhancard.chatbot.domain.PropertyCode;
import com.shinhancard.chatbot.domain.ResultCode;
import com.shinhancard.chatbot.dto.request.EventApplicationRequest;
import com.shinhancard.chatbot.dto.response.EventApplicationResponse;
import com.shinhancard.chatbot.entity.EventApplication;
import com.shinhancard.chatbot.entity.EventManage;
import com.shinhancard.chatbot.entity.EventTarget;
import com.shinhancard.chatbot.repository.EventApplicationRepository;
import com.shinhancard.chatbot.repository.EventManageRepository;
import com.shinhancard.chatbot.repository.EventTargetRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventApplicationService {
	@Autowired
	private ModelMapper modelMapper;
	private final EventApplicationRepository eventApplicationRepository;
	private final EventManageRepository eventManageRepository;
	private final EventTargetRepository eventTargetRepository;

	public List<EventApplicationResponse> getEvents() {
		List<EventApplicationResponse> eventApplicationResponses = new ArrayList<EventApplicationResponse>();
		return eventApplicationResponses;
	}

	public EventApplicationResponse getEventById(String id) {
		EventApplicationResponse eventApplicationResponse = new EventApplicationResponse();
		return eventApplicationResponse;
	}

	public EventApplicationResponse applicationEvent(EventApplicationRequest eventApplicationRequest) {
		EventManage eventManage = findEventManageByEventApplicationRequest(eventApplicationRequest);
		EventApplicationLog eventApplicationLog = new EventApplicationLog(eventApplicationRequest);
		List<PropertyCode> properties = eventManage.getProperties();
		ResultCode resultCode = ResultCode.SUCCESS;

		if (properties.contains(PropertyCode.DEFAULT)) {
			resultCode = canApplyDate(eventManage, eventApplicationLog, resultCode);
		}

		if (properties.contains(PropertyCode.TARGET)) {
			resultCode = canApplyTarget(eventManage, eventApplicationRequest, resultCode);
		}

		if (properties.contains(PropertyCode.QUIZ)) {
			resultCode = canApplyQuiz(eventManage, eventApplicationRequest, resultCode);
		}

		if (properties.contains(PropertyCode.OVERLAP)) {
			resultCode = canApplyOverLap(eventManage, eventApplicationRequest, resultCode, eventApplicationLog);
			eventApplicationLog.setOrder(getOverLapOrder(eventApplicationRequest, resultCode));
		}

		if (properties.contains(PropertyCode.REWARD)) {
			resultCode = canApplyReward(eventManage, eventApplicationRequest, resultCode);
			eventApplicationLog.setRewardName(getReward(eventManage, resultCode));

		}

		EventApplication eventApplication = new EventApplication();
		eventApplicationRepository.save(eventApplication);
		EventApplicationResponse eventApplicationResponse = modelMapper.map(eventApplication,
				EventApplicationResponse.class);

		log.info("saved entity {}", eventApplicationResponse.toString());

		return eventApplicationResponse;
	}

	public EventApplicationResponse updateEvent(String id, EventApplicationRequest eventApplicationRequest) {
		EventApplication eventApplication = modelMapper.map(eventApplicationRequest, EventApplication.class);
		eventApplication.setId(id);
		eventApplicationRepository.save(eventApplication);
		EventApplicationResponse eventApplicationResponse = modelMapper.map(eventApplication,
				EventApplicationResponse.class);
		return eventApplicationResponse;
	}

	public void deleteEvent(String id) {

	}

	public ResultCode canApplyDate(EventManage eventManage, EventApplicationLog eventApplicationLog,
			ResultCode resultCode) {
		if (resultCode.isSuccess()) {
			LocalDateTime startDate = eventManage.getDefaultInfo().getStartDate();
			LocalDateTime endDate = eventManage.getDefaultInfo().getEndDate();
			LocalDateTime ApplyDate = eventApplicationLog.getApplyDate();

			if (startDate.isAfter(ApplyDate) || endDate.isBefore(ApplyDate)) {
				resultCode = ResultCode.FAILED_DATE_ORDER;
			}
		}
		return resultCode;
	}

	public ResultCode canApplyTarget(EventManage eventManage, EventApplicationRequest eventApplicationRequest,
			ResultCode resultCode) {
		if (resultCode.isSuccess()) {
			String targetName = eventManage.getTarget().getTargetName();
			String nonTargetName = eventManage.getTarget().getNonTargetName();
			EventTarget target = eventTargetRepository.findOneByName(targetName);
			EventTarget nonTarget = eventTargetRepository.findOneByName(nonTargetName);
			List<String> targetClnns = target.getClnns();
			List<String> nonTargetClnns = nonTarget.getClnns();
			List<String> channels = eventManage.getTarget().getChannels();

			if (!targetClnns.contains(eventApplicationRequest.getClnn())) {
				resultCode = ResultCode.FAILED;
			}
			if (nonTargetClnns.contains(eventApplicationRequest.getClnn())) {
				resultCode = ResultCode.FAILED;
			}
			if (channels.contains(eventApplicationRequest.getChannel())) {
				resultCode = ResultCode.FAILED;
			}
		}
		return resultCode;
	}

	public ResultCode canApplyQuiz(EventManage eventManage, EventApplicationRequest eventApplicationRequest,
			ResultCode resultCode) {
		if (resultCode.isSuccess()) {
			List<String> comments = eventApplicationRequest.getComments();
			List<String> answers = eventManage.getQuiz().getAnswers();
			if (eventManage.getQuiz().getChecksOneAnswer()) {
				Boolean isCorrect = false;
				for (String comment : comments) {
					isCorrect = answers.contains(comment) ? true : isCorrect;
				}
				resultCode = isCorrect ? resultCode : ResultCode.FAILED;
			}
		}
		return resultCode;
	}

	public ResultCode canApplyOverLap(EventManage eventManage, EventApplicationRequest eventApplicationRequest,
			ResultCode resultCode, EventApplicationLog eventApplicationLog) {

		Boolean canApply = true;
		if (resultCode.isSuccess()) {

			OverLap overLap = eventManage.getOverLap();
			String clnn = eventApplicationRequest.getClnn();
			String eventId = eventApplicationRequest.getEventId();

			EventApplication findEventApplication = eventApplicationRepository.findOneByEventIdAndClnn(eventId, clnn);

			if (findEventApplication != null) {
				LocalDateTime lastApplyDate = findEventApplication.getLastApplyDate();

				canApply = overLap.getLimit() <= findEventApplication.getLastOrder() + 1 ? false : canApply;
				canApply = overLap.getIsStartPastDate()
						? canNoIncludeOverLap(overLap, eventApplicationLog, lastApplyDate)
						: canIncludeOverLap(overLap, eventApplicationLog, lastApplyDate);

			}

		}
		return canApply == true ? resultCode : ResultCode.FAILED;
	}

	public Boolean canNoIncludeOverLap(OverLap overLap, EventApplicationLog eventApplicationLog,
			LocalDateTime lastApplyDate) {
		OverLapCode overLapType = overLap.getType();
		LocalDateTime applyDate = eventApplicationLog.getApplyDate();
		Boolean result = true;
		
		if (overLap.getMaxInterval() != null) {
			result = result ? canApply_NoIncludeOverLap_MaxInterval(overLapType, applyDate,lastApplyDate, overLap.getMaxInterval()) : result;
		}
		if (overLap.getMinInterval() != null) {
			result = result ? canApply_NoIncludeOverLap_MinInterval(overLapType, applyDate,lastApplyDate, overLap.getMinInterval()) : result;
		}
		return result;
	}
	
	
	public Boolean canIncludeOverLap(OverLap overLap, EventApplicationLog eventApplicationLog,
			LocalDateTime lastApplyDate) {
		OverLapCode overLapType = overLap.getType();
		LocalDateTime applyDate = eventApplicationLog.getApplyDate();
		Boolean result = true;
		
		if (overLap.getMaxInterval() != null) {
			result = result ? canApply_IncludeOverLap_MaxInterval(overLapType, applyDate,lastApplyDate, overLap.getMaxInterval()) : result;
		}
		if (overLap.getMinInterval() != null) {
			result = result ? canApply_IncludeOverLap_MinInterval(overLapType, applyDate,lastApplyDate, overLap.getMinInterval()) : result;
		}

		return result;
	}

	public Boolean canApply_NoIncludeOverLap_MaxInterval(OverLapCode overLapType, LocalDateTime applyDate,
			LocalDateTime lastApplyDate, Integer interval) {
		if (overLapType.isMinute() && applyDate.isBefore(lastApplyDate.plusMinutes(interval))) {
			return false;
		} else if (overLapType.isHour() && applyDate.isBefore(lastApplyDate.plusHours(interval))) {
			return false;
		} else if (overLapType.isDay() && applyDate.isBefore(lastApplyDate.plusDays(interval))) {
			return false;
		} else if (overLapType.isMonth() && applyDate.isBefore(lastApplyDate.plusMonths(interval))) {
			return false;
		} else if (overLapType.isYear() && applyDate.isBefore(lastApplyDate.plusYears(interval))) {
			return false;
		}
		return true;
	}

	public Boolean canApply_NoIncludeOverLap_MinInterval(OverLapCode overLapType, LocalDateTime applyDate,
			LocalDateTime lastApplyDate, Integer interval) {
		if (overLapType.isMinute() && applyDate.isAfter(lastApplyDate.plusMinutes(interval))) {
			return false;
		} else if (overLapType.isHour() && applyDate.isAfter(lastApplyDate.plusHours(interval))) {
			return false;
		} else if (overLapType.isDay() && applyDate.isAfter(lastApplyDate.plusDays(interval))) {
			return false;
		} else if (overLapType.isMonth() && applyDate.isAfter(lastApplyDate.plusMonths(interval))) {
			return false;
		} else if (overLapType.isYear() && applyDate.isAfter(lastApplyDate.plusYears(interval))) {
			return false;
		}
		return true;
	}
	

	
	public Boolean canApply_IncludeOverLap_MaxInterval(OverLapCode overLapType, LocalDateTime applyDate,
			LocalDateTime lastApplyDate, Integer interval) {
		if (overLapType.isMinute() && ChronoUnit.MINUTES.between(getWithMinute(lastApplyDate),
				getWithMinute(applyDate)) < interval) {
			return false;
		} else if (overLapType.isHour() && ChronoUnit.HOURS.between(getWithHour(lastApplyDate),
				getWithHour(applyDate)) < interval) {
			return false;
		} else if (overLapType.isDay() && ChronoUnit.DAYS.between(getWithDay(lastApplyDate),
				getWithDay(applyDate)) < interval) {
			return false;
		} else if (overLapType.isMonth() && ChronoUnit.MONTHS.between(getWithMonth(lastApplyDate),
				getWithMonth(applyDate)) < interval) {
			return false;
		} else if (overLapType.isYear() && ChronoUnit.YEARS.between(getWithYear(lastApplyDate),
				getWithYear(applyDate)) < interval) {
			return false;
		}
		return true;
	}
	
	public Boolean canApply_IncludeOverLap_MinInterval(OverLapCode overLapType, LocalDateTime applyDate,
			LocalDateTime lastApplyDate, Integer interval) {
		if (overLapType.isMinute() && ChronoUnit.MINUTES.between(getWithMinute(lastApplyDate),
				getWithMinute(applyDate)) > interval) {
			return false;
		} else if (overLapType.isHour() && ChronoUnit.HOURS.between(getWithHour(lastApplyDate),
				getWithHour(applyDate)) > interval) {
			return false;
		} else if (overLapType.isDay() && ChronoUnit.DAYS.between(getWithDay(lastApplyDate),
				getWithDay(applyDate)) > interval) {
			return false;
		} else if (overLapType.isMonth() && ChronoUnit.MONTHS.between(getWithMonth(lastApplyDate),
				getWithMonth(applyDate)) > interval) {
			return false;
		} else if (overLapType.isYear() && ChronoUnit.YEARS.between(getWithYear(lastApplyDate),
				getWithYear(applyDate)) > interval) {
			return false;
		}
		return true;
	}
	
	
	public LocalDateTime getWithMinute(LocalDateTime localDateTime) {
		return localDateTime.withSecond(0).withNano(0);
	}

	public LocalDateTime getWithHour(LocalDateTime localDateTime) {
		return localDateTime.withMinute(0).withSecond(0).withNano(0);
	}

	public LocalDateTime getWithDay(LocalDateTime localDateTime) {
		return localDateTime.withHour(0).withMinute(0).withSecond(0).withNano(0);
	}

	public LocalDateTime getWithMonth(LocalDateTime localDateTime) {
		return localDateTime.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
	}

	public LocalDateTime getWithYear(LocalDateTime localDateTime) {
		return localDateTime.withDayOfYear(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
	}

	
	
	// TODO :: 함수 만들 것
	public ResultCode canApplyReward(EventManage eventManage, EventApplicationRequest eventApplicationRequest,
			ResultCode resultCode) {
		if (resultCode.isSuccess()) {

		}
		return resultCode;
	}

	// TODO :: 함수 만들 것
	public Integer getOverLapOrder(EventApplicationRequest eventApplicationRequest, ResultCode resultCode) {
		Integer result = 0;
		if (resultCode.isSuccess()) {
			String clnn = eventApplicationRequest.getClnn();
			String eventId = eventApplicationRequest.getEventId();

			EventApplication findEventApplication = eventApplicationRepository.findOneByEventIdAndClnn(eventId, clnn);
			result = findEventApplication.getLastOrder() + 1;
		}
		return result;
	}

	// TODO :: 함수 만들 것
	public String getReward(EventManage eventManage, ResultCode resultCode) {
		String result = "";
		if (resultCode.isSuccess()) {

		}
		return result;
	}

	public EventManage findEventManageByEventApplicationRequest(EventApplicationRequest eventApplicationRequest) {
		String eventId = eventApplicationRequest.getEventId();
		EventManage eventManage = eventManageRepository.findOneByEventId(eventId);
		return eventManage;
	}

	public List<PropertyCode> getEventProperties(EventManage eventManage) {
		List<PropertyCode> properties = new ArrayList<>();
		properties.addAll(eventManage.getProperties());
		return properties;
	}

}
