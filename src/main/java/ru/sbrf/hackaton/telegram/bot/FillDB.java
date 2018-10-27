package ru.sbrf.hackaton.telegram.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.sbrf.hackaton.telegram.bot.dataprovider.IssueCategoryService;
import ru.sbrf.hackaton.telegram.bot.model.IssueCategory;

import javax.annotation.PostConstruct;

@Component
public class FillDB {
    @Autowired
    IssueCategoryService issueCategoryService;


    @PostConstruct
    public void init(){
        System.out.println("start fill db after run app");
        fillCategories();
    }

    private void fillCategories() {
        IssueCategory category = new IssueCategory();
        category.setName("Банкомат");
        issueCategoryService.addCategory(category);
    }

}
