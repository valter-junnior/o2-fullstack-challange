package com.o2.agent.dtos;

import com.o2.agent.enums.ActionEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
@Getter
public class ActionResponseDTO {
    private ActionEnum action;
}
