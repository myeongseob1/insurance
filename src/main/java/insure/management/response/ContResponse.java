package insure.management.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import insure.management.domain.ContractStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContResponse {
    private Long contId;
    private ContractStatus contractStatus;
    private LocalDateTime contBgDate;
    private LocalDateTime contEndDate;
    private Long contDate;
    private Double contPrice;
    private PrdtResponse product;
}
