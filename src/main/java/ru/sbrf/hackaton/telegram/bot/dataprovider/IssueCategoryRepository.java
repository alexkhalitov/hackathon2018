package ru.sbrf.hackaton.telegram.bot.dataprovider;

 import org.springframework.data.repository.CrudRepository;
 import ru.sbrf.hackaton.telegram.bot.model.IssueCategory;

public interface IssueCategoryRepository extends CrudRepository<IssueCategory, Long>{

}
