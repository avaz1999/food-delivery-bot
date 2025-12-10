package food.delivery.backend.service.impl;

import food.delivery.backend.dto.request.BotUserDTO;
import food.delivery.backend.entity.BotUser;
import food.delivery.backend.enums.Language;
import food.delivery.backend.enums.Role;
import food.delivery.backend.enums.State;
import food.delivery.backend.enums.Status;
import food.delivery.backend.repository.BotUserRepository;
import food.delivery.backend.service.BotUserService;
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
public class BotUserServiceImpl implements BotUserService {
    private final BotUserRepository repository;

    @Override
    public BotUserDTO getOrRegisterUser(Message message) {
        BotUser botUser = repository.findByChatId(message.getChatId());
        if (botUser == null) return buildBotUser(message);
        return buildBotUserDTO(botUser);
    }

    @Override
    public void changeState(BotUserDTO botUserDTO, State state) {
        BotUser botUser = repository.findByChatId(botUserDTO.getUserId());
        botUser.setState(state);
        repository.save(botUser);
    }

    @Override
    public Language saveLanguage(BotUserDTO botUserDTO, Language language) {
        BotUser botUser = repository.findByChatId(botUserDTO.getUserId());
        botUser.setLanguage(language);
        return repository.save(botUser).getLanguage();
    }

    private BotUserDTO buildBotUser(Message message) {
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
                .state(State.STATE_START)
                .status(Status.ACTIVE)
                .username(telegramUser.getUserName())
                .build();
        BotUser savedEntity = repository.save(entity);
        return buildBotUserDTO(savedEntity);
    }

    private BotUserDTO buildBotUserDTO(BotUser savedEntity) {
        return BotUserDTO.builder()
                .id(savedEntity.getId())
                .fullName(savedEntity.getFullName())
                .chatId(savedEntity.getChatId())
                .userId(savedEntity.getUserId())
                .language(savedEntity.getLanguage())
                .state(savedEntity.getState())
                .status(savedEntity.getStatus())
                .role(savedEntity.getRole())
                .phone(savedEntity.getPhone())
                .address(savedEntity.getAddress())
                .build();
    }
}
