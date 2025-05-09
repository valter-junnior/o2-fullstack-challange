package com.o2.agent.dtos;

import com.o2.agent.enums.ActionEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActionInvalidParameterDTO extends ActionResponseDTO {
    private String message;

    public ActionInvalidParameterDTO(String message) {
        super(ActionEnum.CREATE_STOCK_MOVEMENT);
        this.message = message;
    }
}
