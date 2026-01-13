package food.delivery.bot.service.message.impl;

import food.delivery.backend.entity.BotUser;
import food.delivery.backend.enums.State;
import food.delivery.backend.service.BotUserService;
import food.delivery.bot.service.base.BaseService;
import food.delivery.bot.service.base.ReplyMarkupService;
import food.delivery.bot.service.base.StateMessageService;
import food.delivery.bot.service.message.ClientMessageService;
import food.delivery.bot.utils.BotMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.List;
import java.util.Objects;

import static food.delivery.backend.enums.State.STATE_START;

/**
 * Created by Avaz Absamatov
 * Date: 12/2/2025
 */
@Service
@RequiredArgsConstructor
public class ClientMessageServiceImpl implements ClientMessageService {
    private final BaseService baseService;
    private final StateMessageService stateMessageService;
    private final ReplyMarkupService replyMarkupService;
    private final BotUserService botUserService;


    @Override
    public List<PartialBotApiMethod<?>> handleClientState(Message message, BotUser botUser) {
        String text = message.getText();
        List<PartialBotApiMethod<?>> result = handleStartMessage(botUser, text);
        if (!result.isEmpty()) return result;
        State state = State.valueOf(botUser.getState());
        return switch (state) {
            case STATE_START -> stateMessageService.handleStartMessage(botUser, text);
            case STATE_SETTING_PHONE_NUMBER -> stateMessageService.handleSettingPhoneNumber(botUser, message);
            case STATE_SETTING_ADDRESS -> stateMessageService.handleSettingLocation(botUser, message);

            case STATE_CHOOSE_ORDER_TYPE -> stateMessageService.handleOrderType(botUser, message);
            case STATE_CHOOSE_LOCATION -> stateMessageService.handleChooseLocation(botUser, message);
            case STATE_ENTER_NAME -> stateMessageService.handleChooseName(botUser, message);
            case ORDER -> stateMessageService.handleOrder(botUser, message);
            case CHOOSE_PAYMENT_TYPE -> stateMessageService.handleChoosePaymentType(botUser, message);
            default -> List.of(baseService.deleteMessage(botUser.getChatId(), message.getMessageId()));
        };
    }

    private List<PartialBotApiMethod<?>> handleStartMessage(BotUser botUser, String text) {
        if (!Objects.equals(botUser.getState(), STATE_START.name()) && "/start".equals(text)) {
            botUserService.changeState(botUser, State.STATE_MAIN_MENU.name());
            return baseService.mainMenuMessage(botUser);
        }
        if (!Objects.equals(botUser.getState(), STATE_START.name()) || !"/start".equals(text))
            return List.of();

        String welcome = BotMessages.WELCOME.getMessageWPar(botUser.getLanguage(), botUser.getFullName());

        String chooseLang = BotMessages.CHOOSE_LANGUAGE.getMessage(botUser.getLanguage());

        botUser.setState(State.STATE_CHOOSE_LANG.name());
        botUserService.changeState(botUser, State.STATE_CHOOSE_LANG.name());

        return List.of(
                baseService.sendMessage(botUser.getChatId(), welcome, null),
                baseService.sendMessage(botUser.getChatId(), chooseLang, replyMarkupService.chooseLanguage(botUser, false))
        );
    }
}
