package uz.pdp.apptransfermoney.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class CardDto {
    @NotNull
    private String number;

    @JsonFormat(pattern = "dd.MM.yyyy")
    @NotNull
    private Date expiredDate;

}
