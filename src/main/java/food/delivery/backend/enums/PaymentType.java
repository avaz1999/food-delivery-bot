package food.delivery.backend.enums;

import lombok.Getter;

/**
 * Created by Avaz Absamatov
 * Date: 1/13/2026
 */
@Getter
public enum PaymentType {
    CASH("Naqd", "Наличные"),
    CLICK("Click", "Клик"),
    PAY_ME("PayMe", "PayMe");

    private final String uz;
    private final String ru;

    PaymentType(String uz, String ru) {
        this.uz = uz;
        this.ru = ru;
    }

    public String getLabel(String lang) {
        return "UZ".equals(lang) ? uz : ru;
    }
}
