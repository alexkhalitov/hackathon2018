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

    private String specialistBotName;
    private String speciliastBotToken;

    private String clientBotName;
    private String clientBotToken;

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

        specialistBotName = xmlConfiguration.getString("bots.specialist.name");
        speciliastBotToken = xmlConfiguration.getString("bots.specialist.token");
        clientBotName = xmlConfiguration.getString("bots.client.name");
        clientBotToken = xmlConfiguration.getString("bots.client.token");

    }

    public String getSpecialistBotName() {
        return specialistBotName;
    }
    public String getSpeciliastBotToken() {
        return speciliastBotToken;
    }

    public String getClientBotName() {
        return clientBotName;
    }

    public String getClientBotToken() {
        return clientBotToken;
    }
}
