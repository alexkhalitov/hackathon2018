package ru.sbrf.hackaton.telegram.bot.config;

import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.io.FileHandler;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileReader;

@Service
public class Config {

    private static final String FILE_LOCATION_PROP = "config.file";

    private String botName;

    private String botToken;

    @PostConstruct
    private void init() throws Exception {
        String file = System.getProperty(FILE_LOCATION_PROP);
        if (file == null) {
            throw new IllegalArgumentException("В параметрах JVM не задан путь до файла конфигурации: " + FILE_LOCATION_PROP);
        }
        XMLConfiguration xmlConfiguration = new XMLConfiguration();
        FileHandler fh = new FileHandler(xmlConfiguration);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            fh.load(reader);
        }

        botName = xmlConfiguration.getString("bot.name");
        botToken = xmlConfiguration.getString("bot.token");

//        List<QueueSettings> queueSettings = xmlConfiguration.configurationsAt("queues.queue").stream().map(node -> {
//            QueueSettings queueSetting = new QueueSettings();
//            queueSetting.setQueue(node.getString("queue"));
//            queueSetting.setQueueConnectionFactory(node.getString("connectionFactoryJndi"));
//            queueSetting.setQueueType(QueueSettings.QueueType.valueOf(node.getString("type")));
//            queueSetting.setExecutorsSize(node.getInt("executors"));
//            return queueSetting;
//        }).collect(Collectors.toList());

    }

    public String getBotName() {
        return botName;
    }

    public void setBotName(String botName) {
        this.botName = botName;
    }

    public String getBotToken() {
        return botToken;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }
}
