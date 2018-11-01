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
import ru.sbrf.hackaton.telegram.bot.dataprovider.ClientService;
import ru.sbrf.hackaton.telegram.bot.dataprovider.GeoPositionService;
import ru.sbrf.hackaton.telegram.bot.dataprovider.IssueService;
import ru.sbrf.hackaton.telegram.bot.dataprovider.SpecialistService;
import ru.sbrf.hackaton.telegram.bot.model.Client;
import ru.sbrf.hackaton.telegram.bot.model.GeoPosition;
import ru.sbrf.hackaton.telegram.bot.model.Issue;
import ru.sbrf.hackaton.telegram.bot.specialist.SpecialistApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SkolzkoHandler implements CategoryHandler{
    private GeoPositionService geoPositionService;
    private ClientService clientService;
    private IssueService issueService;
    private SpecialistApi specialistApi;

    private final ClientBot clientBot;
    private final long chatId;

    private static final Logger LOGGER = LoggerFactory.getLogger(SkolzkoHandler.class);
    public final Map<Long, Skolzko> states = new HashMap<>();

    SkolzkoHandler(ClientBot clientBot, long chatId, GeoPositionService geoPositionService, ClientService clientService, IssueService issueService, SpecialistApi specialistApi) {
        this.clientBot = clientBot;
        this.chatId = chatId;
        this.geoPositionService = geoPositionService;
        this.clientService = clientService;
        this.issueService = issueService;
        this.specialistApi = specialistApi;
    }
    synchronized public boolean update(Update update)  {
        try {
            long chatId = update.getMessage().getChatId();
            Skolzko skolzko = states.get(chatId);
            if (skolzko == null) {
                clientBot.execute(new SendMessage(chatId, "Пожалуйста, опишите суть проблемы")
                        .setReplyMarkup(new ReplyKeyboardRemove()));
                skolzko = new Skolzko();
                skolzko.state = SkolzkoHandler.State.ASK_TEXT;
                states.put(chatId, skolzko);
                return true;
            }
            switch (skolzko.state) {
                case ASK_TEXT:
                    //clientBot.execute(new SendSticker().setChatId(chatId).setSticker("CAADAgADoAQAAulVBRgYWAbVG2ThQgI"));
                    clientBot.execute(createLocationRequest(chatId));
                    skolzko.message = update.getMessage().getText();
                    skolzko.state = SkolzkoHandler.State.ASK_GEOPOSITION;
                    return true;

                case ASK_GEOPOSITION:
                    Issue issue = new Issue();
                    Client client = clientService.getByChatId(chatId);
                    issue.setClient(client);
                    issue.setDescription(skolzko.message);
                    Location location = update.getMessage().getLocation();
                    if (location != null) {
                        GeoPosition geoPosition = new GeoPosition();
                        geoPosition.setLatitude(location.getLatitude());
                        geoPosition.setLongitude(location.getLatitude());
                        geoPositionService.add(geoPosition);
                        issue.setGeoPosition(geoPosition);
                    }
                    issueService.add(issue);
                    specialistApi.ask(issue, skolzko.message);
                    client.getIssues().add(issue);
                    clientService.update(client);
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
                .setText("Пожалуйста, отправьте геолокацию офиса");

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

        // add list to our keyboard
        replyKeyboardMarkup.setKeyboard(keyboard);
        return sendMessage;
    }

    private static class Skolzko {
        public String getChatId() {
            return chatId;
        }

        public void setChatId(String chatId) {
            this.chatId = chatId;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public State getState() {
            return state;
        }

        public void setState(State state) {
            this.state = state;
        }

        private String chatId;
        private String message;
        private State state = State.NEW;
    }

    private enum State {
        NEW, ASK_GEOPOSITION,ASK_TEXT
    }
}
