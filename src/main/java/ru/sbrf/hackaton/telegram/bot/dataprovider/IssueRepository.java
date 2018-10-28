package ru.sbrf.hackaton.telegram.bot.dataprovider;

import org.springframework.data.repository.CrudRepository;
import ru.sbrf.hackaton.telegram.bot.model.Issue;

public interface IssueRepository extends CrudRepository<Issue, Long> {
}
