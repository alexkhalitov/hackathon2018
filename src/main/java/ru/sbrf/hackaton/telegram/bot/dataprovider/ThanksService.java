package ru.sbrf.hackaton.telegram.bot.dataprovider;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sbrf.hackaton.telegram.bot.model.Thanks;

import java.util.ArrayList;
import java.util.List;

@Service
public class ThanksService {
	
	@Autowired
    private ThanksRepository thanksRepository;
	
	public List<Thanks> getAll() {
		List<Thanks> thankss = new ArrayList<Thanks>();
		thanksRepository.findAll().forEach(thankss::add);
		return thankss;
	}
	
	public Thanks get(Long id) {
		return thanksRepository.findOne(id);
	}
	
	public void add(Thanks thanks) {
		thanksRepository.save(thanks);
	}
	
	public void update(Thanks thanks) {
		thanksRepository.save(thanks);
	}
	
	public void delete(Long id) {
		thanksRepository.delete(id);
	}
}
