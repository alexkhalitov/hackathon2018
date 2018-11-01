package ru.sbrf.hackaton.telegram.bot.speechRecognition;

import java.net.*;
import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class Http {
    public static void main(String[] file_id) throws Exception {
        URL url1 = new URL("https://api.telegram.org/bot512352697:AAEtgjMGhbjsrttQ6WQy0r1dewNBFIU21Zw/getFile?file_id=AwADAgADdwMAAjn_2UqFf9WFGS2quwI");
        URLConnection yc1 = url1.openConnection();
        BufferedReader in1 = new BufferedReader(
                new InputStreamReader(
                        yc1.getInputStream()));
        String inputLine;
        while ((inputLine = in1.readLine()) != null) {

        }
        URL url = new URL("https://api.telegram.org/file/bot512352697:AAEtgjMGhbjsrttQ6WQy0r1dewNBFIU21Zw/voice/file_59.oga");
        URLConnection yc = url.openConnection();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        yc.getInputStream()));


        HttpURLConnection conn = (HttpURLConnection) url.openConnection(Proxy.NO_PROXY);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setConnectTimeout(60000);
        conn.setRequestMethod("GET");
// подготовили коннект

// Т.к. запрос у нас GET, то сразу принимаем входящие данные.
// Вот тут как раз (при открытии InputStream ) и происходит отправка GET запроса на сервер.
        InputStream inStream = conn.getInputStream();
// Т.к. файл у нас бинарный, открываем ReadableByteChannel и создаем файл
        ReadableByteChannel rbc = Channels.newChannel(conn.getInputStream());
        FileOutputStream fos = new FileOutputStream("C:\\Temp\\temp.oga");

// Перенаправляем данные из ReadableByteChannel прямо канал файла.
// Говорят, так быстрее, чем по одному байту вычитывать из потока и писать в файл.
        long filePosition = 0;
        long transferedBytes = fos.getChannel().transferFrom(rbc, filePosition, Long.MAX_VALUE);

        while(transferedBytes == Long.MAX_VALUE){
            filePosition += transferedBytes;
            transferedBytes = fos.getChannel().transferFrom(rbc, filePosition, Long.MAX_VALUE);
        }
        rbc.close();
        fos.close();
    }
}
