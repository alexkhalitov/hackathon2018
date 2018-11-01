package ru.sbrf.hackaton.telegram.bot.client.handler.cashpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.sbrf.hackaton.telegram.bot.client.CategoryHandler;
import ru.sbrf.hackaton.telegram.bot.client.ClientBot;
import ru.sbrf.hackaton.telegram.bot.dataprovider.CashPointService;
import ru.sbrf.hackaton.telegram.bot.model.CashPoint;
import ru.sbrf.hackaton.telegram.bot.model.GeoPosition;

import java.util.*;


public class DontWorkHandler implements CategoryHandler {

    private final ClientBot clientBot;
    private final Long chatId;
    private final CashPointService cashPointService;


    private static final Logger LOGGER = LoggerFactory.getLogger(DontWorkHandler.class);

    public DontWorkHandler(ClientBot clientBot, Long chatId, CashPointService cashPointService) {
        this.clientBot = clientBot;
        this.chatId = chatId;
        this.cashPointService = cashPointService;
    }


    public boolean update(Update update) {
        try{
            Location location = update.getMessage().getLocation();
            String message = update.getMessage().getText();

            if(location == null) {
                clientBot.execute(createLocationRequest(chatId));
                return true;
            }else  {
                sendNearestCashpoints(location);
                return false;
            }


        }catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return false;
        }
    }

    private void sendNearestCashpoints(Location location) {
        TreeMap<Double, CashPoint> cashPoints = new TreeMap<>();
        cashPointService.getAll().forEach(cashPoint -> {
            GeoPosition geoPosition = cashPoint.getGeoPosition();
            cashPoints.put(distance(geoPosition.getLatitude(),
                    location.getLatitude(), geoPosition.getLongitude(), location.getLongitude())
                    , cashPoint);
        });
        final int[] i= new int[]{0};
        cashPoints.forEach((aDouble, cashPoint) -> {
            if(i[0] == 1) {
                try {
                    SendLocation sendLocation = new SendLocation().setLongitude(cashPoint.getGeoPosition().getLongitude())
                            .setLatitude(cashPoint.getGeoPosition().getLatitude()).setChatId(chatId);
                    clientBot.execute(sendLocation);
                    clientBot.execute(new SendMessage(chatId, "Ближайший банкомат:\n"+
                            cashPoint.getShortAddress() + " - " + aDouble.intValue() + " м."));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
            i[0]++;
        });
    }

    private static double distance(double lat1, double lat2, double lon1,
                                   double lon2) {
        final int R = 6371; // Radius of the earth
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters
        distance = Math.pow(distance, 2);
        return Math.sqrt(distance);
    }

    private static SendMessage createLocationRequest(Long chatId) {
        SendMessage sendMessage = new SendMessage()
                .setChatId(chatId)
                .setText("Отправьте мне свою геопозицию. Я подберу для Вас ближайший банкомат!");

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
