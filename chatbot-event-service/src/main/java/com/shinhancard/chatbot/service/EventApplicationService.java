package com.shinhancard.chatbot.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.shinhancard.chatbot.config.EventException;
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
import com.shinhancard.chatbot.entity.ApplyManage;
import com.shinhancard.chatbot.entity.EventApplication;
import com.shinhancard.chatbot.entity.EventManage;
import com.shinhancard.chatbot.entity.EventTarget;
import com.shinhancard.chatbot.repository.ApplyManageRepository;
import com.shinhancard.chatbot.repository.EventApplicationRepository;
import com.shinhancard.chatbot.repository.EventManageRepository;
import com.shinhancard.chatbot.repository.EventTargetRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@EnableRetry
@Slf4j
public class EventApplicationService {
	@Autowired
	private ModelMapper modelMapper;
	private final EventApplicationRepository eventApplicationRepository;
	private final EventManageRepository eventManageRepository;
	private final EventTargetRepository eventTargetRepository;
	private final ApplyManageRepository applyManageRepository;

	public List<EventApplication> getEvents() {
		List<EventApplication> eventApplications = eventApplicationRepository.findAll();
		return eventApplications;
	}

	@Transactional(readOnly = true)
	public EventApplication getEventById(String id) {
		EventApplication eventApplication = new EventApplication();
		eventApplication = eventApplicationRepository.findOneById(id);
		return eventApplication;
	}

	public EventApplicationResponse applicationEvent(EventApplicationRequest eventApplicationRequest)
			throws EventException {

		EventApplication eventApplication = getEventApplication(eventApplicationRequest);
		eventApplicationRepository.save(eventApplication);

		return getEventApplicationResponse(eventApplication);
	}

	public EventApplication updateEvent(String id, EventApplication eventApplication) {
		eventApplicationRepository.save(eventApplication);
		return eventApplication;
	}

	public void deleteEvent(String id) {
		eventApplicationRepository.deleteById(id);
	}

	@Transactional(value = "transactionManager", isolation = Isolation.READ_COMMITTED, readOnly = true, propagation = Propagation.MANDATORY)
	public EventApplication getEventApplication(EventApplicationRequest eventApplicationRequest) throws EventException {
		EventManage eventManage = eventManageRepository.findOneByEventId(eventApplicationRequest.getEventId());
		EventApplicationLog eventApplicationLog = getApplicationLog(eventManage, eventApplicationRequest);
		return setEventApplication(eventApplicationLog, eventApplicationRequest);
	}

	public EventApplicationLog getApplicationLog(EventManage eventManage,
			EventApplicationRequest eventApplicationRequest) throws EventException {
		EventApplicationLog eventApplicationLog = new EventApplicationLog(eventApplicationRequest);
		
		checkDefault(eventApplicationLog, eventManage, eventApplicationRequest);
		ApplyManage applyManage = applyManageRepository.findOneByEventId(eventApplicationRequest.getEventId());
				
		if (isNeedCheckOther(eventManage)) {
			checkOther(eventApplicationLog, eventManage, eventApplicationRequest);
		}
		
//		applyManageRepository.save(applyManage);
		if (isNeedSetApplicationLog(eventManage)) {
			eventApplicationLog = setApplicationLog(eventManage, eventApplicationRequest);
		}
		

		return eventApplicationLog;

	}
	
	public void increaseDefaultCountApplyManage(String eventId) {
		
	}

	public void canApplyCheck(EventApplicationLog eventApplicationLog, EventManage eventManage,
			EventApplicationRequest eventApplicationRequest) throws EventException {
		checkDefault(eventApplicationLog, eventManage, eventApplicationRequest);
		if (isNeedCheckOther(eventManage)) {
			checkOther(eventApplicationLog, eventManage, eventApplicationRequest);
		}
	}

	public Boolean isNeedSetApplicationLog(EventManage eventManage) {
		Boolean result = false;
		if (eventManage.getProperties().contains(PropertyCode.OVERLAP)
				|| eventManage.getProperties().contains(PropertyCode.REWARD)) {
			result = true;
		}
		return result;

	}

	public Boolean isNeedCheckOther(EventManage eventManage) {
		Boolean result = true;
		Integer limitApplication = eventManage.getDefaultInfo().getLimitApplication();
		Integer limitClnn = eventManage.getDefaultInfo().getLimitClnn();

		if (limitApplication != 0 || limitClnn != 0 || eventManage.getProperties().contains(PropertyCode.REWARD)) {
			result = true;
		} else {
			result = false;
		}
		return result;
	}

	public void checkOther(EventApplicationLog eventApplicationLog, EventManage eventManage,
			EventApplicationRequest eventApplicationRequest) throws EventException {

		if (!canApplyTotalLimit(eventManage)) {
			throw new EventException(ResultCode.FAILED_APPLY_OVER);
		}

		if (!canApplyReward(eventManage, eventApplicationRequest)) {
			throw new EventException(ResultCode.FAILED_GET_REWARD);
		}
	}

	public void checkDefault(EventApplicationLog eventApplicationLog, EventManage eventManage,
			EventApplicationRequest eventApplicationRequest) throws EventException {
		if (eventManage == null) {
			throw new EventException(ResultCode.FAILED_CANT_FIND_EVENTID);
		}

		if (!canApplyDate(eventManage, eventApplicationLog)) {
			throw new EventException(ResultCode.FAILED_NO_APPLY_DATE);
		}

		if (!canApplyTarget(eventManage, eventApplicationRequest)) {
			throw new EventException(ResultCode.FAILED_NOT_TARGET);
		}

		if (!canApplyQuiz(eventManage, eventApplicationRequest)) {
			throw new EventException(ResultCode.FAILED_NO_CORRECT_ANSWER);
		}

		if (!canApplyOverLap(eventManage, eventApplicationRequest, eventApplicationLog)) {
			throw new EventException(ResultCode.FAILED_OVERLAP_VALIDATE);
		}

	}

	public Boolean canApplyTotalLimit(EventManage eventManage) {
		Boolean result = true;
		String eventId = eventManage.getEventId();

		Integer limitApplication = eventManage.getDefaultInfo().getLimitApplication();
		Integer limitClnn = eventManage.getDefaultInfo().getLimitClnn();

		List<EventApplication> eventApplications = eventApplicationRepository.findAllByEventId(eventId);
		if (eventApplications.size() != 0) {
			if (limitApplication != 0) {
				Integer count = 0;
				for (EventApplication eventApplication : eventApplications) {
					count += eventApplication.getApplicationCount();
				}
				result = limitApplication > count ? result : false;
			}
			if (limitClnn != 0) {
				result = limitClnn > eventApplications.size() ? result : false;
			}
		}

		return result;
	}

	public EventApplicationLog setApplicationLog(EventManage eventManage,
			EventApplicationRequest eventApplicationRequest) {
		EventApplicationLog eventApplicationLog = new EventApplicationLog(eventApplicationRequest);
		if (eventManage.getProperties().contains(PropertyCode.OVERLAP)) {
			eventApplicationLog.setOrder(getOverLapOrder(eventApplicationRequest));
		}

		if (eventManage.getProperties().contains(PropertyCode.REWARD)) {
			eventApplicationLog.setRewardName(getReward(eventManage, eventApplicationRequest));
		}
		return eventApplicationLog;
	}

	public EventApplication setEventApplication(EventApplicationLog eventApplicationLog,
			EventApplicationRequest eventApplicationRequest) {

		String eventId = eventApplicationRequest.getEventId();
		String clnn = eventApplicationRequest.getClnn();

		EventApplication eventApplication = eventApplicationRepository.findOneByEventIdAndClnn(eventId, clnn);
		if (eventApplication == null) {
			eventApplication = new EventApplication();
			eventApplication.setClnn(clnn);
			eventApplication.setEventId(eventId);
		}
		eventApplication.addApplicationLogs(eventApplicationLog);
		return eventApplication;
	}

	public EventApplicationResponse getEventApplicationResponse(EventApplication eventApplication) {
		EventApplicationResponse eventApplicationResponse = new EventApplicationResponse();
		EventManage eventManage = eventManageRepository.findOneByEventId(eventApplication.getEventId());

		eventApplicationResponse.setClnn(eventApplication.getClnn());
		eventApplicationResponse.setEventInfo(new EventInfo(eventManage));
		eventApplicationResponse.setEventApplicationLog(eventApplication.getLastApplicationLog());
		eventApplicationResponse
				.setResponseInfo(setResponseInfo(eventManage, eventApplication.getLastApplicationLog()));

		return eventApplicationResponse;
	}

	public ResponseInfo setResponseInfo(EventManage eventManage, EventApplicationLog eventApplicationLog) {
		ResponseInfo responseInfo = new ResponseInfo();

		if (eventManage.getProperties().contains(PropertyCode.REWARD)) {
			String rewardName = eventApplicationLog.getRewardName();
			if (!StringUtils.isEmpty(rewardName)) {
				RewardInfo rewardInfo = eventManage.getReward().getInfoByRewardName(rewardName);
				responseInfo.setResponseMessage(rewardInfo.getMessage());
				responseInfo.setInfoes(rewardInfo.getInfoes());
			}
		} else {
			Response response = eventManage.getResponse();
			responseInfo.setResponseMessage(response.getSuccessMessage());
			responseInfo.setInfoes(response.getInfoes());
		}

		return responseInfo;
	}

	public Boolean canApplyDate(EventManage eventManage, EventApplicationLog eventApplicationLog) {

		Boolean result = true;
		LocalDateTime startDate = eventManage.getDefaultInfo().getStartDate();
		LocalDateTime endDate = eventManage.getDefaultInfo().getEndDate();
		LocalDateTime ApplyDate = eventApplicationLog.getApplyDate();

		if (startDate.isAfter(ApplyDate) || endDate.isBefore(ApplyDate)) {
			result = false;
		}

		return result;
	}

	public Boolean canApplyTarget(EventManage eventManage, EventApplicationRequest eventApplicationRequest) {
		Boolean result = true;
		if (eventManage.getProperties().contains(PropertyCode.TARGET)) {
			String targetName = eventManage.getTarget().getTargetName();
			String nonTargetName = eventManage.getTarget().getNonTargetName();

			EventTarget target = eventTargetRepository.findOneByName(targetName);
			if (target != null) {
				List<String> targetClnns = target.getClnns();
				if (targetClnns.contains(eventApplicationRequest.getClnn())) {
				} else {
					result = false;
				}
			}

			EventTarget nonTarget = eventTargetRepository.findOneByName(nonTargetName);
			if (nonTarget != null) {
				List<String> nonTargetClnns = nonTarget.getClnns();
				if (nonTargetClnns.contains(eventApplicationRequest.getClnn())) {
					result = false;
				}
			}

			List<String> channels = eventManage.getTarget().getChannels();
			if (CollectionUtils.isEmpty(channels) || channels.contains(eventApplicationRequest.getChannel())) {
			} else {
				result = false;
			}
		}
		return result;
	}

	public Boolean canApplyQuiz(EventManage eventManage, EventApplicationRequest eventApplicationRequest) {
		Boolean isCorrect = false;
		if (eventManage.getProperties().contains(PropertyCode.QUIZ)) {
			List<String> comments = eventApplicationRequest.getComments();
			List<String> answers = eventManage.getQuiz().getAnswers();

			if (eventManage.getQuiz().getChecksOneAnswer()) {
				for (String answer : answers) {
					isCorrect = comments.contains(answer) ? true : isCorrect;
				}
			} else {
				if (comments.equals(answers)) {
					isCorrect = true;
				}
			}
		} else {
			isCorrect = true;
		}
		return isCorrect;
	}

	public Boolean canApplyOverLap(EventManage eventManage, EventApplicationRequest eventApplicationRequest,
			EventApplicationLog eventApplicationLog) {
		Boolean canApply = true;

		if (eventManage.getProperties().contains(PropertyCode.OVERLAP)) {

			OverLap overLap = eventManage.getOverLap();
			String clnn = eventApplicationRequest.getClnn();
			String eventId = eventApplicationRequest.getEventId();

			EventApplication findEventApplication = eventApplicationRepository.findOneByEventIdAndClnn(eventId, clnn);

			if (findEventApplication != null) {
				LocalDateTime lastApplyDate = findEventApplication.getLastApplyDate();
				if (overLap.getLimit() != 0) {
					canApply = overLap.getLimit() > findEventApplication.getApplicationCount() ? canApply : false;
				}
				if (canApply && !eventManage.getOverLap().getType().isAllTime()) {
					canApply = overLap.getIsStartPastDate()
							? canNoIncludeOverLap(overLap, eventApplicationLog, lastApplyDate)
							: canIncludeOverLap(overLap, eventApplicationLog, lastApplyDate);
				}
			}
		} else {
			String clnn = eventApplicationRequest.getClnn();
			String eventId = eventApplicationRequest.getEventId();
			EventApplication findEventApplication = eventApplicationRepository.findOneByEventIdAndClnn(eventId, clnn);
			if (findEventApplication != null) {
				canApply = false;
			}
		}
		return canApply;
	}

	public Boolean canNoIncludeOverLap(OverLap overLap, EventApplicationLog eventApplicationLog,
			LocalDateTime lastApplyDate) {
		OverLapCode overLapType = overLap.getType();
		LocalDateTime applyDate = eventApplicationLog.getApplyDate();
		Boolean result = true;

		if (overLap.getMaxInterval() != 0) {
			result = result
					? canApply_NoIncludeOverLap_MaxInterval(overLapType, applyDate, lastApplyDate,
							overLap.getMaxInterval())
					: result;
		}
		if (overLap.getMinInterval() != 0) {
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

		if (overLap.getMaxInterval() != 0) {
			result = result
					? canApply_IncludeOverLap_MaxInterval(overLapType, applyDate, lastApplyDate,
							overLap.getMaxInterval())
					: result;
		}
		if (overLap.getMinInterval() != 0) {
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
			return true;
		} else if (overLapType.isHour() && applyDate.isBefore(lastApplyDate.plusHours(interval))) {
			return true;
		} else if (overLapType.isDay() && applyDate.isBefore(lastApplyDate.plusDays(interval))) {
			return true;
		} else if (overLapType.isMonth() && applyDate.isBefore(lastApplyDate.plusMonths(interval))) {
			return true;
		} else if (overLapType.isYear() && applyDate.isBefore(lastApplyDate.plusYears(interval))) {
			return true;
		}
		return false;
	}

	public Boolean canApply_NoIncludeOverLap_MinInterval(OverLapCode overLapType, LocalDateTime applyDate,
			LocalDateTime lastApplyDate, Integer interval) {
		if (overLapType.isMinute() && applyDate.isAfter(lastApplyDate.plusMinutes(interval))) {
			return true;
		} else if (overLapType.isHour() && applyDate.isAfter(lastApplyDate.plusHours(interval))) {
			return true;
		} else if (overLapType.isDay() && applyDate.isAfter(lastApplyDate.plusDays(interval))) {
			return true;
		} else if (overLapType.isMonth() && applyDate.isAfter(lastApplyDate.plusMonths(interval))) {
			return true;
		} else if (overLapType.isYear() && applyDate.isAfter(lastApplyDate.plusYears(interval))) {
			return true;
		}
		return false;
	}

	public Boolean canApply_IncludeOverLap_MaxInterval(OverLapCode overLapType, LocalDateTime applyDate,
			LocalDateTime lastApplyDate, Integer interval) {
		if (overLapType.isMinute()
				&& ChronoUnit.MINUTES.between(getWithMinute(lastApplyDate), getWithMinute(applyDate)) <= interval) {
			return true;
		} else if (overLapType.isHour()
				&& ChronoUnit.HOURS.between(getWithHour(lastApplyDate), getWithHour(applyDate)) <= interval) {
			return true;
		} else if (overLapType.isDay()
				&& ChronoUnit.DAYS.between(getWithDay(lastApplyDate), getWithDay(applyDate)) <= interval) {
			return true;
		} else if (overLapType.isMonth()
				&& ChronoUnit.MONTHS.between(getWithMonth(lastApplyDate), getWithMonth(applyDate)) <= interval) {
			return true;
		} else if (overLapType.isYear()
				&& ChronoUnit.YEARS.between(getWithYear(lastApplyDate), getWithYear(applyDate)) <= interval) {
			return true;
		}
		return false;
	}

	public Boolean canApply_IncludeOverLap_MinInterval(OverLapCode overLapType, LocalDateTime applyDate,
			LocalDateTime lastApplyDate, Integer interval) {
		if (overLapType.isMinute()
				&& ChronoUnit.MINUTES.between(getWithMinute(lastApplyDate), getWithMinute(applyDate)) >= interval) {
			return true;
		} else if (overLapType.isHour()
				&& ChronoUnit.HOURS.between(getWithHour(lastApplyDate), getWithHour(applyDate)) >= interval) {
			return true;
		} else if (overLapType.isDay()
				&& ChronoUnit.DAYS.between(getWithDay(lastApplyDate), getWithDay(applyDate)) >= interval) {
			return true;
		} else if (overLapType.isMonth()
				&& ChronoUnit.MONTHS.between(getWithMonth(lastApplyDate), getWithMonth(applyDate)) >= interval) {
			return true;
		} else if (overLapType.isYear()
				&& ChronoUnit.YEARS.between(getWithYear(lastApplyDate), getWithYear(applyDate)) >= interval) {
			return true;
		}
		return false;
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

	public Integer getOverLapOrder(EventApplicationRequest eventApplicationRequest) {
		Integer result = 1;

		String clnn = eventApplicationRequest.getClnn();
		String eventId = eventApplicationRequest.getEventId();

		EventApplication findEventApplication = eventApplicationRepository.findOneByEventIdAndClnn(eventId, clnn);
		if (findEventApplication != null) {
			result = findEventApplication.getApplicationCount() + 1;
		}

		return result;
	}

	public Boolean canApplyReward(EventManage eventManage, EventApplicationRequest eventApplicationRequest) {
		Boolean result = true;
		if (eventManage.getProperties().contains(PropertyCode.OVERLAP)) {
			Reward reward = eventManage.getReward();
			String eventId = eventApplicationRequest.getEventId();
			List<EventApplication> findAllEventApplications = new ArrayList<>();
			findAllEventApplications = eventApplicationRepository.findAllByEventId(eventId);
			if (canRewardLimit(reward, findAllEventApplications)) {
			} else {
				result = false;
			}
		}
		return result;
	}

	public String getReward(EventManage eventManage, EventApplicationRequest eventApplicationRequest) {
		String result = "";
		Reward reward = eventManage.getReward();
		String eventId = eventApplicationRequest.getEventId();

		List<EventApplication> findAllEventApplications = new ArrayList<>();
		findAllEventApplications = eventApplicationRepository.findAllByEventId(eventId);
		result = getRewardName(reward, findAllEventApplications);

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
		// Double randProbability = rand.nextInt(13) % sumProbability;
		Double randProbability = rand.nextDouble() * sumProbability;
		String rewardName = "";
		// log.warn("randProbability : {}", randProbability);
		for (String key : canApplyReward) {
			randProbability -= manageRewardProbability.get(key);
			if (randProbability < 0) {
				rewardName = key;
				break;
			}
		}

		return rewardName;

	}

	public List<String> getCanApplyReward(Map<String, Integer> manageRewardLimit,
			Map<String, Integer> appliedRewardLimit) {
		List<String> rewards = new ArrayList<>();
		for (String key : manageRewardLimit.keySet()) {
			if (manageRewardLimit.get(key) == 0) {
				rewards.add(key);
			} else if (manageRewardLimit.get(key) > appliedRewardLimit.get(key)) {
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
			if (manageRewardLimit.get(rewardName) == 0) {
				result = rewardName;
				break;
			} else if (manageRewardLimit.get(rewardName) > appliedRewardLimit.get(rewardName)) {
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
			if (manageRewardLimit.get(rewardName) == 0) {
				result = true;
				break;
			} else if (manageRewardLimit.get(rewardName) > appliedRewardLimit.get(rewardName)) {
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

	public EventManage findEventManageByEventId(EventApplicationRequest eventApplicationRequest) {
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
