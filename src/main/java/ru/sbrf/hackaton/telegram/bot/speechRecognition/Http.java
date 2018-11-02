package ru.sbrf.hackaton.telegram.bot.speechRecognition;

import java.net.*;
import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import com.google.gson.Gson;

public class Http {
    public static void saveFile(String file_id, String token) throws Exception {
        String       postUrl       = "https://api.telegram.org/bot"+token+"/getFile?file_id=" + file_id;// put in your url
        HttpClient   httpClient    = HttpClientBuilder.create().build();
        HttpPost     post          = new HttpPost(postUrl);
        post.setHeader("Content-type", "application/json");
        HttpResponse  response = httpClient.execute(post);

        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        String line = "";
        String fileName = "";
        while ((line = rd.readLine()) != null) {
            fileName = line.split("file_path")[1].replaceAll("\"", "").replaceAll(":", "").replaceAll("}", "");
        }
        URL url = new URL("https://api.telegram.org/file/bot"+token+"/" + fileName);
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
        File file = new File("C:\\Temp\\temp.oga");
        file.getParentFile().mkdirs();
        FileOutputStream fos = new FileOutputStream(file);

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
