package com.shinhancard.chatbot.domain;

import com.shinhancard.chatbot.config.EventException;

import lombok.Data;

@Data
public class ResultCodeMessage {

	// 결과메세지
	private ResultCode resultCode;
	private String resultMessage;
	private Object result;
	
	public ResultCodeMessage(Object result, ResultCode resultCode) {
		this.resultCode = ResultCode.SUCCESS;
		this.resultMessage = this.resultCode.getResultMessage();
		this.result = result;
	}
	
	public ResultCodeMessage(ResultCode resultCode) {
		this.resultCode = resultCode;
		this.resultMessage = resultCode.getResultMessage();
	}
	

	
	public ResultCodeMessage() {
		this.resultCode = ResultCode.SUCCESS;
		this.resultMessage = resultCode.getResultMessage();
	}
	
	public ResultCodeMessage(EventException e) {
		
	}


	public void setResultCodeAndMessage(ResultCode resultCode) {
		this.resultCode = resultCode;
		this.resultMessage = resultCode.getResultMessage();
	}
}
