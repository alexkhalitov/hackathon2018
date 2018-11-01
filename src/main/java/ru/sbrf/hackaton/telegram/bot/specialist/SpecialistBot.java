package ru.sbrf.hackaton.telegram.bot.specialist;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import ru.sbrf.hackaton.telegram.bot.client.ClientApi;
import ru.sbrf.hackaton.telegram.bot.config.Config;
import ru.sbrf.hackaton.telegram.bot.dataprovider.IssueService;
import ru.sbrf.hackaton.telegram.bot.dataprovider.HistoryMessageRepository;
import ru.sbrf.hackaton.telegram.bot.dataprovider.IssueService;
import ru.sbrf.hackaton.telegram.bot.dataprovider.SpecialistService;
import ru.sbrf.hackaton.telegram.bot.model.Issue;
import ru.sbrf.hackaton.telegram.bot.model.IssueStatus;
import ru.sbrf.hackaton.telegram.bot.model.Specialist;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RejectedExecutionException;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Service
public class SpecialistBot extends TelegramLongPollingBot implements SpecialistApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpecialistBot.class);

    private final Map<Specialist, Issue> activeIssues = new ConcurrentHashMap<>();

    private final Map<Long, Specialist> specialists = new HashMap<>();

    @Autowired
    private ClientApi clientApi;
    @Autowired
    private SpecialistService specialistService;
    @Autowired
    private IssueService issueService;

    @Autowired
    private Config config;
    @Autowired
    private HistoryMessageRepository historyMessageRepository;

    @Override
    public String getBotUsername() {
        return config.getSpecialistBotName();
    }

    @Override
    public String getBotToken() {
        return config.getSpeciliastBotToken();
    }

    @PostConstruct
    public void init() throws TelegramApiRequestException {
        TelegramBotsApi botapi = new TelegramBotsApi();
        botapi.registerBot(this);
        specialistService.getWithChatId().forEach(sp -> specialists.put(sp.getId(), sp));
    }

    @PreDestroy
    public void shutdown() {
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message msg = update.getMessage();
        String txt = msg.getText();
        if ("/start".equals(txt)) {
            SendMessage sendMessage = createContactRequest(update.getMessage().getChatId());
            sendMsg(sendMessage);
        } else {
            Contact contact = msg.getContact();
            Specialist specialist;
            if(msg.getChatId() == null) {
                System.out.println("Chat id is null??!!");
                return;
            }
            long chatId = msg.getChatId();
            if(contact != null && contact.getPhoneNumber() != null) {
                specialist = findSpecialist(chatId, contact);
                if(specialist == null) {
                    return;
                }
                synchronized (specialists) {
                    specialists.put(specialist.getId(), specialist);
                }
                return;
            } else {
                specialist = specialistService.getByChatId(chatId);
            }
            if(specialist == null) {
                sendMsg(createContactRequest(chatId));
                return;
            }
            Issue issue = activeIssues.get(specialist);
            if(issue != null) {
                clientApi.answer(issue, txt);
                sendMsg(new SendMessage(chatId, "<i>Сообщение отправлено клиенту</i>").enableHtml(true));
            }else {
                sendMsg(new SendMessage(chatId, "<i>В данный момент у вас нет активной заявки</i>").enableHtml(true));

            }
        }
    }

    private Specialist findSpecialist(long chatId, Contact contact) {
        Specialist specialist = specialistService.getByMobile(contact.getPhoneNumber());
        if(specialist == null) {
            sendMsg(new SendMessage(chatId, "К сожалению, я не могу понять с кем имею дело :("));;
            return null;
        }
        specialist.setChatId(chatId);
        specialistService.update(specialist);
        SendMessage sendMessage = new SendMessage(chatId, "Добро пожаловать, "+specialist.getFirstname()+"! Как будет вопрос, я напишу Вам!");
        sendMessage.setReplyMarkup(new ReplyKeyboardRemove());
        sendMsg(sendMessage);
        return specialist;
    }

    private void sendMsg(SendMessage sendMessage) {
        try {
            this.execute(sendMessage);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void sendMsg(SendSticker sendMessage) {
        try {
            this.execute(sendMessage);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static SendMessage createContactRequest(Long chatId) {
        SendMessage sendMessage = new SendMessage()
                .setChatId(chatId)
                .setText("Привет, коллега! Давайте познакомимся. Отправьте, пожалуйста, мне свой номер мобильного телефона");

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
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        KeyboardButton keyboardButton = new KeyboardButton();
        keyboardButton.setText("Поделиться номером телефона").setRequestContact(true);
        keyboardFirstRow.add(keyboardButton);

        // add array to list
        keyboard.add(keyboardFirstRow);

        // add list to our keyboard
        replyKeyboardMarkup.setKeyboard(keyboard);
        return sendMessage;
    }

    public void ask(Issue issue, String question) {
        Specialist victim = issue.getAssignee();
        boolean firstQuestion = false;
        if(victim == null) {
            synchronized (specialists) {
                for(Specialist specialist : specialists.values()) {
                    if(activeIssues.get(specialist) == null) {
                        victim = specialist;
                        break;
                    }
                }
                if(victim == null) {
                    throw new RejectedExecutionException("Извините, все специалисты заняты");
                }
                issue.setAssignee(victim);
                issue.setStatus(IssueStatus.IN_PROCESS);
                issueService.update(issue);
                activeIssues.put(victim, issue);
            }
            firstQuestion = true;
        }
        sendMsg(new SendMessage(victim.getChatId(), "<i>Поступил сообщение по заявке №:"+issue.getId()+'\n'+question+"</i>").enableHtml(true));
        if(firstQuestion) {
            clientApi.answer(issue, "Ваш запрос взят в работу. Пожалуйста, ожидайте ответа");
        }
    }

    @Override
    public void close(Issue issue) {
        issue.setStatus(IssueStatus.COMPLETED);
        issueService.update(issue);
        activeIssues.values().remove(issue);
    }

}