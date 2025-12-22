package food.delivery.backend.exception;

import lombok.Getter;

/**
 * Created by Avaz Absamatov
 * Date: 11/13/2025
 */
@Getter
public class InternalServerException extends BaseException{
    public InternalServerException() {
        super(ResponseCodes.SYSTEM_ERROR.getMessage(), ResponseCodes.SYSTEM_ERROR);
    }

    public InternalServerException(ResponseCodes responseCodes) {
        super(responseCodes.getMessage(), responseCodes);
    }

    public InternalServerException(String msg, Integer code) {
        super(msg, code);
    }

    public InternalServerException(ResponseCodes responseCodes, String... args) {
        super(responseCodes.getMessage(), responseCodes, args);
    }
}
