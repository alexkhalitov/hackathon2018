package ru.sbrf.hackaton.telegram.bot.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.sbrf.hackaton.telegram.bot.dataprovider.IssueCategoryService;
import ru.sbrf.hackaton.telegram.bot.model.IssueCategory;

import java.util.List;

@RestController
public class RootController {
	
	@Autowired
    IssueCategoryService issueCategoryService;
	final Logger logger = LoggerFactory.getLogger(RootController.class);

	@RequestMapping("/categories")
	public List<IssueCategory> getAllCategorys() {
		logger.debug("I am in getAllCategorys");
		return issueCategoryService.getAllCategorys();
	}
	
	/*@RequestMapping("/categories/{foo}")
	public IssueCategory getCategorys(@PathVariable("foo") String id) {
		
		return categoryService.getCategory(id);
	}*/
	
	@RequestMapping("/categories/{id}")
	public IssueCategory getCategorys(@PathVariable String id) {

		return issueCategoryService.getCategory(id);
	}
	
	@RequestMapping(method = RequestMethod.POST, value ="/categories")
	public void addCategory(@RequestBody IssueCategory category) {

	}
	
	@RequestMapping(method = RequestMethod.PUT, value ="/categories/{id}")
	public void updateCategory(@RequestBody IssueCategory category, @PathVariable String id) {
		logger.debug("I am in updateCategory");
		issueCategoryService.updateCategory(id, category);
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value ="/categories/{id}")
	public void deleteCategory(@PathVariable String id) {
		issueCategoryService.deleteCategory(id);
	}

}
