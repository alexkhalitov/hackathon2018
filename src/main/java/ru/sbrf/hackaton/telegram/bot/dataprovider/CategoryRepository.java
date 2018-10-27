package ru.sbrf.hackaton.telegram.bot.dataprovider;

 import org.springframework.data.repository.CrudRepository;
 import ru.sbrf.hackaton.telegram.bot.model.Category;

public interface CategoryRepository extends CrudRepository<Category, String>{

}
