package com.dan.test.domain;

import lombok.Data;

import java.util.List;

@Data
public class ContractList {
    private String name;
    private List<Contract> contracts;
}
