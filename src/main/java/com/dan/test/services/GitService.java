package com.dan.test.services;

import com.dan.test.domain.GitContent;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class GitService {

    private RestTemplate restTemplate;

    @Autowired
    public GitService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
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
