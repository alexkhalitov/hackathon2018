package ru.sbrf.hackaton.telegram.bot.client;

import com.google.cloud.dialogflow.v2.QueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.sbrf.hackaton.telegram.bot.ai.Dialogflow;

import java.util.*;


@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Service
public class SuggestIdeas {


    private static final Logger LOGGER = LoggerFactory.getLogger(SuggestIdeas.class);

    private final Map<Long, suggestIdea> states = new HashMap<>();

    private static InlineKeyboardMarkup createSpasiboKeyboard() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<InlineKeyboardButton> buttons1 = new ArrayList<>();
        buttons1.add(new InlineKeyboardButton().setText("Подробнее о программе \"Спасибо\"").setUrl("https://spasibosberbank.ru"));
        buttons.add(buttons1);

        /*List<InlineKeyboardButton> buttons2 = new ArrayList<>();
        buttons2.add(new InlineKeyboardButton().setText("Вернуться в главное меню").setCallbackData("mainMenu"));
        buttons.add(buttons2);*/


        InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();
        markupKeyboard.setKeyboard(buttons);
        return markupKeyboard;
    }

    synchronized boolean update(Update update, ClientBot clientBot) {
        try {
            long chatId = update.getMessage().getChatId();
            suggestIdea suggestIdea = states.get(chatId);
            if (suggestIdea == null) {
                clientBot.execute(new SendMessage(chatId, "Пожалуйста, опишите Вашу идею.")
                        .setReplyMarkup(new ReplyKeyboardRemove()));
                suggestIdea = new suggestIdea();
                suggestIdea.state = State.IDEA;
                states.put(chatId, suggestIdea);
                return true;
            }
            switch (suggestIdea.state) {
                case NEW:
                    break;
                case IDEA:
                    String message = update.getMessage().getText();
                    QueryResult queryResult = Dialogflow.detectIntentTexts(Dialogflow.PROJECT_ID, message, UUID.randomUUID().toString(), "ru-ru");
                    switch (queryResult.getIntent().getDisplayName()) {
                        case Dialogflow.CASH_BACK_INTENT:
                            clientBot.execute(new SendSticker().setChatId(chatId).setSticker("CAADAgADVAADaxboEw7RyAUHoaLyAg"));
                            clientBot.execute(new SendMessage(chatId, queryResult.getFulfillmentText())
                                    .setReplyMarkup(createSpasiboKeyboard()));
                            suggestIdea.state = State.OLD_IDEA;
                            states.remove(chatId);
                            return false;
                        case Dialogflow.DEFAULT_FALLBACK:
                            suggestIdea.state = State.NEW_IDEA;
                            states.remove(chatId);
                            clientBot.execute(new SendSticker().setChatId(chatId).setSticker("CAADAgADoAQAAulVBRgYWAbVG2ThQgI"));
                            clientBot.execute(new SendMessage(chatId, "Благодарим за идею. Мы ее обязательно рассмотрим"));
                            return false;
                        default:
                            throw new IllegalArgumentException("Что-то пошло не так");

                    }
                default:
                    throw new IllegalArgumentException("Что-то пошло не так");
            }
            /*SayThank sayThank = states.get(chatId);
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
                        geoPosition.setLongitude(location.getLatitude());
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
            }*/
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    private enum State {
        IDEA, NEW, NEW_IDEA, OLD_IDEA
    }

    private static class suggestIdea {
        private String chatId;
        private String message;
        private SuggestIdeas.State state = SuggestIdeas.State.NEW;
    }


}
