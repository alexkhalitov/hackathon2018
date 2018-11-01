package ru.sbrf.hackaton.telegram.bot.client.handler;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.sbrf.hackaton.telegram.bot.client.CategoryHandler;
import ru.sbrf.hackaton.telegram.bot.client.ClientBot;
import ru.sbrf.hackaton.telegram.bot.model.IssueCategory;

public class SelfAnsweredCategoryHandler implements CategoryHandler {

    private final ClientBot clientBot;
    private final Long chatId;
    private final String answer;

    public SelfAnsweredCategoryHandler(ClientBot clientBot, Long chatId, IssueCategory category) {
        this.clientBot = clientBot;
        this.chatId = chatId;
        this.answer = category.getAnswer();
    }

    @Override
    public boolean update(Update update) {
        try {
            clientBot.execute(new SendMessage(chatId, answer)
                    .setReplyMarkup(new ReplyKeyboardRemove()).enableHtml(true));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return false;
    }
}
