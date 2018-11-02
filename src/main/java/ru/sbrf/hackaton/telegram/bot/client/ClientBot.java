package ru.sbrf.hackaton.telegram.bot.client;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import ru.sbrf.hackaton.telegram.bot.BotUtils;
import ru.sbrf.hackaton.telegram.bot.ai.SentimentalService;
import ru.sbrf.hackaton.telegram.bot.client.handler.cashpoint.DontWorkHandler;
import ru.sbrf.hackaton.telegram.bot.client.handler.SelfAnsweredCategoryHandler;
import ru.sbrf.hackaton.telegram.bot.config.Config;
import ru.sbrf.hackaton.telegram.bot.dataprovider.*;
import ru.sbrf.hackaton.telegram.bot.model.Client;
import ru.sbrf.hackaton.telegram.bot.model.Issue;
import ru.sbrf.hackaton.telegram.bot.model.IssueCategory;
import ru.sbrf.hackaton.telegram.bot.model.IssueStatus;
import ru.sbrf.hackaton.telegram.bot.specialist.SpecialistApi;
import ru.sbrf.hackaton.telegram.bot.speechRecognition.Http;
import ru.sbrf.hackaton.telegram.bot.speechRecognition.QuickstartSample;
import ru.sbrf.hackaton.telegram.bot.telegramUtils.KeyboardUtils;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static ru.sbrf.hackaton.telegram.bot.BotUtils.getPhoto;

@Service
public class ClientBot extends TelegramLongPollingBot implements ClientApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientBot.class);
    @Autowired
    private Config config;
    @Autowired
    private IssueCategoryService issueCategoryService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private IssueService issueService;
    @Autowired
    private SpecialistApi specialistApi;
    @Autowired
    private HistoryMessageRepository historyMessageRepository;
    @Autowired
    private SentimentalService sentimentalService;
    @Autowired
    private CashPointService cashPointService;
    @Autowired
    private GeoPositionService geoPositionService;

    private final Map<Long, CategoryHandler> categoryHandlerMap = new ConcurrentHashMap<>();

    @Autowired
    private SayThanks sayThanks;
    private final Set<Long> suggestIdeaSet = new HashSet<>();


    private final Set<Long> sayThanksSet = new HashSet<>();
    @Autowired
    private SuggestIdeas suggestIdeas;

    private static SendMessage sayHello(Long chatId, String txt) {
        SendMessage sendMessage = new SendMessage()
                .setChatId(chatId)
                .setText(txt != null ? txt : "Привет! Я сбербанк-бот, чем я могу вам помочь?");
        sendMessage.setReplyMarkup(KeyboardUtils.getReplyKeyboardMarkup());
        return sendMessage;
    }


    @PostConstruct
    public void init() throws TelegramApiRequestException {
        TelegramBotsApi botapi = new TelegramBotsApi();
        botapi.registerBot(this);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            processCallback(update);
        } else if (update.hasMessage()) {
            processMessage(update);
        }
    }


    private CategoryHandler createHanlder(IssueCategory category, Long chatId) {
        if(category.getAnswer() != null) {
            return new SelfAnsweredCategoryHandler(this, chatId, category);
        }
        if(issueCategoryService.findParentFor(category).getName().equals("Банкомат")) {
            String name = category.getName();
            if (name.equals("Не работает")) {
                return new DontWorkHandler(this, chatId, cashPointService);
            }else if(name.equals("Другое")) {
                return new AskSpecialistWithGeopositionSendingHandler(this, chatId, geoPositionService, clientService, issueService, specialistApi, sentimentalService);
            }
        } else if (category.getName().equals("Проблема в помещении")) {
            //депенденси инджекшон
            return new AskSpecialistWithGeopositionSendingHandler(this, chatId, geoPositionService, clientService, issueService, specialistApi, sentimentalService);
        }
        return null; //todo
    }

    private void processMessage(Update update)  {
        Message msg = update.getMessage();
        String txtFromGoogle = null;
        if (update.getMessage() != null && update.getMessage().getVoice() != null) {
            try {
                Http.saveFile(update.getMessage().getVoice().getFileId());
                txtFromGoogle = QuickstartSample.main();
                Field field = msg.getClass().getDeclaredField("text");
                field.setAccessible(true);
                //Object value = field.get(instance);
                field.set(msg, txtFromGoogle);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String txt = txtFromGoogle != null ? txtFromGoogle : update.getMessage().getText();
        Long chatId = msg.getChatId();
        Client client = clientService.getByChatId(chatId);
        CategoryHandler handler = categoryHandlerMap.get(chatId);


        if ("/start".equals(txt)) {
            SendMessage sendMessage = sayHello(chatId, null);
            sendMsg(sendMessage);
        } else if (handler != null) {
            if (!handler.update(update)) {
                categoryHandlerMap.remove(chatId);
                if(client.getIssues().stream().noneMatch(p -> IssueStatus.IN_PROCESS.equals(p.getStatus()))) {
                    SendMessage sendMessage = sayHello(chatId, "Я могу ещё чем-нибудь помочь?");
                    sendMsg(sendMessage);
                }
            }
        }
//        else if (messageIsIssueCategory(txt)) {
//            if (client.getIssues().stream().anyMatch(p -> IssueStatus.NEW.equals(p.getStatus()))) {
//                if (LOGGER.isDebugEnabled()) {
//                    LOGGER.debug("У тебя уже есть необработанное сообщение. Если хочешь что-то добавить, просто напиши");
//                }
//            } else {
//                Issue newIssue = issueService.createNewIssue(client);
//                newIssue.setIssueCategory(issueCategoryService.getIssueCategoryByName(txt));
//                issueService.update(newIssue);
//                SendMessage sendMessage = new SendMessage()
//                        .setChatId(chatId)
//                        .setText("Опишите суть проблемы");
//                sendMsg(sendMessage);
//            }
//        }
        else if (client.getIssues().stream().anyMatch(p -> IssueStatus.IN_PROCESS.equals(p.getStatus()))) {
            client.getIssues().stream().filter(p -> IssueStatus.IN_PROCESS.equals(p.getStatus()))
                    .findAny()
                    .ifPresent(p -> {
                        String photo = null;
                        if(update.getMessage().getPhoto() != null) {
                            photo = BotUtils.getPhotoUrl(this, update.getMessage().getPhoto().get(update.getMessage().getPhoto().size() - 1));
                        }
                        specialistApi.ask(p, txt, photo);
                        SendMessage sendMessage = new SendMessage()
                                .setChatId(chatId)
                                .setText("<i>Сообщение направлено сотруднику банка</i>").enableHtml(true);
                        sendMsg(sendMessage);
                    });

        } else if (ClientBotMenu.FORM_COMPLAINT.getCode().equals(txt)) {
            SendMessage sendMessage = askWhatProblem(update, null);
            sendMsg(sendMessage);
        } else if (issueCategoryService.getAllCategorys().stream().anyMatch(p -> p.getName().equals(txt))) {
            issueCategoryService.getAllCategorys().stream()
                    .filter(p -> p.getName().equals(txt))
                    .findAny()
                    .ifPresent(p -> {
                        SendMessage sendMessage = askWhatProblem(update, p);
                        if (!CollectionUtils.isEmpty(p.getChildren())) {
                            sendMsg(sendMessage);
                        } else {
                            CategoryHandler handler1 = createHanlder(p, chatId);
                            categoryHandlerMap.put(chatId, handler1);
                            if (!handler1.update(update)) {
                                categoryHandlerMap.remove(chatId);
                                takeASleep();
                                sendMessage = sayHello(chatId, "Я могу еще чем-нибудь помочь?");
                                sendMsg(sendMessage);
                            }
                        }
                    });

        } else if (messageIsIssueDescription(client)) {
            Issue newIssue = clientService.getNewIssue(client);
            issueService.update(newIssue);
            SendMessage sendMessage = new SendMessage()
                    .setChatId(chatId)
                    .setText("Ваше обращение передано специалисту для первичного анализа");
            specialistApi.ask(newIssue, update.getMessage().getText(), null);
            sendMsg(sendMessage);
        } else if (ClientBotMenu.IDEA.getCode().equals(txt)
                || suggestIdeaSet.contains(update.getMessage().getChatId())) {
            synchronized (suggestIdeaSet) {
                suggestIdeaSet.add(update.getMessage().getChatId());
                if (!suggestIdeas.update(update, this)) {
                    suggestIdeaSet.remove(update.getMessage().getChatId());
                    takeASleep();
                    SendMessage sendMessage = sayHello(update.getMessage().getChatId(), "Чем могу быть полезен?");
                    sendMsg(sendMessage);
                }
            }

        } else if (ClientBotMenu.SAY_SPASIBO.getCode().equals(txt)
                || sayThanksSet.contains(chatId)) {
            synchronized (sayThanksSet) {
                sayThanksSet.add(chatId);
                if (!sayThanks.update(update, this)) {
                    sayThanksSet.remove(chatId);
                    SendMessage sendMessage = sayHello(chatId, "Спасибо! Я могу еще чем-нибудь помочь?");
                    sendMsg(sendMessage);
                }
            }

        } else if (sayThanksSet.contains(chatId)) {
            synchronized (sayThanksSet) {
                sayThanksSet.add(chatId);
                if (!sayThanks.update(update, this)) {
                    sayThanksSet.remove(chatId);
                    SendMessage sendMessage = sayHello(chatId, "Спасибо! Я могу еще чем-нибудь помочь?");
                    sendMsg(sendMessage);
                }
            }

        }
    }

    private void takeASleep() {
        try {
            Thread.sleep(3000L);
        } catch (InterruptedException ignored) {
        }
    }

    private void processCallback(Update update) {
        if ("closeCurrentIssue".equals(update.getCallbackQuery().getData())) {
            Client client = clientService.getByChatId(update.getCallbackQuery().getMessage().getChatId());
            client.getIssues().stream().filter(p -> IssueStatus.IN_PROCESS.equals(p.getStatus()))
                    .findAny()
                    .ifPresent(p -> {
                        specialistApi.close(p);
                        SendMessage sendMessage = new SendMessage()
                                .setChatId(update.getCallbackQuery().getMessage().getChatId())
                                .setText("<i>Обращение закрыто</i>").enableHtml(true);
                        sendMsg(sendMessage);
                        takeASleep();
                        sendMessage = sayHello(update.getCallbackQuery().getMessage().getChatId(), "Я могу ещё чем-нибудь помочь?");
                        sendMsg(sendMessage);
                    });
        } else if ("mainMenu".equals(update.getCallbackQuery().getData())) {
            SendMessage sendMessage = sayHello(update.getCallbackQuery().getMessage().getChatId(), "Чем могу помочь?");
            sendMsg(sendMessage);
        }
    }

    /**
     * Метод спрашивает у пользователя о типе проблемы и дает варианты ответа
     *
     * @param update Сообщение пользователя
     * @return Сообщение пользователю
     */
    private SendMessage askWhatProblem(Update update, IssueCategory parentCategory) {
        SendMessage sendMessage = new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setText("С чем возникла проблема?");

        ReplyKeyboardMarkup categoryList = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(categoryList);
        categoryList.setSelective(true);
        categoryList.setResizeKeyboard(true);
        List<KeyboardRow> keyboard = new ArrayList<>();

        List<IssueCategory> subListCategory = new ArrayList<>();
        if (parentCategory != null) {
            subListCategory = parentCategory.getChildren();
        } else {
            for (IssueCategory category : issueCategoryService.getAllCategorys()) {
                if (category.getMainMenu()) {
                    subListCategory.add(category);
                }
            }
        }
        for (IssueCategory issueCategory : subListCategory) {
            KeyboardRow keyboardFirstRow = new KeyboardRow();
            KeyboardButton keyboardButton = new KeyboardButton();
            keyboardButton.setText(issueCategory.getName());
            keyboardFirstRow.add(keyboardButton);
            // add array to list
            keyboard.add(keyboardFirstRow);
        }
        categoryList.setKeyboard(keyboard);
        sendMessage.setReplyToMessageId(update.getMessage().getMessageId());
        return sendMessage;
    }

    @Override
    public String getBotUsername() {
        return config.getClientBotName();
    }

    @Override
    public String getBotToken() {
        return config.getClientBotToken();
    }

    @Override
    public void answer(Issue issue, String answer, String photo) {
        if(answer != null) {
            SendMessage sendMessage = new SendMessage()
                    .setChatId(issue.getClient().getChatId())
                    .setText("<i>" + issue.getAssignee().getLastname() + " " + issue.getAssignee().getFirstname() + "</i>:\n" + answer).enableHtml(true);
            sendMsg(sendMessage);
        }

        if(photo != null) {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(getPhoto(photo));
            sendPhoto(new SendPhoto().setPhoto("Фото", inputStream).setChatId(issue.getClient().getChatId()));
        }
    }

    private void sendPhoto(SendPhoto sendMessage) {
        try {
            this.execute(sendMessage);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void closeIssue(Issue issue) {
        SendMessage sendMessage = new SendMessage()
                .setChatId(issue.getClient().getChatId())
                .setText("Если ваша проблема решена, нажмите кнопку Закрыть обращение")
                .setReplyMarkup(KeyboardUtils.getInlineButton("closeCurrentIssue", "Закрыть обращение"));
        sendMsg(sendMessage);
    }

    private void sendMsg(SendMessage sendMessage) {
        try {
            this.execute(sendMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Возвращает true, если пользователь прислал категорию проблемы
     *
     * @param messageText текст
     * @return True, если текст совпадает с одной из категорий
     */
    private Boolean messageIsIssueCategory(String messageText) {
        return messageText != null && issueCategoryService.getAllCategorys()
                .stream()
                .anyMatch(p -> p.getName().toLowerCase().equals(messageText.toLowerCase()));
    }

    /**
     * Возвращает true, если пользователь прислал текст, описывающий его проблему
     *
     * @param client Клиент
     * @return True, если пользователь прислал описание своей проблемы
     */
    private Boolean messageIsIssueDescription(Client client) {
        Issue issue = clientService.getNewIssue(client);
        return issue != null && StringUtils.isEmpty(issue.getDescription());
    }

}
