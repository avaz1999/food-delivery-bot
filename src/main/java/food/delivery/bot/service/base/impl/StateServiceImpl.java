package food.delivery.bot.service.base.impl;

import food.delivery.backend.dto.request.BotUserDTO;
import food.delivery.backend.enums.Language;
import food.delivery.backend.enums.State;
import food.delivery.backend.service.BotUserService;
import food.delivery.bot.service.base.BaseService;
import food.delivery.bot.service.base.MenuService;
import food.delivery.bot.service.base.ReplyMarkupService;
import food.delivery.bot.service.base.StateService;
import food.delivery.bot.utils.BotCommands;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Avaz Absamatov
 * Date: 12/2/2025
 */
@Service
@RequiredArgsConstructor
public class StateServiceImpl implements StateService {
    private final MenuService menuService;
    private final BaseService baseService;
    private final BotUserService botUserService;
    private final ReplyMarkupService replyMarkupService;

    @Override
    public List<BotApiMethod<?>> handleStartMessage(BotUserDTO botUser, String text) {
        botUserService.changeState(botUser, State.STATE_CHOOSE_LANG);
        return baseService.mainMenuMessage(botUser);
    }

    @Override
    public List<BotApiMethod<?>> handleChooseLanguage(BotUserDTO botUser, CallbackQuery callbackQuery) {
        List<BotApiMethod<?>> result = processLanguage(botUser, callbackQuery);
        botUserService.changeState(botUser, State.STATE_MAIN_MENU);
        return result;
    }

    @Override
    public List<BotApiMethod<?>> handleSettingChangeLang(BotUserDTO botUser, CallbackQuery callbackQuery) {
        return processLanguage(botUser, callbackQuery);
    }

    @Override
    public List<BotApiMethod<?>> handleMainMenu(BotUserDTO botUser, CallbackQuery callbackQuery) {
        String data = callbackQuery.getData();
        return switch (data) {
            case "MENU_START_ORDER" -> menuService.addOrder(botUser, data);
            case "MENU_ABOUT_US" -> menuService.aboutUs(botUser, data);
            case "MENU_MY_ORDER" -> menuService.myOrders(botUser, data);
            case "MENU_COMMENT" -> menuService.comment(botUser, data);
            case "MENU_SETTINGS" -> menuService.setting(botUser, callbackQuery);
            default -> throw new IllegalStateException("Unexpected value: " + data);
        };
    }

    @Override
    public List<BotApiMethod<?>> handleSettingMenu(BotUserDTO botUser, CallbackQuery callbackQuery) {
        String data = callbackQuery.getData();
        return switch (data) {
            case "SETTINGS_MENU_CHANGE_LANG" -> menuService.settingChangeLangMenu(botUser, callbackQuery);
            case "SETTINGS_MENU_CHANGE_ADDRESS" -> menuService.settingChangeAddressMenu(botUser, callbackQuery);
            case "SETTINGS_MENU_CHANGE_PHONE" -> menuService.settingChangePhoneMenu(botUser, callbackQuery);
            case "MAIN_MENU" -> menuService.mainMenu(botUser, callbackQuery);
            default -> throw new IllegalStateException("Unexpected value: " + data);
        };
    }


    @NotNull
    private List<BotApiMethod<?>> processLanguage(BotUserDTO botUser, CallbackQuery callbackQuery) {
        String data = callbackQuery.getData();
        Integer messageId = callbackQuery.getMessage().getMessageId();
        Language language;
        List<BotApiMethod<?>> result = new ArrayList<>();

        if (Objects.equals(data, BotCommands.BACK.name())) {
            BotApiMethod<?> editMessage = baseService.processSettingLanguage(botUser, messageId, baseService, replyMarkupService);
            result.add(editMessage);
            botUserService.changeState(botUser, State.STATE_SETTING_MENU);
            return result;
        }
        if (Objects.equals(data, Language.UZ.name()))
            language = botUserService.saveLanguage(botUser, Language.UZ);

        else language = botUserService.saveLanguage(botUser, Language.RU);

        botUser.setLanguage(language);
        List<BotApiMethod<?>> res = baseService.mainMenuMessage(botUser);
        result.addAll(res);
        result.removeFirst();
        botUserService.changeState(botUser, State.STATE_MAIN_MENU);
        DeleteMessage deleteMessage = baseService.deleteMessage(botUser.getChatId(), messageId);
        result.add(deleteMessage);
        return result;
    }
}
