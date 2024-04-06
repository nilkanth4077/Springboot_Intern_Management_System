//package com.rh4.controllers;
//
//import com.rh4.models.*;
//import org.springframework.messaging.handler.annotation.DestinationVariable;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Controller;
//
//@Controller
//public class WebSocketController {
//
//    private final SimpMessagingTemplate messagingTemplate;
//
//    public WebSocketController(SimpMessagingTemplate messagingTemplate) {
//        this.messagingTemplate = messagingTemplate;
//    }
//
//    @MessageMapping("/message/{userId}/projectdef")
//    public void sendMessage(@DestinationVariable String userId, Message message) {
//        String topic = userId.startsWith("001") ? "/topic/projectdef" : "/topic/reply";
//        System.out.println(topic + message.getContent());
//        messagingTemplate.convertAndSendToUser(userId, topic, message);
//    }
//}
