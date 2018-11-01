package ru.sbrf.hackaton.telegram.bot.client;

public enum ClientBotMenu {
    START("/start"),
    FORM_COMPLAINT("Оформить жалобу"),
    IDEA("Предложить идею"),
    SAY_SPASIBO("Сказать спасибо");

    private final String code;

    ClientBotMenu(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
