package com.app.botblend.service;

public interface MessagePersistenceService<T> {
    void persist(Long id, T message);
}
