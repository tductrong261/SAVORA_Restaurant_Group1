package vn.edu.fpt.savora.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    INVALID_CREDENTIALS("AUTH_001", "Invalid email or password.", HttpStatus.UNAUTHORIZED),
    ACCOUNT_LOCKED("AUTH_002", "Your account has been locked.", HttpStatus.FORBIDDEN),
    ACCOUNT_PENDING("AUTH_003", "Your account is not active.", HttpStatus.FORBIDDEN),
    EMAIL_ALREADY_EXISTS("AUTH_004", "This email is already registered.", HttpStatus.CONFLICT),
    PASSWORD_MISMATCH("AUTH_005", "Password confirmation does not match.", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND("USER_404", "User account not found.", HttpStatus.NOT_FOUND),
    INVALID_CURRENT_PASSWORD("USER_001", "Current password is incorrect.", HttpStatus.BAD_REQUEST),
    TABLE_NOT_FOUND("TABLE_404", "Selected dining table was not found.", HttpStatus.NOT_FOUND),
    TABLE_CAPACITY_EXCEEDED("TABLE_001", "Party size exceeds the selected table capacity.", HttpStatus.BAD_REQUEST),
    TABLE_TIME_CONFLICT("RES_001", "This table is already reserved during the selected 120-minute time slot.", HttpStatus.CONFLICT),
    RESERVATION_NOT_FOUND("RES_404", "Reservation not found.", HttpStatus.NOT_FOUND),
    VOUCHER_INVALID("VOUCHER_001", "Voucher code is invalid or expired.", HttpStatus.BAD_REQUEST),
    VOUCHER_MIN_ORDER("VOUCHER_002", "Order subtotal does not meet the minimum amount for this voucher.", HttpStatus.BAD_REQUEST),
    VALIDATION_ERROR("VALIDATION_001", "Invalid input data.", HttpStatus.BAD_REQUEST),
    BAD_REQUEST("COMMON_400", "Bad request.", HttpStatus.BAD_REQUEST),
    INTERNAL_ERROR("COMMON_500", "Internal server error.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatus status;

    ErrorCode(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}
