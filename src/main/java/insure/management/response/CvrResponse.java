package insure.management.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CvrResponse {
    private Long cvrId;

    private String cvrNm;

    private Double registerPrice;
    private Double basePrice;
}
