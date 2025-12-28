package food.delivery.bot.service.callback.impl;

import food.delivery.backend.entity.BotUser;
import food.delivery.backend.enums.State;
import food.delivery.bot.service.base.StateCallbackQueryService;
import food.delivery.bot.service.callback.ClientCallbackQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.List;

/**
 * Created by Avaz Absamatov
 * Date: 12/6/2025
 */
@Service
@RequiredArgsConstructor
public class ClientCallbackQueryServiceImpl implements ClientCallbackQueryService {
    private final StateCallbackQueryService stateService;

    @Override
    public List<PartialBotApiMethod<?>> handleClientState(CallbackQuery callbackQuery, BotUser botUser) {
        State currentState = State.valueOf(botUser.getState());
        return switch (currentState) {
            case STATE_CHOOSE_LANG -> stateService.handleChooseLanguage(botUser, callbackQuery);
            case STATE_MAIN_MENU -> stateService.handleMainMenu(botUser, callbackQuery);
            case STATE_SETTING_MENU -> stateService.handleSettingMenu(botUser, callbackQuery);
            case STATE_SETTING_CHOOSE_LANG -> stateService.handleSettingChangeLang(botUser, callbackQuery);
            case CHOOSE_ITEM_CATEGORY -> stateService.handleChooseOrderType(botUser, callbackQuery);
            case CHOOSE_ITEM -> stateService.handleChooseItem(botUser, callbackQuery);
            default -> throw new IllegalStateException("Unexpected value: " + currentState);
        };
    }
}
