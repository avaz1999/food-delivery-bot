package food.delivery.backend.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Created by Avaz Absamatov
 * Date: 12/21/2025
 */
@Getter
@RequiredArgsConstructor
public enum ResponseCodes {
    SUCCESS(0,"success"),
    INVALID_DATA_TYPE(-10, "invalid.data.type"),
    NOT_FOUND(-11, "not.found"),
    METHOD_NOT_ALLOWED(-12, "method.not.allowed"),
    SYSTEM_ERROR(-13, "system.error"), BAD_REQUEST(-14, "bad.request"),
    CONSTRAINT_VIOLATION(-15, "constrain.violation"),
    ERROR_WHILE_SAVING(-16, "error.while.saving"),
    DATABASE_ERROR(-17, "database.error"),

    //Category
    CATEGORY_ALREADY_EXISTS(-30, "category.already.exists"),
    CATEGORY_PARENT_NOT_FOUND(-31, "category.parent.not.found");
    private final Integer code; // response code
    private final String message;
}
