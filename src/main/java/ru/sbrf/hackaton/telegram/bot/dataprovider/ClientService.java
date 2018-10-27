package ru.sbrf.hackaton.telegram.bot.dataprovider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sbrf.hackaton.telegram.bot.model.Client;
import ru.sbrf.hackaton.telegram.bot.model.Issue;
import ru.sbrf.hackaton.telegram.bot.model.IssueStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClientService {
    @Autowired
    private ClientRepository clientRepository;

    public List<Client> getAll() {
        List<Client> clients = new ArrayList<Client>();
        clientRepository.findAll().forEach(clients::add);
        return clients;
    }

    public Client get(Long id) {
        return clientRepository.findOne(id);
    }

    public void add(Client issue) {
        clientRepository.save(issue);
    }

    public void update(Client issue) {
        clientRepository.save(issue);
    }

    public void delete(Long id) {
        clientRepository.delete(id);
    }

    public Client getByChatId(Long chatId) {
        for (Client client : clientRepository.findAll()) {
            if (chatId.equals(client.getChatId())) {
                return client;
            }
        }
        Client client = new Client();
        client.setChatId(chatId);
        add(client);
        return client;
    }

    public Issue getNewIssue(Client client) {
        Issue issue = null;
        Optional<Issue> issueOptional = client.getIssues().stream().filter(p -> IssueStatus.NEW.equals(p.getStatus())).findFirst();
        if (issueOptional.isPresent()) {
            issue = issueOptional.get();
        }
        return issue;

    }
}
