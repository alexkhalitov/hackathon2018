package ru.sbrf.hackaton.telegram.bot.specialist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import ru.sbrf.hackaton.telegram.bot.config.Config;
import ru.sbrf.hackaton.telegram.bot.dataprovider.SpecialistDataProvider;
import ru.sbrf.hackaton.telegram.bot.model.Specialist;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;

@Service
public class SpecialistBot extends TelegramLongPollingBot {


    @Autowired
    private SpecialistDataProvider dataProvider;

    @Autowired
    private Config config;

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    public static void main(String[] args) throws InterruptedException {
    }


    @Override
    public String getBotToken() {
        return config.getBotToken();
    }

    @PostConstruct
    public void init() throws TelegramApiRequestException {
        ApiContextInitializer.init(); // Инициализируем апи
        TelegramBotsApi botapi = new TelegramBotsApi();
        botapi.registerBot(this);
    }

    @PreDestroy
    public void shutdown() {
    }


    @Override
    public void onUpdateReceived(Update update) {
        Message msg = update.getMessage();
        String txt = msg.getText();
        if (txt.equals("/start")) {
            SendMessage sendMessage = createContactRequest(update.getMessage().getChatId());
            sendMsg(sendMessage);
        } else {
            Contact contact = msg.getContact();
            Specialist specialist;
            Long chatId = msg.getChatId();
            if(contact != null && contact.getPhoneNumber() != null) {
                specialist = dataProvider.getSpecialistByMobile(contact.getPhoneNumber());
                if(specialist == null) {
                    sendMsg(new SendMessage(chatId, "К сожалению, я не могу понять с кем имею дело :("));;
                }
                specialist.setChatId(chatId);
                dataProvider.update(specialist);
            }else {
                specialist = dataProvider.getSpecialistByChatId(chatId);
            }
            if(specialist == null) {
                sendMsg(createContactRequest(chatId));
            }
            //....
        }
    }

    private void sendMsg(SendMessage sendMessage) {
        try {
            this.execute(sendMessage);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static SendMessage createContactRequest(Long chatId) {
        SendMessage sendMessage = new SendMessage()
                .setChatId(chatId)
                .setText("Привет, коллега! Отправьте, пожалуйста, мне свои данные");

        // create keyboard
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        // new list
        List<KeyboardRow> keyboard = new ArrayList<>();

        // first keyboard line
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        KeyboardButton keyboardButton = new KeyboardButton();
        keyboardButton.setText("Поделиться номером телефона >").setRequestContact(true);
        keyboardFirstRow.add(keyboardButton);

        // add array to list
        keyboard.add(keyboardFirstRow);

        // add list to our keyboard
        replyKeyboardMarkup.setKeyboard(keyboard);
        return sendMessage;
    }


}