package insure.management.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
public class PrdtCreateRequest {
    @NotBlank
    private String prdtNm;

    @NotNull
    private Long minContDate;

    @NotNull
    private Long maxContDate;

    @NotNull
    private List<CvrCreateRequest> coverages = new ArrayList<>();

}
