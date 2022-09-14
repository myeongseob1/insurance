package insure.management.request;

import insure.management.domain.ContractStatus;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ContStatusUpdateRequest {
    @NotNull
    private Long contId;

    @NotNull
    private ContractStatus contStatus;
}
