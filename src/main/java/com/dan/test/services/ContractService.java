package com.dan.test.services;

import com.dan.test.domain.AvailableContract;
import com.dan.test.domain.Contract;
import com.dan.test.domain.ContractList;
import com.dan.test.domain.GitContent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public List<AvailableContract> getAvailableMicroserviceContracts(String accessToken) {
        List<GitContent> directories = gitService.getContent(contractsUrl, accessToken);
        log.info("{}", directories);

        return directories
                .stream()
                .filter(d -> "dir".equals(d.getType()))
                .map(d -> new AvailableContract(d.getName(), d.getUrl()))
                .collect(Collectors.toList());
    }

    public ContractList getContractList(String accessToken, String url) {
        List<GitContent> contracts = gitService.getContent(url, accessToken);

        ContractList contractList = new ContractList();
        contractList.setName(contracts.get(0).getName());

        contractList.setContracts(
                contracts
                        .stream()
                        .map(c -> Contract
                                .builder()
                                .version(extractVersion(c.getName()))
                                .published(isPublished(c.getName()))
                                .downloadUrl(c.getDownload_url())
                                .build())
                        .collect(Collectors.toList()));

        return contractList;
    }

    private String extractVersion(String name) {
        return name.replaceAll("-UNPUBLISHED", "").replaceAll(".yml", "");
    }

    private boolean isPublished(String name) {
        return !name.contains("-UNPUBLISHED");
    }
}
