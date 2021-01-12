package com.shinhancard.chatbot.repository;

import com.shinhancard.chatbot.entity.ApplyManage;

public interface CustomApplyManageRepository {
	ApplyManage incDefaultByEventId(String eventId);
	ApplyManage decDefaultByEventId(String eventId);
}
