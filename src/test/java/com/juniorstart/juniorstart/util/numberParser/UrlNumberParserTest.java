package com.juniorstart.juniorstart.util.numberParser;

import com.juniorstart.juniorstart.exception.AgeSpecifierNotFoundException;

import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class UrlNumberParserTest {

    @Test
    public void passingEmptyStringShouldThrowAgeSpecifierNotFoundException() {
        String number = "";
        assertThrows(AgeSpecifierNotFoundException.class,  () -> UrlNumberParser.parse(number));
    }

    @ParameterizedTest
    @MethodSource("createUrlStrings")
    public void passingValidStringShouldReturnParserResponse(String argument, NumberSpecifier expectedSpecifier, Integer expectedNumber) {
        UrlNumberParser.UrlNumberParserResponse<Integer> response = UrlNumberParser.parse(argument);

        assertEquals(expectedSpecifier, response.getNumberSpecifier());
        assertEquals(expectedNumber, response.getNumber());
    }

    private static Stream<Arguments> createUrlStrings() {
        return Stream.of(
                Arguments.of("4", NumberSpecifier.EQUAL, 4),
                Arguments.of("eq5", NumberSpecifier.EQUAL, 5),
                Arguments.of("5", NumberSpecifier.EQUAL, 5),
                Arguments.of("05", NumberSpecifier.EQUAL, 5),
                Arguments.of("neq50", NumberSpecifier.NOT_EQUAL, 50),
                Arguments.of("gt65", NumberSpecifier.GRATER, 65),
                Arguments.of("gte74", NumberSpecifier.GRATER_EQUAL, 74),
                Arguments.of("lt45", NumberSpecifier.LOWER, 45),
                Arguments.of("lte145", NumberSpecifier.LOWER_EQUAL, 145)
        );
    }
}