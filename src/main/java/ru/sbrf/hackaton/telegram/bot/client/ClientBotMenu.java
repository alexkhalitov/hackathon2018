package ru.sbrf.hackaton.telegram.bot.client;

public enum ClientBotMenu {
    //START("/start"),
    FORM_COMPLAINT("Оформить жалобу \uD83E\uDD2C"),
    IDEA("Предложить идею \uD83D\uDCA1"),
    SAY_SPASIBO("Сказать спасибо \uD83D\uDE0C");

    private final String code;

    ClientBotMenu(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
