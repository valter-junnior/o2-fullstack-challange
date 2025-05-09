package com.o2.agent.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ChatBotResponseDTO {
    private String id;
    private String object;
    private long created;
    private List<Choice> choices;

    @Data
    @AllArgsConstructor
    public static class Choice {
        private Message message;
        private String finishReason;
        private int index;

        @Data
        @AllArgsConstructor
        public static class Message {
            private String role;
            private String content;
        }
    }
}
