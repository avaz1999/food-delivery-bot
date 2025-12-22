package food.delivery.backend.exception;

import lombok.Getter;

/**
 * Use cases -> Service ga client tomonda kirib kelgan request validation jarayonidan o'ta olmasa .
 * throw qilinadigan Exception.
 * Bundan qaytan message localization qilinib kieltga qaytariladi doim
 *
 * @see ResponseCodes ResponseCodes codes between 1000 and 1999
 */

@Getter
public class BadRequestException extends BaseException {

    public BadRequestException(ResponseCodes responseCode) {
        super(responseCode);
    }

    public BadRequestException(String msg, Integer code) {
        super(msg, code);
    }

    public BadRequestException(ResponseCodes responseCode, String... args) {
        super(responseCode.getMessage(), responseCode, args);
    }
}