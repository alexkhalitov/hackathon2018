package ru.sbrf.hackaton.telegram.bot.dataprovider;

import org.springframework.data.repository.CrudRepository;
import ru.sbrf.hackaton.telegram.bot.model.Thanks;

public interface ThanksRepository extends CrudRepository<Thanks, Long> {
}
