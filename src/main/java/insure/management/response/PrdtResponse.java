package insure.management.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PrdtResponse {
    private Long prdtId;
    private String prdtNm;
    private Long minContDate;
    private Long maxContDate;

    private List<CvrResponse> cvrData = new ArrayList<>();
}
