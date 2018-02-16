package com.dan.test.domain;

import lombok.Data;

@Data
public class Pull {
    private Integer number;
    private User user;
    private String title;
    private String html_url;
}
