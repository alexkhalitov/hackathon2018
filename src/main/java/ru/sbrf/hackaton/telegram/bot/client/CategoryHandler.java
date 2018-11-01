package ru.sbrf.hackaton.telegram.bot.client;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface CategoryHandler {

    /**
     * Обработка сообщения пользователя
     * @param update сообщение пользователя
     * @return true - продолжаем работать с пользователем, false - всё, закругляемся и идем в главное меню
     */
    boolean update(Update update);
}
