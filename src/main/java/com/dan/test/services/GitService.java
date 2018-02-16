package com.dan.test.services;

import com.dan.test.domain.Change;
import com.dan.test.domain.GitContent;
import com.dan.test.domain.Pull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Slf4j
public class GitService {

    private RestTemplate restTemplate;

    @Autowired
    public GitService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Pull> getPulls(String pullsUrl, String accessToken) {
        return gitRequest(pullsUrl, accessToken, new ParameterizedTypeReference<List<Pull>>() {
        });
    }

    public List<Change> getChanges(String changesUrl, String accessToken) {
        return gitRequest(changesUrl, accessToken, new ParameterizedTypeReference<List<Change>>() {
        });
    }

    public List<GitContent> getContent(String contentsUrl, String accessToken) {
        return gitRequest(contentsUrl, accessToken, new ParameterizedTypeReference<List<GitContent>>() {
        });
    }

    public String getFile(String fileUrl, String accessToken) {
        return gitRequest(fileUrl, accessToken, String.class);
    }

    private <T> T gitRequest(String url, String accessToken, Class<T> responseType) {
        log.info("gitRequest url={}, accessToken={}, responseType={}", url, accessToken, responseType);
        T response = restTemplate.getForObject(appendTokenToUrl(url, accessToken), responseType);
        log.info("gitRequest response={}", response);
        return response;
    }

    private <T> List<T> gitRequest(String url, String accessToken, ParameterizedTypeReference<List<T>> responseType) {
        log.info("gitRequest url={}, accessToken={}, responseType={}", url, accessToken, responseType);
        List<T> response = restTemplate.exchange(appendTokenToUrl(url, accessToken), HttpMethod.GET, null, responseType).getBody();
        log.info("gitRequest response={}", response);
        return response;
    }

    private String appendTokenToUrl(String url, String token) {
        return String.format("%s%saccess_token=%s", url, url.contains("?") ? "&" : "?", token);
    }
}
