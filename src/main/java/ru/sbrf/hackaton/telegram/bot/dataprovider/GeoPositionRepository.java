package ru.sbrf.hackaton.telegram.bot.dataprovider;

import org.springframework.data.repository.CrudRepository;
import ru.sbrf.hackaton.telegram.bot.model.GeoPosition;

public interface GeoPositionRepository extends CrudRepository<GeoPosition, Long> {
}
