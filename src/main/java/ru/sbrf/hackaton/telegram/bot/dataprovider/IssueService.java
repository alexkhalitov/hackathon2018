package ru.sbrf.hackaton.telegram.bot.dataprovider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sbrf.hackaton.telegram.bot.model.Client;
import ru.sbrf.hackaton.telegram.bot.model.Issue;
import ru.sbrf.hackaton.telegram.bot.model.IssueStatus;

import java.util.ArrayList;
import java.util.List;

@Service
public class IssueService {
    @Autowired
    private IssueRepository issueRepository;
    @Autowired
    private ClientService clientService;

    public List<Issue> getAll() {
        List<Issue> issues = new ArrayList<Issue>();
        issueRepository.findAll().forEach(issues::add);
        return issues;
    }

    public Issue get(Long id) {
        return issueRepository.findOne(id);
    }

    public void add(Issue issue) {
        issueRepository.save(issue);
    }

    public void update(Issue issue) {
        issueRepository.save(issue);
    }

    public void delete(Long id) {
        issueRepository.delete(id);
    }

    public List<Issue> getByClientId(Long clientId) {
        List<Issue> issuesList = new ArrayList<>();
        for (Issue issue : issueRepository.findAll()) {
            if (clientId.equals(issue.getClient().getId())) {
                issuesList.add(issue);
            }
        }
        return issuesList;
    }

    public Issue createNewIssue(Client client) {
        Issue issue = new Issue();
        issue.setStatus(IssueStatus.NEW);
        issue.setClient(client);
        add(issue);
        client.getIssues().add(issue);
        clientService.update(client);
        return issue;
    }

}
