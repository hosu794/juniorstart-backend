package com.juniorstart.juniorstart.parser;

import lombok.Getter;

public enum NumberSpecifier {
    EQUAL("eq"),
    NOT_EQUAL("neq"),
    GRATER("gt"),
    GRATER_EQUAL("gte"),
    LOWER("lt"),
    LOWER_EQUAL("lte"),
    BETWEEN("bt");

    @Getter
    private final String value;

    NumberSpecifier(String value) {
        this.value = value;
    }
}
