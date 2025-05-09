package com.o2.agent.dtos;

import com.o2.agent.enums.ActionEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActionConsultExitMovementDTO extends ActionResponseDTO {
    private String startAt;
    private String endAt;

    public ActionConsultExitMovementDTO(String startAt, String endAt) {
        super(ActionEnum.CREATE_STOCK_MOVEMENT);
        this.startAt = startAt;
        this.endAt = endAt;
    }
}
