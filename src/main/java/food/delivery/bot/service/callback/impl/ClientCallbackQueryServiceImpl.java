package food.delivery.bot.service.callback.impl;

import food.delivery.backend.entity.BotUser;
import food.delivery.backend.enums.State;
import food.delivery.backend.model.dto.MyOrderDTO;
import food.delivery.backend.model.dto.OrderDTO;
import food.delivery.backend.model.dto.PageableDTO;
import food.delivery.backend.service.OrderService;
import food.delivery.bot.service.base.BaseService;
import food.delivery.bot.service.base.ReplyMarkupService;
import food.delivery.bot.service.base.StateCallbackQueryService;
import food.delivery.bot.service.base.TemplateBuilder;
import food.delivery.bot.service.callback.ClientCallbackQueryService;
import food.delivery.bot.utils.BotCommands;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;
import java.util.Objects;

/**
 * Created by Avaz Absamatov
 * Date: 12/6/2025
 */
@Service
@RequiredArgsConstructor
public class ClientCallbackQueryServiceImpl implements ClientCallbackQueryService {
    private final StateCallbackQueryService stateService;
    private final OrderService orderService;
    private final ReplyMarkupService replyMarkupService;
    private final TemplateBuilder templateBuilder;
    private final BaseService baseService;

    @Override
    public List<PartialBotApiMethod<?>> handleClientState(CallbackQuery callbackQuery, BotUser botUser) {
        List<PartialBotApiMethod<?>> result = myOrdersCommand(botUser, callbackQuery);
        if (!result.isEmpty()) return result;
        State currentState = State.valueOf(botUser.getState());
        return switch (currentState) {
            case STATE_CHOOSE_LANG -> stateService.handleChooseLanguage(botUser, callbackQuery);
            case STATE_MAIN_MENU -> stateService.handleMainMenu(botUser, callbackQuery);
            case STATE_SETTING_MENU -> stateService.handleSettingMenu(botUser, callbackQuery);
            case STATE_SETTING_CHOOSE_LANG -> stateService.handleSettingChangeLang(botUser, callbackQuery);
            case CHOOSE_ITEM_CATEGORY -> stateService.handleChooseOrderType(botUser, callbackQuery);
            case CHOOSE_ITEM -> stateService.handleChooseItem(botUser, callbackQuery);
            case MY_CART -> stateService.handleMyCart(botUser, callbackQuery);
            default -> throw new IllegalStateException("Unexpected value: " + currentState);
        };
    }

    private List<PartialBotApiMethod<?>> myOrdersCommand(BotUser botUser, CallbackQuery callback) {
        String data = callback.getData();
        Integer messageId = callback.getMessage().getMessageId();
        String[] split = data.split("#");
        String command = split[0];
        if (!Objects.equals(command, "order")) {
            return List.of();
        }
        if (Objects.equals(BotCommands.CLOSE.name(), split[1])) {
            DeleteMessage deleteMessage = baseService.deleteMessage(botUser.getChatId(), messageId);
            return List.of(deleteMessage);
        }
        int orderIdOrPage = Integer.parseInt(split[2]);
        if (Objects.equals(split[1], "order")) {
            OrderDTO orderDTO = orderService.getOrderById(orderIdOrPage, botUser);
            String template = templateBuilder.orderTemplate(botUser.getLanguage(), orderDTO);
            InlineKeyboardMarkup close = replyMarkupService.close(botUser);
            SendMessage sendMessage = baseService.sendMessage(botUser.getChatId(), template, close);
            return List.of(sendMessage);
        }
        PageableDTO<MyOrderDTO> myOrders = orderService.getMyOrders(botUser, orderIdOrPage, 5);
        InlineKeyboardMarkup markup = replyMarkupService.myOrders(botUser, myOrders, orderIdOrPage);
        String template = templateBuilder.buildMyOrders(myOrders.getItems(), botUser.getLanguage());
        EditMessageText editMessageText = baseService.editMessageText(botUser.getChatId(), template, messageId, markup);
        return List.of(editMessageText);
    }
}
