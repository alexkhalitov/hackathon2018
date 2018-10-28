package ru.sbrf.hackaton.telegram.bot.client;

public enum ClientBotMenu {
    START("/start"),
    SOLVE_PROBLEM("Решить проблему"),
    FIND_CASHPOINT("Найти банкомат");

    private final String code;

    ClientBotMenu(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
