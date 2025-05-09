package com.o2.agent.controllers;

import com.o2.agent.services.ChatBotService;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@AllArgsConstructor
public class ChatBotController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatBotService chatBotService;

    @MessageMapping("/sendMessage")
    public void sendMessage(String message) {
        System.out.println("Received message: " + message);
        String response = chatBotService.sendMessage(message);
        System.out.println("Response: " + response);
        messagingTemplate.convertAndSend("/topic/messages", response);
    }
}
