package ru.sbrf.hackaton.telegram.bot.dataprovider;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sbrf.hackaton.telegram.bot.model.Specialist;

import java.util.ArrayList;
import java.util.List;

@Service
public class SpecialistService {
	
	@Autowired
    private SpecialistRepository specialistRepository;
	
	public List<Specialist> getAll() {
		List<Specialist> specialists = new ArrayList<Specialist>();
		specialistRepository.findAll().forEach(specialists::add);
		return specialists;
	}
	
	public Specialist get(Long id) {
		return specialistRepository.findOne(id);
	}
	
	public void add(Specialist specialist) {
		specialistRepository.save(specialist);
	}
	
	public void update(Specialist specialist) {
		specialistRepository.save(specialist);
	}
	
	public void delete(Long id) {
		specialistRepository.delete(id);
	}

    public Specialist getByMobile(String mobile) {
        for(Specialist specialist : specialistRepository.findAll()) {
            if(specialist.getMobile().equals(mobile)) {
                return specialist;
            }
        }
        return null;
    }

    public Specialist getByChatId(long chatId) {
        for(Specialist specialist : specialistRepository.findAll()) {
            if(specialist.getChatId() != null && specialist.getChatId().equals(chatId)) {
                return specialist;
            }
        }
        return null;
    }
}
