package ru.sbrf.hackaton.telegram.bot.dataprovider;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sbrf.hackaton.telegram.bot.model.Category;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {
	
	@Autowired
    CategoryRepository categoryRepository;
	
	/*private List<Category> categorys = new ArrayList<>(Arrays.asList(
			new Category("Spring", "Spring Framework", "Spring Framework Description"),
			new Category("Java", "Java Programming", "Java Programming Description"),
			new Category("JavaScript", "JavaScript Framework", "JavaScript Framework Description")			
			));*/
	
	public List<Category> getAllCategorys() {
		List<Category> categorys = new ArrayList<Category>();
		categoryRepository.findAll().forEach(categorys::add);
		return categorys;
	}
	
	public Category getCategory(String id) {
		return categoryRepository.findOne(id);
	}
	
	public void addCategory(Category category) {
		categoryRepository.save(category);
	}
	
	public void updateCategory(String id, Category category) {
		categoryRepository.save(category);
	}
	
	public void deleteCategory(String id) {
		categoryRepository.delete(id);
	}
	
	
	/*public Category getCategory(String id) {
		return categorys.stream().filter(t -> t.getId().equals(id)).findFirst().get();
	}
	
	public void addCategory(Category category) {
		categorys.add(category);
	}

	public void updateCategory(String id, Category category) {

		for(int i = 0; i < categorys.size(); i++) {
			
			Category t = categorys.get(i);
			if(t.getId().equals(id)) {
				System.out.println("ID is -> " + t.getId());
				categorys.set(i, category);
				return;
			}
		}
		
	}

	public void deleteCategory(String id) {
		categorys.removeIf(t -> t.getId().equals(id));
		
	}*/

}
