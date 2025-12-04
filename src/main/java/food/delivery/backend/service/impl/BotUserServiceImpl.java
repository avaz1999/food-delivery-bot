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
        BotUser botUser = repository.findByUserId(message.getFrom().getId());
        if (botUser == null) return buildBotUser(message);
        return buildBotUserDTO(botUser);
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
                .fullName(savedEntity.getFullName())
                .chatId(savedEntity.getChatId())
                .userId(savedEntity.getUserId())
                .language(savedEntity.getLanguage())
                .state(savedEntity.getState())
                .status(savedEntity.getStatus())
                .role(savedEntity.getRole())
                .build();
    }
}
