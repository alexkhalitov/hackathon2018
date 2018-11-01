package ru.sbrf.hackaton.telegram.bot.dataprovider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sbrf.hackaton.telegram.bot.model.CashPoint;
import ru.sbrf.hackaton.telegram.bot.model.Issue;
import ru.sbrf.hackaton.telegram.bot.model.IssueStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CashPointService {
    @Autowired
    private CashPointRepository cashPointRepository;

    public List<CashPoint> getAll() {
        List<CashPoint> cashPoints = new ArrayList<CashPoint>();
        cashPointRepository.findAll().forEach(cashPoints::add);
        return cashPoints;
    }

    public CashPoint get(Long id) {
        return cashPointRepository.findOne(id);
    }

    public void add(CashPoint issue) {
        cashPointRepository.save(issue);
    }

    public void update(CashPoint issue) {
        cashPointRepository.save(issue);
    }

    public void delete(Long id) {
        cashPointRepository.delete(id);
    }

}
