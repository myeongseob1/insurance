package insure.management.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CvrCreateRequest {
    @NotBlank
    private String cvrNm;

    @NotNull
    private Double registerPrice;

    @NotNull
    private Double basePrice;

}
