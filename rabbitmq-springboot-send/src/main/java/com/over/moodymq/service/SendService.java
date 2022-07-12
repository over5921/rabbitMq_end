package com.over.moodymq.service;

/**
 * Created by jsflz on 2022/7/7 9:39
 */
public interface SendService {
    void sendMessage(String message);

    void sendFanoutMessage(String message);

    void sendTopicMessage(String message);

    void sendConfimDirect(String confimDirict);

    void sendConfimDirect1(String confimDirict1);

    void sendDDL();

    void sendDDLYh();
}
