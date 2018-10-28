package ru.sbrf.hackaton.telegram.bot.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import ru.sbrf.hackaton.telegram.bot.config.Config;
import ru.sbrf.hackaton.telegram.bot.dataprovider.ClientService;
import ru.sbrf.hackaton.telegram.bot.dataprovider.IssueCategoryService;
import ru.sbrf.hackaton.telegram.bot.dataprovider.IssueService;
import ru.sbrf.hackaton.telegram.bot.model.Client;
import ru.sbrf.hackaton.telegram.bot.model.Issue;
import ru.sbrf.hackaton.telegram.bot.model.IssueCategory;
import ru.sbrf.hackaton.telegram.bot.model.IssueStatus;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

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

    private static SendMessage sayHello(Long chatId) {
        SendMessage sendMessage = new SendMessage()
                .setChatId(chatId)
                .setText("Привет! Я сбербанк-бот, чем я могу вам помочь?");

        //sendMessage.enableHtml(true);

        // create keyboard
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        //replyKeyboardMarkup.setOneTimeKeyboard(true);

        // new list
        List<KeyboardRow> keyboard = new ArrayList<>();

        // first keyboard line
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        KeyboardButton findCashPointkeyboardButton = new KeyboardButton();
        findCashPointkeyboardButton.setText(ClientBotMenu.FIND_CASHPOINT.getCode()).setRequestLocation(true);
        KeyboardButton solveProblemKeyboardButton = new KeyboardButton();
        solveProblemKeyboardButton.setText(ClientBotMenu.SOLVE_PROBLEM.getCode());
        keyboardFirstRow.add(findCashPointkeyboardButton);
        keyboardFirstRow.add(solveProblemKeyboardButton);

        // add array to list
        keyboard.add(keyboardFirstRow);

        // add list to our keyboard
        replyKeyboardMarkup.setKeyboard(keyboard);
        return sendMessage;
    }

    @PostConstruct
    public void init() throws TelegramApiRequestException {
        TelegramBotsApi botapi = new TelegramBotsApi();
        botapi.registerBot(this);
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message msg = update.getMessage();
        String txt = msg.getText();
        Client client = clientService.getByChatId(update.getMessage().getChatId());

        if (ClientBotMenu.START.getCode().equals(txt)) {
            SendMessage sendMessage = sayHello(update.getMessage().getChatId());
            sendMsg(sendMessage);

        } else if (messageIsIssueCategory(txt)) {
            if (client.getIssues().stream().anyMatch(p -> IssueStatus.NEW.equals(p.getStatus()))) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("У тебя уже есть необработанное сообщение. Если хочешь что-то добавить, просто напиши");
                }
            } else {
                issueService.createNewIssue(client);
                SendMessage sendMessage = new SendMessage()
                        .setChatId(update.getMessage().getChatId())
                        .setText("Опишите суть проблемы");
                sendMsg(sendMessage);
            }
        } else if (ClientBotMenu.SOLVE_PROBLEM.getCode().equals(txt)) {
            SendMessage sendMessage = askWhatProblem(update);
            sendMsg(sendMessage);
        } else if (messageIsIssueDescription(client)) {
            Issue newIssue = clientService.getNewIssue(client);
            newIssue.setDescription(update.getMessage().getText());
            issueService.update(newIssue);
            SendMessage sendMessage = new SendMessage()
                    .setChatId(update.getMessage().getChatId())
                    .setText("Ваше обращение передано специалисту для первичного анализа, ожидайте ответ в течение 2 минут");
            sendMsg(sendMessage);
        }
    }

    /**
     * Метод спрашивает у пользователя о типе проблемы и дает варианты ответа
     *
     * @param update Сообщение пользователя
     * @return Сообщение пользователю
     */
    private SendMessage askWhatProblem(Update update) {
        SendMessage sendMessage = new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setText("С чем возникла проблема?");

        ReplyKeyboardMarkup categoryList = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(categoryList);
        categoryList.setSelective(true);
        categoryList.setResizeKeyboard(true);
        List<KeyboardRow> keyboard = new ArrayList<>();
        for (IssueCategory issueCategory : issueCategoryService.getAllCategorys()) {
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
    public void answer(Issue issue, String answer) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Поступил ответ по заявке: " + issue + ":\n\n" + answer);
        }
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
