package com.dan.test.domain;

import lombok.Data;

@Data
public class Contract {
    private String version;
    private boolean published;
    private String content;
}