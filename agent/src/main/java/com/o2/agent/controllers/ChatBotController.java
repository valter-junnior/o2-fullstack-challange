package com.o2.agent.controllers;

import com.o2.agent.dtos.MessageDTO;
import com.o2.agent.services.ChatBotService;
import com.o2.agent.services.MessageProcessorService;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@AllArgsConstructor
public class ChatBotController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatBotService chatBotService;
    private final MessageProcessorService messageProcessorService;

    @MessageMapping("/sendMessage")
    public void sendMessage(MessageDTO message) {
        String response = chatBotService.sendMessage(message.getContent());

        MessageDTO messageDTO = messageProcessorService.process(response);

        messagingTemplate.convertAndSend("/topic/messages", messageDTO);
    }
}
