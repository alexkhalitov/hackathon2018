package ru.sbrf.hackaton.telegram.bot;

import org.apache.commons.io.IOUtils;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class BotUtils {


    public static byte[] getPhoto(String url) {
        try {
            URL fileUrl = new URL(url);
            HttpURLConnection httpConn = (HttpURLConnection) fileUrl.openConnection();
            try {
                try(InputStream inputStream = httpConn.getInputStream()) {
                    return IOUtils.toByteArray(inputStream);
                }
            }finally {
                httpConn.disconnect();
            }
        }catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static String getPhotoUrl(TelegramLongPollingBot bot, PhotoSize photoSize) {
        try {
            return bot.execute(new GetFile().setFileId(photoSize.getFileId())).getFileUrl(bot.getBotToken());
        } catch (TelegramApiException e) {
            e.printStackTrace();
            return null;
        }
    }
}
