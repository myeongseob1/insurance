package insure.management.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ContCoverageUpdateRequest {
    @NotNull
    private Long contId;

    @NotNull
    private Long cvrId;
}
