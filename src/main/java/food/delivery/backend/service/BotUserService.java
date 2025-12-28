package food.delivery.backend.service;

import food.delivery.backend.entity.BotUser;
import food.delivery.backend.enums.Language;
import food.delivery.backend.enums.Role;
import food.delivery.backend.enums.State;
import food.delivery.backend.enums.Status;
import food.delivery.backend.repository.BotUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.message.Message;

/**
 * Created by Avaz Absamatov
 * Date: 12/2/2025
 */
@Service
@RequiredArgsConstructor
public class BotUserService {
    private final BotUserRepository repository;

    public BotUser getOrRegisterUser(Message message) {
        BotUser botUser = repository.findByChatId(message.getChatId());
        if (botUser == null) return buildBotUser(message);
        return botUser;
    }

    public void changeState(BotUser botUser, String state) {
        botUser.setState(state);
        repository.save(botUser);
    }

    public Language saveLanguage(BotUser botUser, Language language) {
        botUser.setLanguage(language);
        return repository.save(botUser).getLanguage();
    }

    public BotUser savePhoneNumber(BotUser botUser, String phoneNumber) {
        botUser.setPhone(phoneNumber);
        botUser.setState(State.STATE_SETTING_MENU.name());
        return repository.save(botUser);
    }

    public BotUser saveAddress(BotUser botUser) {
        botUser.setAddress(botUser.getTemAddress() == null ? "" : botUser.getTemAddress());
        botUser.setState(State.STATE_SETTING_MENU.name());
        return repository.save(botUser);
    }

    public BotUser saveTempAddress(BotUser botUser) {
        return repository.save(botUser);
    }

    private BotUser buildBotUser(Message message) {
        User telegramUser = message.getFrom();
        String firstName = telegramUser.getFirstName();
        String lastName = telegramUser.getLastName();

        String fullName = firstName + (lastName != null ? " " + lastName : "");
        BotUser entity = BotUser.builder()
                .fullName(fullName)
                .userId(telegramUser.getId())
                .chatId(message.getChatId())
                .role(Role.CLIENT)
                .language(Language.UZ)
                .state(State.STATE_START.name())
                .status(Status.ACTIVE)
                .username(telegramUser.getUserName())
                .build();
        return repository.save(entity);
    }
}
