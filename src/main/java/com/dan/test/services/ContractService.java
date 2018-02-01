package com.dan.test.services;

import com.dan.test.domain.Contract;
import com.dan.test.domain.ContractList;
import com.dan.test.domain.GitContent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public List<ContractList> getAllContracts(String accessToken) {
        List<GitContent> directories = gitService.getContent(contractsUrl, accessToken);
        log.info("{}", directories);

        Map<String, List<GitContent>> contractDirectories = new HashMap<>();

        for (GitContent directory : directories) {
            if ("dir".equals(directory.getType())) {
                contractDirectories.put(directory.getName(), gitService.getContent(directory.getUrl(), accessToken));
            }
        }

        List<ContractList> contractLists = new ArrayList<>();

        for (Map.Entry<String, List<GitContent>> contractDirectory : contractDirectories.entrySet()) {
            ContractList contractList = new ContractList();
            contractList.setName(contractDirectory.getKey());

            List<Contract> contracts = new ArrayList<>();
            for (GitContent gitContract : contractDirectory.getValue()) {
                Contract contract = new Contract();
                contract.setVersion(extractVersion(gitContract.getName()));
                contract.setPublished(isPublished(gitContract.getName()));

                String file = gitService.getFile(gitContract.getDownload_url(), accessToken);
                contract.setContent(file);

                contracts.add(contract);
            }

            contractList.setContracts(contracts);

            contractLists.add(contractList);
        }

        return contractLists;
    }

    private String extractVersion(String name) {
        return name.replaceAll("-UNPUBLISHED", "").replaceAll(".yml", "");
    }

    private boolean isPublished(String name) {
        return !name.contains("-UNPUBLISHED");
    }
}
