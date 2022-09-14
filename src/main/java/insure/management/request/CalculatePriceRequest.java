package insure.management.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CalculatePriceRequest {
    @NotNull
    private Long prdtId;

    @NotNull
    private List<Long> cvrIds;

    @NotNull
    private Long contDate;
}
