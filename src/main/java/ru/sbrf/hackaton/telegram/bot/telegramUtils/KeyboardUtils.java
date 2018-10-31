package ru.sbrf.hackaton.telegram.bot.telegramUtils;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.sbrf.hackaton.telegram.bot.client.ClientBotMenu;

import java.util.ArrayList;
import java.util.List;

public class KeyboardUtils {
    public static InlineKeyboardMarkup getInlineButton(String callbackData, String text) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keybord = new ArrayList<>();
        InlineKeyboardButton closeButton = new InlineKeyboardButton();
        closeButton.setCallbackData(callbackData);
        closeButton.setText(text);
        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(closeButton);
        keybord.add(row);
        keyboardMarkup.setKeyboard(keybord);
        return keyboardMarkup;

    }

    public static ReplyKeyboardMarkup getReplyKeyboardMarkup() {
        // create keyboard
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

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
        return replyKeyboardMarkup;
    }
}
