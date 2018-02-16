package com.dan.test.services;

import com.dan.test.domain.Contract;
import com.dan.test.domain.ContractList;
import com.dan.test.domain.GitContent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ContractService {

    private String contractsUrl;
    private GitService gitService;

    @Autowired
    public ContractService(@Value("${contracts.url}") String contractsUrl, GitService gitService) {
        this.contractsUrl = contractsUrl;
        this.gitService = gitService;
    }

    public String getContractText(String accessToken, String downloadUrl) {
        return gitService.getFile(downloadUrl, accessToken);
    }

    public List<ContractList> getAllContracts(String accessToken) {
        List<GitContent> directories = gitService.getContent(contractsUrl, accessToken);
        log.info("{}", directories);

        Map<String, List<GitContent>> contractDirectories = new HashMap<>();

        for (GitContent directory : directories) {
            if ("dir".equals(directory.getType())) {
                contractDirectories.put(directory.getName(), gitService.getContent(directory.getUrl(), accessToken));
            }
        }

        return contractDirectories
                .entrySet()
                .stream()
                .map(cd -> ContractList
                        .builder()
                        .name(cd.getKey())
                        .contracts(cd.getValue()
                                .stream()
                                .map(gc -> Contract
                                        .builder()
                                        .version(extractVersion(gc.getName()))
                                        .published(isPublished(gc.getName()))
                                        .downloadUrl(gc.getDownload_url())
                                        .build())
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }

    private String extractVersion(String name) {
        return name.replaceAll("-UNPUBLISHED", "").replaceAll(".yml", "");
    }

    private boolean isPublished(String name) {
        return !name.contains("-UNPUBLISHED");
    }
}
