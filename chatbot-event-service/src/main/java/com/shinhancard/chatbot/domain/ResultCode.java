package com.shinhancard.chatbot.domain;

public enum ResultCode {
	SUCCESS("00", "성공적으로 신청(저장) 되었습니다."),
	FAILED_NO_EVENTID_INPUT("01", "eventId를 입력해주세요."),
	FAILED_NO_DATE_INPUT("02", "StartDt, EndDt를 입력해주세요 입력 형식은 YYYY-MM-DDT hh:mm:ss 입니다."),
	FAILED_DATE_ORDER("03", "StartDate가 EndDate보다 느립니다."),
	FAILED_NO_OVERLAP_INPUT("04", "overLapDateType || overLapDateCount || includeDateTF를 입력해 주세요"),
	FAILED_NO_LIMIT_INPUT("05", "limitType || limitInfo를 입력해 주세요"),
	FAILED_NO_QUIZANSWER_INPUT("06", "quizAnswer를 입력해 주세요"),
	FAILED_EVENTID_OVERLAP("07", "이미 등록된 EventID입니다."),
	FAILED_RANDOMPROB_OVER_ONE("07", "RANDOM 확률의 총 합은 1을 넘을 수 없습니다."),
	FAILED_NO_CLNN_INPUT("08", "clnn(고객번호)를 입력해주세요"),
	FAILED_CANT_FIND_EVENTID("09", "등록된 EventID가 아닙니다."),
	FAILED_NO_APPLY_DATE("10", "이벤트 신청 기간이 아닙니다."),
	FAILED_ALREADY_APPLIED("11", "이미 이벤트에 신청하셨습니다."),
	FAILED_TIME_OVERLAP("12", "이벤트 신청이 불가합니다. 다음달(내일) 다시 신청해주세요"), 
	FAILED_ORDERCOUNT_OVER("13", "이벤트에 신청 가능한 건수를 초과하였습니다."),
	FAILED_NO_CORRECT_ANSWER("14", "이벤트 정답이 아닙니다."),
	FAILED_DEFAULT_INPUT("15", "입력 값이 잘못되었음"),
	FAILED_OVERLAP_VALIDATE("16", "overLap Validation Failed"),
	FAILED_GET_REWARD("17", "reward를 받을 수 없습니다.");
	
	
	private String resultCode;
	private String resultMessage;

	
	private ResultCode(String resultCode, String resultMessage) {
		this.resultCode	= resultCode;
		this.resultMessage = resultMessage;
	}
	
	public String getResultCode() {
		return resultCode;
	}

	public String getResultMessage() {
		return resultMessage;
	}

	public Boolean isSuccess() {
		if (this.equals(SUCCESS)) {
			return true;
		} else {
			return false;
		}
	}
}