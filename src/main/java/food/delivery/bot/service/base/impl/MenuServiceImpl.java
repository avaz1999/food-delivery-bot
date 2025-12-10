package food.delivery.bot.service.base.impl;

import food.delivery.backend.dto.request.BotUserDTO;
import food.delivery.backend.enums.State;
import food.delivery.backend.service.BotUserService;
import food.delivery.bot.service.base.BaseService;
import food.delivery.bot.service.base.MenuService;
import food.delivery.bot.service.base.ReplyMarkupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.List;

import static food.delivery.bot.utils.BotMessages.ADD_ORDER;
import static food.delivery.bot.utils.BotMessages.CHOOSE_LANGUAGE;

/**
 * Created by Avaz Absamatov
 * Date: 12/6/2025
 */
@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {
    private final BaseService baseService;
    private final ReplyMarkupService replyMarkupService;
    private final BotUserService botUserService;

    @Override
    public List<BotApiMethod<?>> addOrder(BotUserDTO botUserDTO, String data) {
        return List.of();
    }

    @Override
    public List<BotApiMethod<?>> aboutUs(BotUserDTO botUserDTO, String data) {
        return List.of();
    }

    @Override
    public List<BotApiMethod<?>> myOrders(BotUserDTO botUserDTO, String data) {
        return List.of();
    }

    @Override
    public List<BotApiMethod<?>> comment(BotUserDTO botUserDTO, String data) {
        return List.of();
    }

    //-------------------SETTING--------------------------
    @Override
    public List<BotApiMethod<?>> setting(BotUserDTO botUserDTO, CallbackQuery callbackQuery) {
        Integer messageId = callbackQuery.getMessage().getMessageId();
        BotApiMethod<?> editMessage = baseService.
                processSettingLanguage(botUserDTO, messageId, baseService, replyMarkupService);
        botUserService.changeState(botUserDTO, State.STATE_SETTING_MENU);

        return List.of(editMessage);
    }


    @Override
    public List<BotApiMethod<?>> settingChangeLangMenu(BotUserDTO botUser, CallbackQuery callbackQuery) {
        String changeLangMessage = CHOOSE_LANGUAGE.getMessage(botUser.getLanguage());
        Integer messageId = callbackQuery.getMessage().getMessageId();
        botUserService.changeState(botUser, State.STATE_SETTING_CHOOSE_LANG);
        return List.of(baseService.editMessageText(botUser.getChatId(),
                changeLangMessage, messageId, replyMarkupService.chooseLanguage(botUser, true)));
    }

    @Override
    public List<BotApiMethod<?>> settingChangeAddressMenu(BotUserDTO botUser, CallbackQuery callbackQuery) {
        return List.of();
    }

    @Override
    public List<BotApiMethod<?>> settingChangePhoneMenu(BotUserDTO botUser, CallbackQuery callbackQuery) {
        return List.of();
    }

    @Override
    public List<BotApiMethod<?>> mainMenu(BotUserDTO botUser, CallbackQuery callbackQuery) {
        String addOrder = ADD_ORDER.getMessage(botUser.getLanguage());
        Integer messageId = callbackQuery.getMessage().getMessageId();
        botUserService.changeState(botUser, State.STATE_MAIN_MENU);
        return List.of(baseService.editMessageText(botUser.getChatId(),
                addOrder, messageId, replyMarkupService.mainMenu(botUser)));
    }
}
