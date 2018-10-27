package ru.sbrf.hackaton.telegram.bot.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.sbrf.hackaton.telegram.bot.dataprovider.CategoryService;
import ru.sbrf.hackaton.telegram.bot.model.Category;

import java.util.List;

@RestController
public class RootController {
	
	@Autowired
	CategoryService categoryService;
	final Logger logger = LoggerFactory.getLogger(RootController.class);

	@RequestMapping("/categories")
	public List<Category> getAllCategorys() {
		logger.debug("I am in getAllCategorys");
		return categoryService.getAllCategorys();
	}
	
	/*@RequestMapping("/categories/{foo}")
	public Category getCategorys(@PathVariable("foo") String id) {
		
		return categoryService.getCategory(id);
	}*/
	
	@RequestMapping("/categories/{id}")
	public Category getCategorys(@PathVariable String id) {

		return categoryService.getCategory(id);
	}
	
	@RequestMapping(method = RequestMethod.POST, value ="/categories")
	public void addCategory(@RequestBody Category category) {

	}
	
	@RequestMapping(method = RequestMethod.PUT, value ="/categories/{id}")
	public void updateCategory(@RequestBody Category category, @PathVariable String id) {
		logger.debug("I am in updateCategory");
		categoryService.updateCategory(id, category);
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value ="/categories/{id}")
	public void deleteCategory(@PathVariable String id) {
		categoryService.deleteCategory(id);
	}

}
