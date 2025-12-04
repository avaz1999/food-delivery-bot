package food.delivery.bot.service.message.impl;

import food.delivery.backend.dto.request.BotUserDTO;
import food.delivery.backend.enums.State;
import food.delivery.bot.service.base.BaseService;
import food.delivery.bot.service.base.ReplyMarkupService;
import food.delivery.bot.service.base.StateService;
import food.delivery.bot.service.message.ClientMessageService;
import food.delivery.bot.utils.BotMessageHelper;
import food.delivery.bot.utils.BotMessages;
import food.delivery.bot.utils.ResponseCodes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.Objects;

/**
 * Created by Avaz Absamatov
 * Date: 12/2/2025
 */
@Service
@RequiredArgsConstructor
public class ClientMessageServiceImpl implements ClientMessageService {
    private final BaseService baseService;
    private final StateService stateService;
    private final ReplyMarkupService replyMarkupService;


    @Override
    public BotApiMethod<?> handleClientState(Message message, BotUserDTO botUser) {
        String text = message.getText();
        BotApiMethod<?> result = handleStartMessage(botUser, text);
        if (result != null) return result;
        return switch (botUser.getState()) {
            case STATE_START -> stateService.handleStartMessage(botUser, text);
            case STATE_CHOOSE_LANG -> null;
            case STATE_ENTER_PHONE_NUMBER -> null;
            case STATE_MAIN_MENU -> null;
        };
    }

    private BotApiMethod<?> handleStartMessage(BotUserDTO botUser, String text) {
        if (botUser.getState().equals(State.STATE_START)
                && Objects.equals(text, "/start")) {
            return baseService.sendText(botUser.getChatId(),
                    BotMessages.BOT_START1.getMessage(botUser.getLanguage()) +
                            botUser.getFullName() + BotMessages.BOT_START2.getMessage(botUser.getLanguage())
                    , null);
        }
        return null;
    }
}
