package ru.sbrf.hackaton.telegram.bot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.sbrf.hackaton.telegram.bot.dataprovider.IssueService;
import ru.sbrf.hackaton.telegram.bot.dataprovider.ThanksService;
import ru.sbrf.hackaton.telegram.bot.model.GeoPosition;
import ru.sbrf.hackaton.telegram.bot.model.Issue;
import ru.sbrf.hackaton.telegram.bot.model.Thanks;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MapController {

    @Autowired
    IssueService issueService;
    @Autowired
    ThanksService thanksService;

    @GetMapping("/dataMaps")
    public String dataMaps(@RequestParam(name="geoPositions", required=false, defaultValue="World") String name, Model model) {
        List<Issue> allCategorys = issueService.getAll();
        List<MapDataDTO> geoPositions = new ArrayList<>();
        for(Issue issueCategory:allCategorys){
            MapDataDTO mapDataDTO = new MapDataDTO();
            mapDataDTO.setLatitude(issueCategory.getGeoPosition().getLatitude());
            mapDataDTO.setLongitude(issueCategory.getGeoPosition().getLongitude());
            geoPositions.add(mapDataDTO);
        }
        model.addAttribute("geoPositions", geoPositions);
        return "dataMaps";
    }

    @GetMapping("/dataMapsThanks")
    public String dataMapsThanks(@RequestParam(name="geoPositions", required=false, defaultValue="World") String name, Model model) {
        List<Thanks> thanksList = thanksService.getAll();
        List<MapDataDTO> geoPositions = new ArrayList<>();
        for(Thanks thanks:thanksList){
            MapDataDTO mapDataDTO = new MapDataDTO();
            mapDataDTO.setLatitude(thanks.getPosition().getLatitude());
            mapDataDTO.setLongitude(thanks.getPosition().getLongitude());
            mapDataDTO.setMessage(thanks.getMessage());
            geoPositions.add(mapDataDTO);
        }
        model.addAttribute("geoPositions", geoPositions);
        return "dataMapsThanks";
    }
}
