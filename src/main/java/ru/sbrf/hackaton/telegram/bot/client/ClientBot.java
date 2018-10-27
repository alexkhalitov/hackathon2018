package ru.sbrf.hackaton.telegram.bot.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.sbrf.hackaton.telegram.bot.controllers.RootController;
import ru.sbrf.hackaton.telegram.bot.model.Issue;

@Service
public class ClientBot extends TelegramLongPollingBot implements ClientApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientBot.class);

    @Override
    public void onUpdateReceived(Update update) {

    }

    @Override
    public String getBotUsername() {
        return "";
    }

    @Override
    public String getBotToken() {
        return "";
    }

    @Override
    public void answer(Issue issue, String answer) {
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("Поступил ответ по заявке: " + issue + ":\n\n" + answer);
        }
    }
}
