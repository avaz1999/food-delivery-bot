package food.delivery.bot.service.base.impl;

import food.delivery.backend.dto.request.BotUserDTO;
import food.delivery.bot.service.base.BaseService;
import food.delivery.bot.service.base.ReplyMarkupService;
import food.delivery.bot.service.base.StateService;
import food.delivery.bot.utils.BotMessageHelper;
import food.delivery.bot.utils.ResponseCodes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;

import java.util.List;

/**
 * Created by Avaz Absamatov
 * Date: 12/2/2025
 */
@Service
@RequiredArgsConstructor
public class StateServiceImpl implements StateService {
    private final ReplyMarkupService replyMarkupService;
    private final BaseService baseService;

    @Override
    public List<BotApiMethod<?>> handleStartMessage(BotUserDTO botUser, String text) {
        return List.of(baseService.sendText(botUser.getChatId(), BotMessageHelper.get(
                ResponseCodes.BOT_START.getMessage(),
                botUser.getFullName(),
                "\uD83C\uDDFA\uD83C\uDDFF"
        ), replyMarkupService.mainMenu(botUser)));
    }
}
