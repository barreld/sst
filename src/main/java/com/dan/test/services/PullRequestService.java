package com.dan.test.services;

import com.dan.test.domain.Change;
import com.dan.test.domain.FileChange;
import com.dan.test.domain.Pull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PullRequestService {
    private String pullsUrl;
    private GitService gitService;
    private Map<String, List<FileChange>> fileChangeMap;

    // Total HACK for POC
    private boolean doneYet = false;

    @Autowired
    public PullRequestService(GitService gitService, @Value("${pulls.url}") String pullsUrl) {
        this.pullsUrl = pullsUrl;
        this.gitService = gitService;
        fileChangeMap = new HashMap<>();
    }

    public List<FileChange> getFileChanges(String accessToken, String fileName) {
        if (!doneYet) {
            List<Pull> pulls = gitService.getPulls(pullsUrl + "?state=open", accessToken);

            for (Pull pull : pulls) {
                List<Change> changes = gitService.getChanges(pullsUrl + "/" + pull.getNumber() + "/files", accessToken);
                for (Change change : changes) {
                    fileChangeMap.putIfAbsent(change.getFilename(), new ArrayList<>());
                    fileChangeMap.get(change.getFilename()).add(new FileChange(pull.getUser().getLogin(), pull.getUser().getAvatar_url(), pull.getTitle(), pull.getHtml_url()));
                }
            }
            doneYet = true;
        }

        for (String key : fileChangeMap.keySet()) {
            if (key.contains(fileName)) {
                return fileChangeMap.get(key);
            }
        }

        return null;
    }
}
