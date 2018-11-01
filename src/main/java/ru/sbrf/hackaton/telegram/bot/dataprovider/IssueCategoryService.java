package ru.sbrf.hackaton.telegram.bot.dataprovider;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sbrf.hackaton.telegram.bot.model.IssueCategory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

	public IssueCategory findParentFor(IssueCategory issueCategory) {
		Optional<IssueCategory> res = getAllCategorys().stream().filter(cat ->
			cat.getChildren().stream().anyMatch(child -> child.getId().equals(issueCategory.getId()))
		).findAny();
		return res.orElse(null);
	}

	/**
	 * Найти категорию по имени
	 *
	 * @param name
	 * @return
	 */
	public IssueCategory getIssueCategoryByName(String name) {
		return getAllCategorys()
				.stream()
				.filter(p -> p.getName() != null && p.getName().toLowerCase().equals(name.toLowerCase()))
				.findAny()
				.orElseThrow(IllegalArgumentException::new);
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
