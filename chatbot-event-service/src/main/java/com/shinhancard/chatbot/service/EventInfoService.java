package com.shinhancard.chatbot.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shinhancard.chatbot.controller.request.EventInfoRequest;
import com.shinhancard.chatbot.controller.response.EventInfoResponse;
import com.shinhancard.chatbot.domain.past.EventInfo;
import com.shinhancard.chatbot.domain.past.ResultCode;
import com.shinhancard.chatbot.domain.past.EventInfo.RewardType;
import com.shinhancard.chatbot.repository.EventInfoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventInfoService {
	@Autowired
	private ModelMapper modelMapper;

	private final EventInfoRepository eventInfoRepository;

	public EventInfo getEventById(String id) {
		return eventInfoRepository.findOneById(id);
	}

	public EventInfo updateEvent(String id, EventInfo event) {
		EventInfo savedEvent = eventInfoRepository.findOneById(event.getId());
		savedEvent.update(event);

		// 사실 데이터를 조회 > 수정 하게 되면 JPA에서는 '영속성 관리' 라는 것을 해주기 때문에 save를 따로 해줄필요가 없지만..
		// 그냥 명시적으로 save 추가해 주었음
		return eventInfoRepository.save(savedEvent);
	}

	public void deleteEvent(String id) {
		eventInfoRepository.deleteById(id);
	}

	public void joinEvent(String eventId) {

	}

	public EventInfoResponse registEvent(EventInfoRequest eventInfoRequest) {

		log.info("eventInfo Service Start");
//		ModelMapper modelMapper = new modelMapper();
		EventInfo eventInfo = modelMapper.map(eventInfoRequest, EventInfo.class);
		EventInfoResponse eventInfoResponse = modelMapper.map(eventInfo, EventInfoResponse.class);
		log.info("eventHistory to EventHistoryResponse Mapping success {}, {}", eventInfoRequest, eventInfo);

		// resultCode setting

		ResultCode eventInfoResultCode = ResultCode.SUCCESS;
		log.info("resultCode setting");

		// 입력값 Validation
		if (!getInputValidation(eventInfo)) {
			eventInfoResultCode = ResultCode.FAILED_DEFAULT_INPUT;
		}
		log.info("입력값 Validation");

		// validation 체크를 통과한 경우 DB에 저장
		if (eventInfoResultCode.isSuccess()) {
			eventInfoRepository.save(eventInfo);
		}
		log.info("validation 체크를 통과한 경우 DB에 저장");

		// Response의 resultCode 채움
		eventInfoResponse.setResult(eventInfoResultCode);
		log.info("Response의 resultCode 채움");

		return eventInfoResponse;
	}

	public Boolean getInputValidation(EventInfo eventInfo) {
		ResultCode resultCode = ResultCode.SUCCESS;
		// EventId가 입력되었는지?
		if (!eventInfo.getValidationEventId()) {
			resultCode = ResultCode.FAILED_NO_EVENTID_INPUT;
		}
		// Date 입력이 정상적으로 입력되었는지?
		if (!eventInfo.getValidationDateInput()) {
			resultCode = ResultCode.FAILED_NO_DATE_INPUT;
		}
		if (!eventInfo.getValidationDateOrder()) {
			resultCode = ResultCode.FAILED_DATE_ORDER;
		}
		// OverLap 입력이 정상적으로 되었는지?
		if (!eventInfo.getValidationOverLapInput()) {
			resultCode = ResultCode.FAILED_NO_OVERLAP_INPUT;
		}
		// rewardInfo 입력이 정상적으로 되었는지?
		if (eventInfo.getRewardTF()) {
			// default로 입력해야 할 값이 정상적으로 입력되었는가?
			if (!eventInfo.getValidationRewardDefaultInput()) {
				resultCode = ResultCode.FAILED_NO_LIMIT_INPUT;
			}
			log.info("prob validation");
			// RANDOMPROB일 때 확률의 값이 1 이하인가?
			if (RewardType.RANDOMPROB.equals(eventInfo.getRewardType())
					|| RewardType.QUIZ.equals(eventInfo.getRewardType())) {
				if (!eventInfo.getValidationRewardRandomProbInput()) {
					resultCode = ResultCode.FAILED_RANDOMPROB_OVER_ONE;
				}
			}
		}

		// evendId가 중복되었는지?
		if (getValidationEventIdOverLap(eventInfo)) {
			resultCode = ResultCode.FAILED_EVENTID_OVERLAP;
		}

		log.trace("resultCode", resultCode.getResultMessage());

		if (resultCode.isSuccess())
			return true;
		else
			return false;

	}

	public Boolean getValidationEventIdOverLap(EventInfo eventInfo) {
		EventInfo findOneByEventId = eventInfoRepository.findOneByEventId(eventInfo.getEventId());
		if (findOneByEventId == null) {
			return false;
		} else {
			return true;
		}
	}

}
