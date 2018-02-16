package com.dan.test.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileChange {
    private String user;
    private String userPic;
    private String description;
    private String url;
}
