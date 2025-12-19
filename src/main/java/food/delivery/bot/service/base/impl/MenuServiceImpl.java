package food.delivery.bot.service.base.impl;

import food.delivery.backend.entity.BotUser;
import food.delivery.backend.enums.State;
import food.delivery.backend.service.BotUserService;
import food.delivery.bot.service.base.BaseService;
import food.delivery.bot.service.base.MenuService;
import food.delivery.bot.service.base.ReplyMarkupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.util.List;

import static food.delivery.bot.utils.BotMessages.*;

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
    public List<BotApiMethod<?>> addOrder(BotUser botUser, CallbackQuery callbackQuery) {
        DeleteMessage deleteMessage = baseService.deleteMessage(botUser.getChatId(), callbackQuery.getMessage().getMessageId());
        ReplyKeyboard replyKeyboard = replyMarkupService.orderType(botUser);
        String message = CHOOSE_ORDER_TYPE.getMessage(botUser.getLanguage());
        SendMessage sendMessage = baseService.sendMessage(botUser.getChatId(), message, replyKeyboard);
        botUserService.changeState(botUser, State.STATE_CHOOSE_ORDER_TYPE);
        return List.of(deleteMessage, sendMessage);
    }

    @Override
    public List<BotApiMethod<?>> aboutUs(BotUser botUserDTO, CallbackQuery callbackQuery) {
        return List.of();
    }

    @Override
    public List<BotApiMethod<?>> myOrders(BotUser botUserDTO, CallbackQuery callbackQuery) {
        return List.of();
    }

    @Override
    public List<BotApiMethod<?>> comment(BotUser botUserDTO, CallbackQuery callbackQuery) {
        return List.of();
    }

    //-------------------SETTING--------------------------
    @Override
    public List<BotApiMethod<?>> settingMenu(BotUser botUserDTO, CallbackQuery callbackQuery) {
        Integer messageId = callbackQuery.getMessage().getMessageId();
        BotApiMethod<?> editMessage = baseService.
                processSettingLanguage(botUserDTO, messageId, baseService, replyMarkupService);
        botUserService.changeState(botUserDTO, State.STATE_SETTING_MENU);

        return List.of(editMessage);
    }


    @Override
    public List<BotApiMethod<?>> settingChangeLangMenu(BotUser botUser, CallbackQuery callbackQuery) {
        String changeLangMessage = CHOOSE_LANGUAGE.getMessage(botUser.getLanguage());
        Integer messageId = callbackQuery.getMessage().getMessageId();
        botUserService.changeState(botUser, State.STATE_SETTING_CHOOSE_LANG);
        return List.of(baseService.editMessageText(botUser.getChatId(),
                changeLangMessage, messageId, replyMarkupService.chooseLanguage(botUser, true)));
    }

    @Override
    public List<BotApiMethod<?>> settingChangePhoneMenu(BotUser botUser, CallbackQuery callbackQuery) {
        String addPhone = BOT_SHARE_PHONE_NUMBER.getMessage(botUser.getLanguage());
        Integer messageId = callbackQuery.getMessage().getMessageId();
        DeleteMessage deleteMessage = baseService.deleteMessage(botUser.getChatId(), messageId);
        SendMessage sendMessage = baseService.sendMessage(botUser.getChatId(), addPhone, replyMarkupService.sharePhone(botUser));
        botUserService.changeState(botUser, State.STATE_SETTING_PHONE_NUMBER);
        return List.of(deleteMessage, sendMessage);
    }

    @Override
    public List<BotApiMethod<?>> settingChangeAddressMenu(BotUser botUser, CallbackQuery callbackQuery) {
        String message = ADD_ADDRESS.getMessage(botUser.getLanguage());
        Integer messageId = callbackQuery.getMessage().getMessageId();
        DeleteMessage deleteMessage = baseService.deleteMessage(botUser.getChatId(), messageId);
        SendMessage sendLocation = baseService.sendMessage(botUser.getChatId(), message, replyMarkupService.sendLocation(botUser));
        botUserService.changeState(botUser, State.STATE_SETTING_ADDRESS);
        return List.of(deleteMessage, sendLocation);
    }

    @Override
    public List<BotApiMethod<?>> mainMenu(BotUser botUser, CallbackQuery callbackQuery) {
        String addOrder = ADD_ORDER.getMessage(botUser.getLanguage());
        Integer messageId = callbackQuery.getMessage().getMessageId();
        botUserService.changeState(botUser, State.STATE_MAIN_MENU);
        return List.of(baseService.editMessageText(botUser.getChatId(),
                addOrder, messageId, replyMarkupService.mainMenu(botUser)));
    }
}
