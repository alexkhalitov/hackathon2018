package ru.sbrf.hackaton.telegram.bot.dataprovider;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sbrf.hackaton.telegram.bot.model.IssueCategory;

import java.util.ArrayList;
import java.util.List;

@Service
public class IssueCategoryService {
	
	@Autowired
	private IssueCategoryRepository issueCategoryRepository;
	
	/*private List<IssueCategory> categorys = new ArrayList<>(Arrays.asList(
			new IssueCategory("Spring", "Spring Framework", "Spring Framework Description"),
			new IssueCategory("Java", "Java Programming", "Java Programming Description"),
			new IssueCategory("JavaScript", "JavaScript Framework", "JavaScript Framework Description")			
			));*/
	
	public List<IssueCategory> getAllCategorys() {
		List<IssueCategory> categorys = new ArrayList<IssueCategory>();
		issueCategoryRepository.findAll().forEach(categorys::add);
		return categorys;
	}
	
	public IssueCategory getCategory(long id) {
		return issueCategoryRepository.findOne(id);
	}
	
	public void addCategory(IssueCategory category) {
		issueCategoryRepository.save(category);
	}
	
	public void updateCategory(long id, IssueCategory category) {
		issueCategoryRepository.save(category);
	}
	
	public void deleteCategory(long id) {
		issueCategoryRepository.delete(id);
	}
	
	
	/*public IssueCategory getCategory(long id) {
		return categorys.stream().filter(t -> t.getId().equals(id)).findFirst().get();
	}
	
	public void addCategory(IssueCategory category) {
		categorys.add(category);
	}

	public void updateCategory(long id, IssueCategory category) {

		for(int i = 0; i < categorys.size(); i++) {
			
			IssueCategory t = categorys.get(i);
			if(t.getId().equals(id)) {
				System.out.println("ID is -> " + t.getId());
				categorys.set(i, category);
				return;
			}
		}
		
	}

	public void deleteCategory(long id) {
		categorys.removeIf(t -> t.getId().equals(id));
		
	}*/

}
