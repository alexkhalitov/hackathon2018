package ru.sbrf.hackaton.telegram.bot.dataprovider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sbrf.hackaton.telegram.bot.model.GeoPosition;

import java.util.ArrayList;
import java.util.List;

@Service
public class GeoPositionService {
    @Autowired
    private GeoPositionRepository geoPositionRepository;

    public List<GeoPosition> getAll() {
        List<GeoPosition> geoPositions = new ArrayList<GeoPosition>();
        geoPositionRepository.findAll().forEach(geoPositions::add);
        return geoPositions;
    }

    public GeoPosition get(Long id) {
        return geoPositionRepository.findOne(id);
    }

    public void add(GeoPosition issue) {
        geoPositionRepository.save(issue);
    }

    public void update(GeoPosition issue) {
        geoPositionRepository.save(issue);
    }

    public void delete(Long id) {
        geoPositionRepository.delete(id);
    }
}
