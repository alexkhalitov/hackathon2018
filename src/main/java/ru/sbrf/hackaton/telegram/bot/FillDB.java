package ru.sbrf.hackaton.telegram.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.sbrf.hackaton.telegram.bot.dataprovider.CashPointService;
import ru.sbrf.hackaton.telegram.bot.dataprovider.GeoPositionService;
import ru.sbrf.hackaton.telegram.bot.dataprovider.IssueCategoryService;
import ru.sbrf.hackaton.telegram.bot.dataprovider.SpecialistService;
import ru.sbrf.hackaton.telegram.bot.model.CashPoint;
import ru.sbrf.hackaton.telegram.bot.model.GeoPosition;
import ru.sbrf.hackaton.telegram.bot.model.IssueCategory;
import ru.sbrf.hackaton.telegram.bot.model.Specialist;

import javax.annotation.PostConstruct;

@Component
public class FillDB {

    @Autowired
    private IssueCategoryService issueCategoryService;
    @Autowired
    private SpecialistService specialistService;
    @Autowired
    private GeoPositionService geoPositionService;
    @Autowired
    private CashPointService cashPointService;

    @PostConstruct
    public void init(){
        System.out.println("start fill db after run app");
        fillCategories();
        fillSpecialists();
        fillCashPoints();
    }

    private void fillCategories() {
        IssueCategory hungry = new IssueCategory();
        hungry.setName("Банкомат съел карту");

        IssueCategory crash = new IssueCategory();
        crash.setName("Банкомат не работает");

        IssueCategory category = new IssueCategory();
        category.setName("Банкомат");
        category.setMainMenu(true);
        category.getChildren().add(crash);
        category.getChildren().add(hungry);
        issueCategoryService.addCategory(category);

        IssueCategory skolzko = new IssueCategory();
        skolzko.setName("Проблема в помещении");

        IssueCategory slowpoke = new IssueCategory();
        slowpoke.setName("Медленное обслуживание");

        IssueCategory ham = new IssueCategory();
        ham.setName("Некачественное обслуживание");

        IssueCategory vsp = new IssueCategory();
        vsp.setName("Обслуживание в филиале");
        vsp.setMainMenu(true);
        vsp.getChildren().add(slowpoke);
        vsp.getChildren().add(ham);
        vsp.getChildren().add(skolzko);
        issueCategoryService.addCategory(vsp);
    }

    private void fillCashPoints() {
        CashPoint cashPoint = new CashPoint();
        GeoPosition geoPosition = new GeoPosition();
        cashPoint.setShortAddress("Пресненская наб., 2");
        cashPoint.setAddress("Пресненская наб., 2");
        geoPosition.setLatitude(55.749137F);
        geoPosition.setLongitude(37.537456F);
        cashPoint.setGeoPosition(geoPosition);
        geoPositionService.add(geoPosition);
        cashPointService.add(cashPoint);
        cashPoint = new CashPoint();
        geoPosition = new GeoPosition();
        cashPoint.setShortAddress("Соймоновский пр-д, 5");
        cashPoint.setAddress("Соймоновский пр-д, 5");
        geoPosition.setLatitude(55.750496F);
        geoPosition.setLongitude(37.539387F);
        cashPoint.setGeoPosition(geoPosition);
        geoPositionService.add(geoPosition);
        cashPointService.add(cashPoint);
        cashPoint = new CashPoint();
        geoPosition = new GeoPosition();
        cashPoint.setShortAddress("Шмитовский пр-д, 16 стр.1");
        cashPoint.setAddress("Шмитовский пр-д, 16 стр.1");
        geoPosition.setLatitude(55.758471F);
        geoPosition.setLongitude(37.549409F);
        cashPoint.setGeoPosition(geoPosition);
        geoPositionService.add(geoPosition);
        cashPointService.add(cashPoint);
        cashPoint = new CashPoint();
        geoPosition = new GeoPosition();
        cashPoint.setShortAddress("Краснопресненская наб., 2, строение 1");
        cashPoint.setAddress("Краснопресненская наб., 2, строение 1");
        geoPosition.setLatitude(55.755436F);
        geoPosition.setLongitude(37.573615F);
        cashPoint.setGeoPosition(geoPosition);
        geoPositionService.add(geoPosition);
        cashPointService.add(cashPoint);

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
