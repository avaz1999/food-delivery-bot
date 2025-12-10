package food.delivery.bot.service.handler.impl;

import food.delivery.backend.dto.request.BotUserDTO;
import food.delivery.backend.service.BotUserService;
import food.delivery.bot.service.base.BaseService;
import food.delivery.bot.service.callback.*;
import food.delivery.bot.service.handler.CallbackQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.List;

/**
 * Created by Avaz Absamatov
 * Date: 11/30/2025
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CallbackQueryServiceImpl implements CallbackQueryService {
    private final BotUserService botUserService;
    private final ClientCallbackQueryService clientCallbackService;
    private final AdminCallbackQueryService adminCallbackService;
    private final KitchenManagerCallbackQueryService kitchenManagerCallbackService;
    private final DriverCallbackQueryService driverCallbackService;
    private final DeveloperCallbackQueryService developerCallbackService;
    private final BaseService baseService;

    @Override
    public List<BotApiMethod<?>> callbackHandler(CallbackQuery callbackQuery) {
        try {
            BotUserDTO botUser = botUserService.getOrRegisterUser((Message) callbackQuery.getMessage());
            return switch (botUser.getRole()) {
                case CLIENT -> clientCallbackService.handleClientState(callbackQuery, botUser);
                case ADMIN -> adminCallbackService.handleAdminState(callbackQuery);
                case KITCHEN_MANAGER -> kitchenManagerCallbackService.handleKitchenManagerState(callbackQuery);
                case DRIVER -> driverCallbackService.handleDriverState(callbackQuery);
                case DEVELOPER -> developerCallbackService.handleDeveloperState(callbackQuery);
            };
        } catch (Exception e) {
            log.error("CallbackQuery handling error", e);
            Long chatId = callbackQuery.getMessage().getChatId();
            return List.of(baseService.sendText(chatId, "Service temporary error", null));
        }

    }
}
