package food.delivery.bot.controller;

import food.delivery.bot.service.handler.CallbackQueryService;
import food.delivery.bot.service.handler.MessageHandlerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Avaz Absamatov
 * Date: 12/1/2025
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class BotController {
    private final MessageHandlerService messageService;
    private final CallbackQueryService callbackQueryService;
    private final OkHttpTelegramClient telegramClient;

    @PostMapping("/webhook")
    public ResponseEntity<String> handle(@RequestBody Update update) {
        List<BotApiMethod<?>> methodsToSend = new ArrayList<>();
        if (update.hasMessage()) {
            List<BotApiMethod<?>> res = messageService.messageHandler(update.getMessage());
            methodsToSend.addAll(res);
        } else if (update.hasCallbackQuery()) {
            List<BotApiMethod<?>> c = callbackQueryService.callbackHandler(update.getCallbackQuery());
            if (c != null) methodsToSend.addAll(c);
        }
        for (BotApiMethod<?> method : methodsToSend) {
            try {
                telegramClient.execute(method);
            } catch (Exception e) {
                log.error("Failed to send METHOD={}, MESSAGE={}", method, e.getMessage());
            }
        }

        return ResponseEntity.ok("OK");

    }
}
