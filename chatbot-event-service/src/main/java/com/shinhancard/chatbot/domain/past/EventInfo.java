package com.shinhancard.chatbot.domain.past;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import lombok.Data;

@Data
public class EventInfo {

	@Id
	private String id;

	private String eventId;
	private String displayName;

	private LocalDateTime startDt;
	private LocalDateTime endDt;

	// 중복 신청 관련 field
	private Boolean overLapTF;
	private OverLapType overLapDateType;
	private Integer overLapDateCount;
	private Boolean includeDateTF;

	// limit 관련 field
	private Boolean rewardTF;
	private RewardType rewardType;
	private LinkedHashMap<String, Double> rewardInfo = new LinkedHashMap<>();

//	// quiz 신청 관련 field
//	private Boolean quizTF;
	private List<String> quizAnswer;

	// 결과 field
	private HashMap<String, HashMap<String, String>> resultInfo = new HashMap<>();

	// 대상자 선정
	private List<String> targetClnn;
	private List<String> nonTargetClnn;

	public void update(EventInfo newEvent) {
		this.eventId = newEvent.getEventId();
		this.displayName = newEvent.getDisplayName();

	}

	public EventInfo() {
		targetClnn = new ArrayList<String>();
		nonTargetClnn = new ArrayList<String>();
		quizAnswer = new ArrayList<String>();
	}

	// TODO :: 최소한의 필드만 넣어놨으니 더 추가할 것
	public Boolean getResultInfoContainsKey(String rewardName) {
		if (this.getResultInfo().containsKey(rewardName)) {
			return true;
		}
		return false;
	}

	public HashMap<String, String> getResultInfoValue(String rewardName) {
		return this.getResultInfo().get(rewardName);
	}

	public Boolean getResultInfoContainsResponseMessage(String rewardName) {
		if (this.getResultInfo().get(rewardName).containsKey("responseMessage")) {
			return true;
		}
		return false;
	}

	public String getResultInfoResponseMessage(String rewardName) {
		return this.getResultInfo().get(rewardName).get("responseMessage");
	}

	public Boolean isNotValid() {

		// TODO :: 신청가능 기간 및 참여 가능한 상태인지 체크하는 로직
		// this.startDt,
		/*
		 * if () { return true; }
		 */
		return false;
	}

	public Boolean needOverLapLogics() {

		if (!this.overLapDateType.equals(OverLapType.ALLTIME)) {
			return true;
		}
		return false;

	}

	public Boolean getValidationRewardDefaultInput() {
		if (this.rewardType == null || CollectionUtils.isEmpty(this.rewardInfo)) {
			return false;
		}
		return true;
	}

	public Boolean getValidationRewardRandomProbInput() {

		// randomprob의 값이 1을 넘었는지?
		if (this.getTotalProb() > 1) {
			return false;
		}
		return true;
	}

	public Boolean getValidationOverLapInput() {
		if (this.overLapTF) {
			if (this.overLapDateType == null || this.overLapDateCount == null || this.includeDateTF == null) {
				return false;
			}
		}
		return true;
	}

	public Boolean getValidationQuizAnswerInput() {
		if (CollectionUtils.isEmpty(this.quizAnswer)) {
			return false;
		}
		return true;
	}

	public Boolean getValidationEventId() {
		if (StringUtils.isEmpty(this.eventId)) {
			return false;
		}
		return true;
	}

	public Boolean getValidationDateInput() {
		if (this.getStartDt() == null || this.getEndDt() == null) {
			return false;
		}
		return true;
	}

	public Boolean getValidationDateOrder() {
		if (this.getStartDt().isAfter(this.getEndDt())) {
			return false;
		}
		return true;
	}

	public Double getTotalProb() {
		Double totalProb = 0.0;
		Set<String> keys = this.getRewardInfo().keySet();

		for (String key : keys) {
			// System.out.println(key);
			totalProb += this.getRewardInfo().get(key);
		}
		return totalProb;
	}

	public Integer getTotalCount() {
		int result = 0;
		Set<String> keys = this.rewardInfo.keySet();

		for (String key : keys) {
			result += rewardInfo.get(key);
		}
		return result;
	}

	public Boolean getEventDateValidate(LocalDateTime date) {
		if (this.startDt.isAfter(date) || this.endDt.isBefore(date)) {
			return false;
		}
		return true;
	}

	public String getReward(List<EventHistory> findEventId) {
		String result = "default";

		if (this.rewardType.isRewardRandomProb()) {
			result = getRewardRandomProb();
		} else if (this.rewardType.isRewardRandom()) {
			result = getRewardRandom(findEventId);
		} else if (this.rewardType.isRewardFCFS()) {
			result = getRewardFCFS(findEventId);
		} else if (this.rewardType.isRewardQuiz()) {
			result = getRewardRandomProb();
		} else if (this.rewardType.isRewardQuizLimit()) {
			result = getRewardRandom(findEventId);
		}

		return result;
	}

	public String getRewardRandomProb() {
		String result = "";
		LinkedHashMap<String, Double> applicableWinner = setRandomProbWinner();
		Double totalWinnerCount = getLastValue(applicableWinner);

		Random rand = new Random();
		Double criteria = rand.nextDouble() % totalWinnerCount;
		Set<String> keys = applicableWinner.keySet();

		for (String key : keys) {
			if (criteria < applicableWinner.get(key)) {
				result = key;
				break;
			}
		}
		return result;
	}

	public String getRewardRandom(List<EventHistory> findEventId) {
		String result = "";
		LinkedHashMap<String, Double> applicableWinner = canApplyWinner(findEventId);

		Double totalWinnerCount = getLastValue(applicableWinner);

		Random rand = new Random();
		int criteria = rand.nextInt(totalWinnerCount.intValue());

		Set<String> keys = applicableWinner.keySet();
		for (String key : keys) {
			if (criteria < applicableWinner.get(key).intValue()) {
				result = key;
				break;
			}
		}
		return result;
	}

	public String getRewardFCFS(List<EventHistory> findEventId) {
		String result = "";
		LinkedHashMap<String, Double> applicableWinner = canApplyWinner(findEventId);

		Set<String> keys = applicableWinner.keySet();
		for (String key : keys) {
			if (applicableWinner.get(key) != 0) {
				result = key;
				break;
			}
		}
		return result;
	}

	public LinkedHashMap<String, Double> canApplyWinner(List<EventHistory> findEventId) {
		LinkedHashMap<String, Double> result = new LinkedHashMap<String, Double>();
		Double totalprob = 0.0;
		Set<String> keys = this.rewardInfo.keySet();

		for (String key : keys) {
			totalprob += this.rewardInfo.get(key);
			totalprob -= getRewardCount(findEventId, key);
			result.put(key, totalprob);
		}
		return result;
	}

	public Double getRewardCount(List<EventHistory> findEventId, String key) {
		Double result = 0.0;
		for (int i = 0; i < findEventId.size(); i++) {
			for (int j = 0; j < findEventId.get(i).getLogs().size(); j++) {
				if (findEventId.get(i).getLogs().get(j).getRewardName().equals(key)) {
					result += 1;
				}
			}
		}
		return result;
	}

	public Double getLastValue(LinkedHashMap<String, Double> inputMap) {
		Double result = 0.0;
		Set<String> keys = inputMap.keySet();
		for (String key : keys) {
			result = inputMap.get(key);
		}
		return result;
	}

	public LinkedHashMap<String, Double> setRandomProbWinner() {
		LinkedHashMap<String, Double> result = new LinkedHashMap<String, Double>();
		Double totalprob = 0.0;
		Set<String> keys = this.rewardInfo.keySet();

		for (String key : keys) {
			totalprob += this.rewardInfo.get(key);
			result.put(key, totalprob);
		}
		return result;
	}

	public enum RewardType {
		FCFS, RANDOM, RANDOMPROB, QUIZ, QUIZ_LIMIT; // 랜덤 확률

		public boolean isRewardRandom() {
			if (this.equals(RewardType.RANDOM)) {
				return true;
			}

			return false;
		}

		public boolean isRewardFCFS() {
			if (this.equals(RewardType.FCFS)) {
				return true;
			}

			return false;
		}

		public boolean isRewardRandomProb() {
			if (this.equals(RewardType.RANDOMPROB)) {
				return true;
			}
			return false;
		}

		public boolean isRewardQuiz() {
			if (this.equals(RewardType.QUIZ)) {
				return true;
			}
			return false;
		}

		public boolean isRewardQuizLimit() {
			if (this.equals(RewardType.QUIZ_LIMIT)) {
				return true;
			}
			return false;
		}

	}

	public enum OverLapType {
		ALLTIME, MINUTE, HOUR, DAY, MONTH, YEAR;

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

}
