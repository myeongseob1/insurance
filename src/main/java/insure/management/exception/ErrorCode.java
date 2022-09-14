package insure.management.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
@AllArgsConstructor
@Getter
public enum ErrorCode {
    // Contract
    REQUIRED_PARAMETER_ERROR(HttpStatus.BAD_REQUEST,410,"REQUIRED PARAMETER ERROR"),
    WRONG_CONTRACT(HttpStatus.BAD_REQUEST,411,"잘못된 계약입니다."),
    EXPIRED_CONTRACT(HttpStatus.NOT_MODIFIED,412,"만료된 계약입니다."),
    WRONG_CONTRACT_COVERAGE(HttpStatus.BAD_REQUEST,413,"계약에 존재하지 않는 담보입니다"),
    WRONG_CONTRACT_DATE(HttpStatus.BAD_REQUEST,414,"계약기간이 최소 계약기간보다 낮거나 최대 계약기간보다 높습니다"),

    // Product
    WRONG_PRODUCT_COVERAGE(HttpStatus.BAD_REQUEST,415,"해당 상품에 포함되지 않은 담보입니다"),
    WRONG_PRODUCT(HttpStatus.BAD_REQUEST,416,"잘못된 상품입니다."),

    // Coverage
    WRONG_COVERAGE(HttpStatus.BAD_REQUEST,417,"잘못된 담보입니다."),
    ;

    private HttpStatus status;
    private int errorCode;
    private String message;
}
