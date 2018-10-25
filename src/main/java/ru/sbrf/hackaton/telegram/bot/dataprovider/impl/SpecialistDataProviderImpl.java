package ru.sbrf.hackaton.telegram.bot.dataprovider.impl;

import org.springframework.stereotype.Service;
import ru.sbrf.hackaton.telegram.bot.dataprovider.SpecialistDataProvider;
import ru.sbrf.hackaton.telegram.bot.model.Specialist;

@Service
public class SpecialistDataProviderImpl implements SpecialistDataProvider {
    @Override
    public Specialist getSpecialistByMobile(String mobile) {
        return null;
    }

    @Override
    public void update(Specialist specialist) {

    }

    @Override
    public Specialist getSpecialistByChatId(Long chatId) {
        return null;
    }
}
