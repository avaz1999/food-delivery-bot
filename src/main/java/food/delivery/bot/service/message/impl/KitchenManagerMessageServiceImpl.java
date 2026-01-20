package food.delivery.bot.service.message.impl;

import food.delivery.backend.entity.BotUser;
import food.delivery.backend.model.dto.OrderDTO;
import food.delivery.backend.service.OrderService;
import food.delivery.bot.service.base.BaseService;
import food.delivery.bot.service.base.ReplyMarkupService;
import food.delivery.bot.service.base.TemplateBuilder;
import food.delivery.bot.service.message.KitchenManagerMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;

/**
 * Created by Avaz Absamatov
 * Date: 12/2/2025
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class KitchenManagerMessageServiceImpl implements KitchenManagerMessageService {
    private final OrderService orderService;
    private final BaseService baseService;
    private final TemplateBuilder templateBuilder;
    private final ReplyMarkupService replyMarkupService;

    @Override
    public List<PartialBotApiMethod<?>> handleKitchenManagerState(Message message) {
        return null;
    }

    @Override
    public SendMessage orderMessage(BotUser botUser) {
        OrderDTO orderDTO = orderService.getActiveOrderByUser(botUser);
        String template = templateBuilder.orderTemplate(botUser.getLanguage(), orderDTO);
        InlineKeyboardMarkup markup = replyMarkupService.kitchenManagerOrderTemplate(botUser);
        return baseService.sendMessage(botUser.getChatId(), template, markup);
    }
}
