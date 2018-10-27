package ru.sbrf.hackaton.telegram.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.sbrf.hackaton.telegram.bot.dataprovider.IssueCategoryService;
import ru.sbrf.hackaton.telegram.bot.dataprovider.SpecialistService;
import ru.sbrf.hackaton.telegram.bot.model.IssueCategory;
import ru.sbrf.hackaton.telegram.bot.model.Specialist;

import javax.annotation.PostConstruct;

@Component
public class FillDB {

    @Autowired
    private IssueCategoryService issueCategoryService;
    @Autowired
    private SpecialistService specialistService;

    @PostConstruct
    public void init(){
        System.out.println("start fill db after run app");
        fillCategories();
        fillSpecialists();
    }

    private void fillCategories() {
        IssueCategory category = new IssueCategory();
        category.setName("Банкомат");
        issueCategoryService.addCategory(category);
    }

    private void fillSpecialists() {
        Specialist specialist = new Specialist();
        specialist.setFirstname("Александр");
        specialist.setMiddlename("Александрович");
        specialist.setLastname("Халитов");
        specialist.setMobile("79133885236");
        specialistService.add(specialist);

        specialist = new Specialist();
        specialist.setFirstname("Алексей");
        specialist.setMiddlename("Леонидович");
        specialist.setLastname("Антропов");
        specialist.setMobile("79233777159");
        specialistService.add(specialist);

        specialist = new Specialist();
        specialist.setFirstname("Артём");
        specialist.setMiddlename("Игоревич");
        specialist.setLastname("Мысик");
        specialist.setMobile("79628391294");
        specialistService.add(specialist);

        specialist = new Specialist();
        specialist.setFirstname("Александр");
        specialist.setMiddlename("Геннадьевич");
        specialist.setLastname("Лайков");
        specialist.setMobile("79137439385");
        specialistService.add(specialist);

        specialist = new Specialist();
        specialist.setFirstname("БРНВ");
        specialist.setMiddlename("БРНВ");
        specialist.setLastname("БРНВ");
        specialist.setMobile("79231857206");
        specialistService.add(specialist);
    }

}
