package com.juniorstart.juniorstart.util.numberParser;

import com.juniorstart.juniorstart.exception.AgeSpecifierNotFoundException;
import static com.juniorstart.juniorstart.util.numberParser.NumberSpecifier.*;
import lombok.Getter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlNumberParser {
    public static UrlNumberParserResponse<Integer> parse(String var) {
        Pattern pattern = Pattern.compile("^(eq|neq|gt|gte|lt|lte|)([0-9]+)$");
        Matcher matcher = pattern.matcher(var);
        if (!matcher.matches()) {
            throw new AgeSpecifierNotFoundException();
        }

        Integer number = Integer.parseInt(matcher.group(2));

        switch (matcher.group(1)) {
            case "eq":
            case "":
                return new UrlNumberParserResponse<>(NumberSpecifier.EQUAL, number);
            case "neq":
                return new UrlNumberParserResponse<>(NumberSpecifier.NOT_EQUAL, number);
            case "gt":
                return new UrlNumberParserResponse<>(NumberSpecifier.GRATER, number);
            case "gte":
                return new UrlNumberParserResponse<>(NumberSpecifier.GRATER_EQUAL, number);
            case "lt":
                return new UrlNumberParserResponse<>(NumberSpecifier.LOWER, number);
            case "lte":
                return new UrlNumberParserResponse<>(NumberSpecifier.LOWER_EQUAL, number);
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
