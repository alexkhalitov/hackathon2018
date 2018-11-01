package ru.sbrf.hackaton.telegram.bot.dataprovider;

import org.springframework.data.repository.CrudRepository;
import ru.sbrf.hackaton.telegram.bot.model.CashPoint;

public interface CashPointRepository extends CrudRepository<CashPoint, Long> {
}
