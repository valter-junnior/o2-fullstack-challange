package com.o2.agent.dtos;
import com.o2.agent.enums.ActionEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActionCreateMovementDTO extends ActionResponseDTO {
    private String productId;
    private int quantity;
    private String type;
    private String date;

    public ActionCreateMovementDTO(String productId, int quantity, String type, String date) {
        super(ActionEnum.CREATE_STOCK_MOVEMENT);
        this.productId = productId;
        this.quantity = quantity;
        this.type = type;
        this.date = date;
    }
}
