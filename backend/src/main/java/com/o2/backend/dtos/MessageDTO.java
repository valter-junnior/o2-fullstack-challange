package com.o2.backend.dtos;

import lombok.Data;

@Data
public class MessageDTO {
    private String id;
    private String sender;
    private String content;
}
