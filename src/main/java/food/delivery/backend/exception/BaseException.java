package food.delivery.backend.exception;

import lombok.Getter;
import org.springframework.core.NestedRuntimeException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Projecti ning har bir Custom exceptionlarni hammasida code va
 * exception Message uchun argumentlar bo'lishi mumkin. Shularnin umumlashtirish uchun ochilgan exception.
 * Bu o'zi throw qilinmaydi. Bundan extend olgan Exceptionlar throw qilinadi doim
 */
@Getter
public class BaseException extends NestedRuntimeException {
    /**
     * Sodir bo'lgan exception uchun tizimda code
     */
    private final int code;
    /**
     * Exception messages yozilgan properties da {0},{1} ... lar  bilan kelgan argumentlarni to'ldiruvchi qiymatlari
     */
    private final List<String> args;
    /**
     * Message localization qilingami yoqmi bildirib turishi uchun.
     * Agar localization qilingan bo'lsa , GlobalException handlerda qayta properties dan qidirmaslik uchun qo'shildi
     */
    private final boolean i18n;


    public BaseException(ResponseCodes responseCode) {
        super(responseCode.getMessage());
        this.args = new ArrayList<>();
        this.code = responseCode.getCode();
        this.i18n = false;
    }

    /**
     * Only use where you handle exception which is thrown by System and message localization did
     *
     * @param msg  localized message
     * @param code exception code which is thrown by System
     */
    public BaseException(String msg, Integer code) {
        super(msg);
        this.args = new ArrayList<>();
        this.code = code;
        this.i18n = true;
    }

    public BaseException(String msg, ResponseCodes responseCode, String... args) {
        super(msg);
        this.args = Arrays.stream(args).toList();
        this.code = responseCode.getCode();
        this.i18n = false;
    }


    private String getExceptionDescription(String description, List<String> args) {
        for (int i = 0; i < args.size(); i++) {
            description = description.replace("{" + i + "}", (args.get(i) == null ? "null" : args.get(i)));
        }
        return description;
    }
}