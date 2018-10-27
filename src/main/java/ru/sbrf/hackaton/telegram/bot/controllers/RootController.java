package ru.sbrf.hackaton.telegram.bot.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.sbrf.hackaton.telegram.bot.dataprovider.IssueCategoryService;
import ru.sbrf.hackaton.telegram.bot.dataprovider.SpecialistService;
import ru.sbrf.hackaton.telegram.bot.model.IssueCategory;
import ru.sbrf.hackaton.telegram.bot.model.Specialist;

import java.util.List;

@RestController
public class RootController {
	private static final Logger LOGGER = LoggerFactory.getLogger(RootController.class);
	
	@Autowired
    private IssueCategoryService issueCategoryService;
	@Autowired
	private SpecialistService specialistService;

	@RequestMapping("/categories")
	public List<IssueCategory> getAllCategorys() {
		LOGGER.debug("I am in getAllCategorys");
		return issueCategoryService.getAllCategorys();
	}

	
	@RequestMapping("/categories/{id}")
	public IssueCategory getCategorys(@PathVariable long id) {

		return issueCategoryService.getCategory(id);
	}
	
	@RequestMapping(method = RequestMethod.POST, value ="/categories")
	public void addCategory(@RequestBody IssueCategory category) {

	}
	
	@RequestMapping(method = RequestMethod.PUT, value ="/categories/{id}")
	public void updateCategory(@RequestBody IssueCategory category, @PathVariable long id) {
		LOGGER.debug("I am in updateCategory");
		issueCategoryService.updateCategory(id, category);
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value ="/categories/{id}")
	public void deleteCategory(@PathVariable long id) {
		issueCategoryService.deleteCategory(id);
	}


	@RequestMapping("/specialists")
	public List<Specialist> getAllSpecialists() {
		LOGGER.debug("I am in getAllSpecialists");
		return specialistService.getAll();
	}

	@RequestMapping("/specialists/{id}")
	public Specialist getSpecialists(@PathVariable long id) {

		return specialistService.get(id);
	}
	
	@RequestMapping(method = RequestMethod.POST, value ="/specialists")
	public void addSpecialist(@RequestBody Specialist specialist) {

	}
	
	@RequestMapping(method = RequestMethod.PUT, value ="/specialists/{id}")
	public void updateSpecialist(@RequestBody Specialist specialist, @PathVariable long id) {
		LOGGER.debug("I am in updateSpecialist");
		specialistService.update(specialist);
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value ="/specialists/{id}")
	public void deleteSpecialist(@PathVariable long id) {
		specialistService.delete(id);
	}
}