package food.delivery.bot.service.handler.impl;

import food.delivery.backend.dto.request.BotUserDTO;
import food.delivery.backend.entity.BotUser;
import food.delivery.backend.service.BotUserService;
import food.delivery.bot.service.handler.MessageHandlerService;
import food.delivery.bot.service.base.BaseService;
import food.delivery.bot.service.message.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.List;

import static food.delivery.backend.enums.Role.CLIENT;

/**
 * Created by Avaz Absamatov
 * Date: 11/30/2025
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MessageHandlerServiceImpl implements MessageHandlerService {
    private final BotUserService botUserService;
    private final ClientMessageService clientMessageService;
    private final AdminMessageService adminMessageService;
    private final KitchenManagerMessageService kitchenManagerMessageService;
    private final DriverMessageService driverMessageService;
    private final DeveloperMessageService developerMessageService;
    private final BaseService baseService;

    @Override
    public List<BotApiMethod<?>> messageHandler(Message message) {
        try {
            BotUser botUser = botUserService.getOrRegisterUser(message);
            return switch (botUser.getRole()) {
                case CLIENT -> clientMessageService.handleClientState(message, botUser);
                case ADMIN -> adminMessageService.handleAdminState(message);
                case KITCHEN_MANAGER -> kitchenManagerMessageService.handleKitchenManagerState(message);
                case DRIVER -> driverMessageService.handleDriverState(message);
                case DEVELOPER -> developerMessageService.handleDeveloperState(message);
            };
        } catch (Exception e) {
            log.error("Message handling error", e);
            Long chatId = message.getChatId();
            return List.of(baseService.sendText(chatId, "Service temporary error", null));
        }

    }

}
