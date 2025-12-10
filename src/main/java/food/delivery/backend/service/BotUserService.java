package food.delivery.backend.service;

import food.delivery.backend.dto.request.BotUserDTO;
import food.delivery.backend.enums.Language;
import food.delivery.backend.enums.State;
import org.telegram.telegrambots.meta.api.objects.message.Message;

/**
 * Created by Avaz Absamatov
 * Date: 12/2/2025
 */
public interface BotUserService {
    BotUserDTO getOrRegisterUser(Message message);

    void changeState(BotUserDTO botUserDTO, State state);

    Language saveLanguage(BotUserDTO botUser, Language language);
}
