package com.dan.test.domain;

import lombok.Data;

@Data
public class GitContent {
    private String name;
    private String path;
    private String url;
    private String download_url;
    private String type;
}


//"name": ".gitignore",
//        "path": ".gitignore",
//        "sha": "dd59fe608c023ce19036a97c5a5e8aaf2a286a3e",
//        "size": 280,
//        "url": "https://api.github.com/repos/barreld/contracts/contents/.gitignore?ref=master",
//        "html_url": "https://github.com/barreld/contracts/blob/master/.gitignore",
//        "git_url": "https://api.github.com/repos/barreld/contracts/git/blobs/dd59fe608c023ce19036a97c5a5e8aaf2a286a3e",
//        "download_url": "https://raw.githubusercontent.com/barreld/contracts/master/.gitignore",
//        "type": "file",
//        "_links": {
//        "self": "https://api.github.com/repos/barreld/contracts/contents/.gitignore?ref=master",
//        "git": "https://api.github.com/repos/barreld/contracts/git/blobs/dd59fe608c023ce19036a97c5a5e8aaf2a286a3e",
//        "html": "https://github.com/barreld/contracts/blob/master/.gitignore"
//        }