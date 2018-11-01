package ru.sbrf.hackaton.telegram.bot.telegramUtils;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.sbrf.hackaton.telegram.bot.client.ClientBot;
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

        for (ClientBotMenu menuPoint : ClientBotMenu.values()) {
            KeyboardRow keyboardFirstRow = new KeyboardRow();
            KeyboardButton newKeyboardButton = new KeyboardButton();
            newKeyboardButton.setText(menuPoint.getCode());
            keyboardFirstRow.add(newKeyboardButton);
            keyboard.add(keyboardFirstRow);
        }


        // add list to our keyboard
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }
}
