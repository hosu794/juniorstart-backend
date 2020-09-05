package com.juniorstart.juniorstart.util;

import com.juniorstart.juniorstart.exception.AgeSpecifierNotFoundException;
import static com.juniorstart.juniorstart.util.NumberSpecifier.*;
import lombok.Getter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlNumberParser {
    public static UrlNumberParserResponse<Integer> parse(String var) {
        Pattern pattern = Pattern.compile("^(eq|neq|gt|gte|lt|lte|)([0-9])+$");
        Matcher matcher = pattern.matcher(var);
        if (!matcher.matches()) {
            throw new AgeSpecifierNotFoundException();
        }

        Integer number = Integer.parseInt(matcher.group(2));

        switch (matcher.group(1)) {
            case "eq":
            case "":
                return new UrlNumberParserResponse<>(EQUAL, number);
            case "neq":
                return new UrlNumberParserResponse<>(NOT_EQUAL, number);
            case "gt":
                return new UrlNumberParserResponse<>(GRATER, number);
            case "gte":
                return new UrlNumberParserResponse<>(GRATER_EQUAL, number);
            case "lt":
                return new UrlNumberParserResponse<>(LOWER, number);
            case "lte":
                return new UrlNumberParserResponse<>(LOWER_EQUAL, number);
            default:
                throw new AgeSpecifierNotFoundException();
        }
    }

    @Getter
    public static class UrlNumberParserResponse<T extends Number> {
        private final NumberSpecifier numberSpecifier;
        private final T number;

        public UrlNumberParserResponse(NumberSpecifier numberSpecifier, T number) {
            this.numberSpecifier = numberSpecifier;
            this.number = number;
        }
    }
}
