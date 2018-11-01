package ru.sbrf.hackaton.telegram.bot.specialist;

import ru.sbrf.hackaton.telegram.bot.model.Issue;

import java.util.List;

/**
 * Интерфейс вз-я клиентского бота с ботом специалиста
 */
public interface SpecialistApi {

    /**
     * Задать вопрос
     * @param issue заявка в рамках которой задается вопрос
     * @param question вопрос
     * @param photo url фото
     */
    void ask(Issue issue, String question, String photo);

    /**
     * Закрытие заявки (клиент закрыл запрос)
     * @param issue заяква
     */
    void close(Issue issue);
}
