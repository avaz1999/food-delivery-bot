package food.delivery.backend.enums;

/**
 * Created by Avaz Absamatov
 * Date: 12/23/2025
 */
public enum FoodStatus {
    AVAILABLE,      // Mahsulot mavjud, zakas berish mumkin (menyuda ko'rinadi)
    UNAVAILABLE,    // Mavjud emas (masalan, masalliq tugagan)
    DISCONTINUED,   // Butunlay olib tashlangan (eski mahsulot, endi yo'q)
    HIDDEN          // Admin ko'radi, lekin oddiy foydalanuvchiga ko'rinmaydi (test uchun yoki maxfiy)
}