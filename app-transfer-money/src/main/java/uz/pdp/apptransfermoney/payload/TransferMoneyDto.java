package uz.pdp.apptransfermoney.payload;

import lombok.Data;

@Data
public class TransferMoneyDto {
    private String fromCard;
    private String toCard;
    private Double amount;
}
