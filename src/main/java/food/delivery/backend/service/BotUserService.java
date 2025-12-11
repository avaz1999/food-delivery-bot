package food.delivery.backend.service;

import food.delivery.backend.entity.BotUser;
import food.delivery.backend.enums.Language;
import food.delivery.backend.enums.State;
import org.telegram.telegrambots.meta.api.objects.message.Message;

/**
 * Created by Avaz Absamatov
 * Date: 12/2/2025
 */
public interface BotUserService {
    BotUser getOrRegisterUser(Message message);

    void changeState(BotUser botUserDTO, State state);

    Language saveLanguage(BotUser botUser, Language language);

    BotUser savePhoneNumber(BotUser botUser, String phoneNumber);
}
