package com.over.moodymq.service;

/**
 * Created by jsflz on 2022/7/7 9:54
 */
public interface ReceiveService {
    void receive();

    void directReceive(String message);

//    void fanoutReceive(String message);
}
