package food.delivery.bot.service.message.impl;

import food.delivery.backend.entity.BotUser;
import food.delivery.backend.enums.State;
import food.delivery.backend.service.BotUserService;
import food.delivery.bot.service.base.BaseService;
import food.delivery.bot.service.base.ReplyMarkupService;
import food.delivery.bot.service.base.StateService;
import food.delivery.bot.service.message.ClientMessageService;
import food.delivery.bot.utils.BotMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.List;

import static food.delivery.backend.enums.State.STATE_START;

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
    private final BotUserService botUserService;


    @Override
    public List<BotApiMethod<?>> handleClientState(Message message, BotUser botUser) {
        String text = message.getText();
        List<BotApiMethod<?>> result = handleStartMessage(botUser, text);
        if (!result.isEmpty()) return result;
        return switch (botUser.getState()) {
            case STATE_START -> stateService.handleStartMessage(botUser, text);
            case STATE_SETTING_PHONE_NUMBER -> stateService.handleSettingPhoneNumber(botUser, message);
            case STATE_SETTING_ADDRESS -> stateService.handleSettingLocation(botUser, message);

            case STATE_CHOOSE_ORDER_TYPE -> stateService.handleOrderType(botUser, message);
            case STATE_CHOOSE_LOCATION -> stateService.handleChooseLocation(botUser, message);
            case STATE_ENTER_NAME -> stateService.handleChooseName(botUser, message);
            default -> List.of(baseService.deleteMessage(botUser.getChatId(), message.getMessageId()));
        };
    }

    private List<BotApiMethod<?>> handleStartMessage(BotUser botUser, String text) {
        if (botUser.getState() != STATE_START && "/start".equals(text)) {
            botUserService.changeState(botUser, State.STATE_MAIN_MENU);
            return baseService.mainMenuMessage(botUser);
        }
        if (botUser.getState() != STATE_START || !"/start".equals(text))
            return List.of();

        String welcome = BotMessages.WELCOME.getMessageWPar(botUser.getLanguage(), botUser.getFullName());

        String chooseLang = BotMessages.CHOOSE_LANGUAGE.getMessage(botUser.getLanguage());

        botUser.setState(State.STATE_CHOOSE_LANG);
        botUserService.changeState(botUser, State.STATE_CHOOSE_LANG);

        return List.of(
                baseService.sendMessage(botUser.getChatId(), welcome, null),
                baseService.sendMessage(botUser.getChatId(), chooseLang, replyMarkupService.chooseLanguage(botUser, false))
        );
    }
}
