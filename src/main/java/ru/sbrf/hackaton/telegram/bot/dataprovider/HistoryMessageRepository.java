package ru.sbrf.hackaton.telegram.bot.dataprovider;

import org.springframework.data.repository.CrudRepository;
import ru.sbrf.hackaton.telegram.bot.model.HistoryMessage;

public interface HistoryMessageRepository extends CrudRepository<HistoryMessage, Long> {
}
