package ru.sbrf.hackaton.telegram.bot.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;


public class CashPointDontWork implements CategoryHandler{

    private final ClientBot clientBot;
    private final Long chatId;


    private static final Logger LOGGER = LoggerFactory.getLogger(CashPointDontWork.class);

    CashPointDontWork(ClientBot clientBot, Long chatId) {
        this.clientBot = clientBot;
        this.chatId = chatId;
    }


    public boolean update(Update update) {
        try{
            Location location = update.getMessage().getLocation();
            String message = update.getMessage().getText();

            if(location == null) {
                clientBot.execute(createLocationRequest(chatId));
                return true;
            }else  {
                clientBot.execute(sendNearestCashpoints());
                return false;
            }


        }catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return false;
        }
    }

    private SendMessage sendNearestCashpoints() {
        return null;
    }

    private static SendMessage createLocationRequest(Long chatId) {
        SendMessage sendMessage = new SendMessage()
                .setChatId(chatId)
                .setText("Я буду очень признателен Вам, если Вы отправите мне свою геопозицию");

        sendMessage.enableHtml(true);

        // create keyboard
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        // new list
        List<KeyboardRow> keyboard = new ArrayList<>();

        // first keyboard line
        KeyboardRow row = new KeyboardRow();
        KeyboardButton keyboardButton = new KeyboardButton();
        keyboardButton.setText("Поделиться геопозицией").setRequestLocation(true);
        row.add(keyboardButton);
        keyboard.add(row);

        // second keyboard line
        row = new KeyboardRow();
        keyboardButton = new KeyboardButton();
        keyboardButton.setText("Пропустить");
        row.add(keyboardButton);
        keyboard.add(row);

        // add list to our keyboard
        replyKeyboardMarkup.setKeyboard(keyboard);
        return sendMessage;
    }

 


}
