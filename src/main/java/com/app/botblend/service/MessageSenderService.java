package com.app.botblend.service;

import com.app.botblend.model.Message;

public interface MessageSenderService {
    boolean sendMessage(Message message);
}
