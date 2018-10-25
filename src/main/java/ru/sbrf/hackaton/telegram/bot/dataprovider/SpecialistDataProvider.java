package ru.sbrf.hackaton.telegram.bot.dataprovider;

import ru.sbrf.hackaton.telegram.bot.model.Specialist;


public interface SpecialistDataProvider {
    Specialist getSpecialistByMobile(String mobile);

    void update(Specialist specialist);

    Specialist getSpecialistByChatId(Long chatId);
}
