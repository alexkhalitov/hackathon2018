package ru.sbrf.hackaton.telegram.bot.client.handler.cashpoint;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.sbrf.hackaton.telegram.bot.client.CategoryHandler;
import ru.sbrf.hackaton.telegram.bot.client.ClientBot;

public class OtherHandler implements CategoryHandler {

    private final ClientBot clientBot;
    private final Long chatId;

    public OtherHandler(ClientBot clientBot, Long chatId) {
        this.clientBot = clientBot;
        this.chatId = chatId;
    }


    @Override
    public boolean update(Update update) {
        return false;
    }
}
