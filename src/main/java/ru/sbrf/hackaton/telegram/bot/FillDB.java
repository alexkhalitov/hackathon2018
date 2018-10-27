package ru.sbrf.hackaton.telegram.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.sbrf.hackaton.telegram.bot.dataprovider.CategoryService;
import ru.sbrf.hackaton.telegram.bot.model.Category;

import javax.annotation.PostConstruct;

@Component
public class FillDB {
    @Autowired
    CategoryService categoryService;


    @PostConstruct
    public void init(){
        System.out.println("start fill db after run app");
        fillCategories();
    }

    private void fillCategories() {
        Category category = new Category();
        category.setName("Банкомат");
        categoryService.addCategory(category);
    }

}
