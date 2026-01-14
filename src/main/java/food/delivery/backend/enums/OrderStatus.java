package food.delivery.backend.enums;

import lombok.Getter;

/**
 * Created by Avaz Absamatov
 * Date: 1/13/2026
 */
@Getter
public enum OrderStatus {
    NEW("\uD83C\uDD95 Yangi", "\uD83C\uDD95 Новый"),
    PAID("\uD83D\uDCB0 To'langan", "\uD83D\uDCB0 Оплачено"),
    KITCHEN_ACCEPTED("\uD83D\uDC68\u200D\uD83C\uDF73 Oshxona qabul qilgan", "\uD83D\uDC68\u200D\uD83C\uDF73 Принято кухней"),
    COURIER_ASSIGNED("\uD83D\uDEF5 Kurer berilgan", "\uD83D\uDEF5 Назначен курьер"),
    DELIVERED("✅ Yetkazilgan", "✅ Доставлено"),
    CANCELLED("❌ Bekor qilindi", "❌ Отменено");

    private final String uz;
    private final String ru;

    OrderStatus(String uz, String ru) {
        this.uz = uz;
        this.ru = ru;
    }

    public String getLabel(String lang) {
        return "UZ".equals(lang) ? uz : ru;
    }
}
