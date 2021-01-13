package com.shinhancard.chatbot.controller;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.shinhancard.chatbot.config.EventException;
import com.shinhancard.chatbot.domain.ResultCode;
import com.shinhancard.chatbot.domain.ResultCodeMessage;

@RestControllerAdvice(basePackages = "com.shinhancard.chatbot.controller")
public class GlobalControllerExceptionHandler implements ResponseBodyAdvice<Object> {

	@ExceptionHandler(EventException.class)
	protected ResultCodeMessage eventException(EventException e) {
		return new ResultCodeMessage(e.getResultCode());
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ResultCodeMessage defaultException() {
		return new ResultCodeMessage(ResultCode.FAILED_DEFAULT_INPUT);
	}
	

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return true;
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
			ServerHttpResponse response) {
		if (body instanceof ResultCodeMessage) {
			return body;
		} else {
			return new ResultCodeMessage(body, ResultCode.SUCCESS);
		}
	}
}
