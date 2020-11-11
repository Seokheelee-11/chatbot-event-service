package com.shinhancard.chatbot.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shinhancard.chatbot.domain.DefaultInfo;
import com.shinhancard.chatbot.domain.EventApplicationLog;
import com.shinhancard.chatbot.domain.EventInfo;
import com.shinhancard.chatbot.domain.OverLap;
import com.shinhancard.chatbot.domain.OverLapCode;
import com.shinhancard.chatbot.domain.PropertyCode;
import com.shinhancard.chatbot.domain.Response;
import com.shinhancard.chatbot.domain.ResponseInfo;
import com.shinhancard.chatbot.domain.ResultCode;
import com.shinhancard.chatbot.domain.Reward;
import com.shinhancard.chatbot.domain.RewardCode;
import com.shinhancard.chatbot.domain.RewardInfo;
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
			eventApplicationLog.setRewardName(getReward(eventManage, eventApplicationRequest, resultCode));

		}

		EventApplication eventApplication = saveEventApplication(eventManage, eventApplicationLog, eventApplicationRequest, resultCode);
		EventApplicationResponse eventApplicationResponse = setEventApplicationResponse(eventManage, eventApplicationLog, resultCode, eventApplication);
		
		return eventApplicationResponse;
		
//		saveEventApplication(eventManage, eventApplicationLog, eventApplicationRequest, resultCode);
//		EventApplication eventApplication = new EventApplication();
//		eventApplicationRepository.save(eventApplication);
//		EventApplicationResponse eventApplicationResponse = modelMapper.map(eventApplication,
//				EventApplicationResponse.class);
//		log.info("saved entity {}", eventApplicationResponse.toString());
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

	// TODO :: 함수 만들 것
	public EventApplication saveEventApplication(EventManage eventManage, EventApplicationLog eventApplicationLog,
			EventApplicationRequest eventApplicationRequest, ResultCode resultCode) {
		String eventId = eventApplicationRequest.getEventId();
		String clnn = eventApplicationRequest.getClnn();

		EventApplication eventApplication = eventApplicationRepository.findOneByEventIdAndClnn(eventId, clnn);
		if(eventApplication == null) {
			eventApplication = new EventApplication();
			eventApplication.setClnn(clnn);
			eventApplication.setEventId(eventId);
		}		
		eventApplication.addApplicationLogs(eventApplicationLog);
		eventApplicationRepository.save(eventApplication);
		
		 return eventApplication;
	}

	public EventApplicationResponse setEventApplicationResponse(EventManage eventManage, EventApplicationLog eventApplicationLog,
			ResultCode resultCode, EventApplication eventApplication) {
		EventApplicationResponse eventApplicationResponse = new EventApplicationResponse();

		eventApplicationResponse.setResultCodeAndMessage(resultCode);
		eventApplicationResponse.setClnn(eventApplication.getClnn());
		eventApplicationResponse.setEventInfo(setEventInfo(eventManage));
		eventApplicationResponse.setEventApplicationLog(eventApplicationLog);
		eventApplicationResponse.setResponseInfo(setResponseInfo(eventManage, eventApplicationLog, resultCode));
			
		return eventApplicationResponse;
	}

	public ResponseInfo setResponseInfo(EventManage eventManage, EventApplicationLog eventApplicationLog,
			ResultCode resultCode) {
		ResponseInfo responseInfo = new ResponseInfo();
		
		if (resultCode.isSuccess()) {
			if (eventManage.getReward().getIsProperty()) {
				String rewardName = eventApplicationLog.getRewardName();
				RewardInfo rewardInfo = eventManage.getReward().getInfoByRewardName(rewardName);
				responseInfo.setResponseMessage(rewardInfo.getMessage());
				responseInfo.setInfoes(rewardInfo.getInfoes());
			} else {
				Response response = eventManage.getResponse();
				responseInfo.setResponseMessage(response.getSuccessMessage());
				responseInfo.setInfoes(response.getInfoes());
			}
		} else {
			Response response = eventManage.getResponse();
			responseInfo.setResponseMessage(response.getFailureMessage());
		}
		return responseInfo;
	}

	public EventInfo setEventInfo(EventManage eventManage) {
		EventInfo eventInfo = new EventInfo();
		DefaultInfo defaultInfo = eventManage.getDefaultInfo();

		eventInfo.setEventId(eventManage.getEventId());
		eventInfo.setName(defaultInfo.getName());
		eventInfo.setDiscription(defaultInfo.getDescription());
		eventInfo.setStartDate(defaultInfo.getStartDate());
		eventInfo.setEndDate(defaultInfo.getEndDate());

		return eventInfo;
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
			result = result
					? canApply_NoIncludeOverLap_MaxInterval(overLapType, applyDate, lastApplyDate,
							overLap.getMaxInterval())
					: result;
		}
		if (overLap.getMinInterval() != null) {
			result = result
					? canApply_NoIncludeOverLap_MinInterval(overLapType, applyDate, lastApplyDate,
							overLap.getMinInterval())
					: result;
		}
		return result;
	}

	public Boolean canIncludeOverLap(OverLap overLap, EventApplicationLog eventApplicationLog,
			LocalDateTime lastApplyDate) {
		OverLapCode overLapType = overLap.getType();
		LocalDateTime applyDate = eventApplicationLog.getApplyDate();
		Boolean result = true;

		if (overLap.getMaxInterval() != null) {
			result = result
					? canApply_IncludeOverLap_MaxInterval(overLapType, applyDate, lastApplyDate,
							overLap.getMaxInterval())
					: result;
		}
		if (overLap.getMinInterval() != null) {
			result = result
					? canApply_IncludeOverLap_MinInterval(overLapType, applyDate, lastApplyDate,
							overLap.getMinInterval())
					: result;
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
		if (overLapType.isMinute()
				&& ChronoUnit.MINUTES.between(getWithMinute(lastApplyDate), getWithMinute(applyDate)) < interval) {
			return false;
		} else if (overLapType.isHour()
				&& ChronoUnit.HOURS.between(getWithHour(lastApplyDate), getWithHour(applyDate)) < interval) {
			return false;
		} else if (overLapType.isDay()
				&& ChronoUnit.DAYS.between(getWithDay(lastApplyDate), getWithDay(applyDate)) < interval) {
			return false;
		} else if (overLapType.isMonth()
				&& ChronoUnit.MONTHS.between(getWithMonth(lastApplyDate), getWithMonth(applyDate)) < interval) {
			return false;
		} else if (overLapType.isYear()
				&& ChronoUnit.YEARS.between(getWithYear(lastApplyDate), getWithYear(applyDate)) < interval) {
			return false;
		}
		return true;
	}

	public Boolean canApply_IncludeOverLap_MinInterval(OverLapCode overLapType, LocalDateTime applyDate,
			LocalDateTime lastApplyDate, Integer interval) {
		if (overLapType.isMinute()
				&& ChronoUnit.MINUTES.between(getWithMinute(lastApplyDate), getWithMinute(applyDate)) > interval) {
			return false;
		} else if (overLapType.isHour()
				&& ChronoUnit.HOURS.between(getWithHour(lastApplyDate), getWithHour(applyDate)) > interval) {
			return false;
		} else if (overLapType.isDay()
				&& ChronoUnit.DAYS.between(getWithDay(lastApplyDate), getWithDay(applyDate)) > interval) {
			return false;
		} else if (overLapType.isMonth()
				&& ChronoUnit.MONTHS.between(getWithMonth(lastApplyDate), getWithMonth(applyDate)) > interval) {
			return false;
		} else if (overLapType.isYear()
				&& ChronoUnit.YEARS.between(getWithYear(lastApplyDate), getWithYear(applyDate)) > interval) {
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
	public ResultCode canApplyReward(EventManage eventManage, EventApplicationRequest eventApplicationRequest,
			ResultCode resultCode) {
		Reward reward = eventManage.getReward();
		String eventId = eventApplicationRequest.getEventId();
		if (resultCode.isSuccess()) {
			List<EventApplication> findAllEventApplications = new ArrayList<>();
			findAllEventApplications = eventApplicationRepository.findAllByEventId(eventId);
			if (canRewardLimit(reward, findAllEventApplications)) {
			} else {
				resultCode = ResultCode.FAILED;
			}
		}
		return resultCode;
	}

	// TODO :: 함수 만들 것
	public String getReward(EventManage eventManage, EventApplicationRequest eventApplicationRequest,
			ResultCode resultCode) {
		String result = "";
		Reward reward = eventManage.getReward();
		String eventId = eventApplicationRequest.getEventId();
		if (resultCode.isSuccess()) {
			List<EventApplication> findAllEventApplications = new ArrayList<>();
			findAllEventApplications = eventApplicationRepository.findAllByEventId(eventId);
			result = getRewardName(reward, findAllEventApplications);
		}
		return result;
	}

	public String getRewardName(Reward reward, List<EventApplication> eventApplications) {
		String rewardName = "";
		Map<String, Integer> manageRewardLimit = setManageRewardLimit(reward, eventApplications);
		Map<String, Integer> appliedRewardLimit = setappliedRewardLimit(reward, eventApplications);
		Map<String, Double> manageRewardProbability = setManageRewardProbability(reward, eventApplications);

		if (reward.getType().equals(RewardCode.DRAWROTS)) {
			rewardName = getRewardDrawRots(manageRewardLimit, appliedRewardLimit, manageRewardProbability);
		} else if (reward.getType().equals(RewardCode.FCFS)) {
			rewardName = getRewardFCFS(manageRewardLimit, appliedRewardLimit);
		}
		return rewardName;
	}

	public String getRewardDrawRots(Map<String, Integer> manageRewardLimit, Map<String, Integer> appliedRewardLimit,
			Map<String, Double> manageRewardProbability) {
		List<String> canApplyReward = getCanApplyReward(manageRewardLimit, appliedRewardLimit);
		Double sumProbability = getSumProbability(canApplyReward, manageRewardProbability);
		Random rand = new Random();
		Double randProbability = rand.nextDouble() % sumProbability;
		String rewardName = "";

		for (String key : canApplyReward) {
			randProbability -= manageRewardProbability.get(key);
			if (randProbability < 0) {
				rewardName = key;
			}
		}

		return rewardName;

	}

	public List<String> getCanApplyReward(Map<String, Integer> manageRewardLimit,
			Map<String, Integer> appliedRewardLimit) {
		List<String> rewards = new ArrayList<>();
		for (String key : manageRewardLimit.keySet()) {
			if (manageRewardLimit.get(key) > appliedRewardLimit.get(key)) {
				rewards.add(key);
			}
		}
		return rewards;
	}

	public Double getSumProbability(List<String> canApplyReward, Map<String, Double> manageRewardProbability) {
		Double sumProbability = 0.0;
		for (String rewardName : canApplyReward) {
			sumProbability += manageRewardProbability.get(rewardName);
		}
		return sumProbability;
	}

	public String getRewardFCFS(Map<String, Integer> manageRewardLimit, Map<String, Integer> appliedRewardLimit) {
		String result = "";
		for (String rewardName : manageRewardLimit.keySet()) {
			if (manageRewardLimit.get(rewardName) > appliedRewardLimit.get(rewardName)) {
				result = rewardName;
				break;
			}
		}
		return result;
	}

	public Boolean canRewardLimit(Reward reward, List<EventApplication> eventApplications) {
		Boolean result = false;
		Map<String, Integer> manageRewardLimit = setManageRewardLimit(reward, eventApplications);
		Map<String, Integer> appliedRewardLimit = setappliedRewardLimit(reward, eventApplications);

		for (String rewardName : manageRewardLimit.keySet()) {
			if (manageRewardLimit.get(rewardName) > appliedRewardLimit.get(rewardName)) {
				result = true;
				break;
			}
		}

		return result;
	}

	public Map<String, Integer> setManageRewardLimit(Reward reward, List<EventApplication> eventApplications) {
		Map<String, Integer> manageRewardLimit = new LinkedHashMap<>();

		for (RewardInfo info : reward.getInfoes()) {
			manageRewardLimit.put(info.getName(), info.getLimit());
		}
		return manageRewardLimit;
	}

	public Map<String, Double> setManageRewardProbability(Reward reward, List<EventApplication> eventApplications) {
		Map<String, Double> ManageRewardProbability = new LinkedHashMap<>();

		for (RewardInfo info : reward.getInfoes()) {
			ManageRewardProbability.put(info.getName(), info.getProbability());
		}
		return ManageRewardProbability;
	}

	public Map<String, Integer> setappliedRewardLimit(Reward reward, List<EventApplication> eventApplications) {

		Map<String, Integer> appliedRewardLimit = new LinkedHashMap<>();

		for (RewardInfo info : reward.getInfoes()) {
			Integer rewardApplied = 0;
			for (EventApplication eventApplication : eventApplications) {
				rewardApplied += eventApplication.getRewardAppliedNumber(info.getName());
			}
			appliedRewardLimit.put(info.getName(), rewardApplied);
		}
		return appliedRewardLimit;
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
