package ru.sbrf.hackaton.telegram.bot.dataprovider;

import org.springframework.data.repository.CrudRepository;
import ru.sbrf.hackaton.telegram.bot.model.Client;

public interface ClientRepository extends CrudRepository<Client, Long> {
}
