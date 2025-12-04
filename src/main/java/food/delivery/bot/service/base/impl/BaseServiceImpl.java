package food.delivery.bot.service.base.impl;

import food.delivery.bot.service.base.BaseService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

/**
 * Created by Avaz Absamatov
 * Date: 12/2/2025
 */
@Service
public class BaseServiceImpl implements BaseService {
    @Override
    public SendMessage sendText(Long chatId, String text, ReplyKeyboard replyKeyboard) {
        SendMessage sendMessage = SendMessage.builder()
                .parseMode(ParseMode.MARKDOWN)
                .chatId(chatId.toString())
                .text(text)
                .build();
        if (replyKeyboard != null) sendMessage.setReplyMarkup(replyKeyboard);
        return sendMessage;
    }
}
