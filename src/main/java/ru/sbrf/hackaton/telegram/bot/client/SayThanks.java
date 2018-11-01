package ru.sbrf.hackaton.telegram.bot.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.sbrf.hackaton.telegram.bot.dataprovider.GeoPositionService;
import ru.sbrf.hackaton.telegram.bot.dataprovider.ThanksService;
import ru.sbrf.hackaton.telegram.bot.model.GeoPosition;
import ru.sbrf.hackaton.telegram.bot.model.Thanks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Service
public class SayThanks {

    private static final Logger LOGGER = LoggerFactory.getLogger(SayThanks.class);

    @Autowired
    private ThanksService thanksService;
    @Autowired
    private GeoPositionService geoPositionService;

    private final Map<Long, SayThank> states = new HashMap<>();

    synchronized boolean update(Update update, ClientBot clientBot)  {
        try {
            long chatId = update.getMessage().getChatId();
            SayThank sayThank = states.get(chatId);
            if (sayThank == null) {
                clientBot.execute(new SendMessage(chatId, "Пожалуйста, напишите Ваш отзыв")
                        .setReplyMarkup(new ReplyKeyboardRemove()));
                sayThank = new SayThank();
                sayThank.state = State.ASK_TEXT;
                states.put(chatId, sayThank);
                return true;
            }
            switch (sayThank.state) {
                case ASK_TEXT:
                    clientBot.execute(new SendSticker().setChatId(chatId).setSticker("CAADAgADoAQAAulVBRgYWAbVG2ThQgI"));
                    clientBot.execute(createLocationRequest(chatId));
                    sayThank.message = update.getMessage().getText();
                    sayThank.state = State.ASK_GEOPOSITION;
                    return true;

                case ASK_GEOPOSITION:Thanks thanks = new Thanks();
                    thanks.setChatId(chatId);
                    thanks.setMessage(sayThank.message);
                    Location location = update.getMessage().getLocation();
                    if (update.getMessage().getLocation() != null) {
                        GeoPosition geoPosition = new GeoPosition();
                        geoPosition.setLatitude(location.getLatitude());
                        geoPosition.setLongitude(location.getLongitude());
                        geoPositionService.add(geoPosition);
                        thanks.setPosition(geoPosition);
                    }
                    thanksService.add(thanks);
                    clientBot.execute(new SendSticker()
                            .setReplyMarkup(new ReplyKeyboardRemove())
                            .setChatId(chatId).setSticker("CAADAgADmQQAAulVBRi8-VqIiMu2WAI"));
                    states.remove(chatId);
                    return false;
                default:
                    throw new IllegalArgumentException("Что-то пошло не так");
            }
        }catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return false;
        }

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


    private static class SayThank {
        private String chatId;
        private String message;
        private State state = State.NEW;
    }

    private enum State {
        NEW, ASK_TEXT, ASK_GEOPOSITION
    }


}
