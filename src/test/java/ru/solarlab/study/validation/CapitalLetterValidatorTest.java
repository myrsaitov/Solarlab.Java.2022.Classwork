package ru.solarlab.study.validation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CapitalLetterValidatorTest {

    private final CapitalLetterValidator validator = new CapitalLetterValidator();

    @Test
    @DisplayName("Null string should be valid")
    void testNullString() {
        // arrange
        final String value = null;
        //act
        final boolean valid = validator.isValid(value, null);
        //assert
        assertTrue(valid);
    }

    @Test
    @DisplayName("Empty string should be valid")
    void testEmptyString() {
        final String value = "";
        final boolean valid = validator.isValid(value, null);
        assertTrue(valid);
    }

    @Test
    @DisplayName("Capital letter string should be valid")
    void testCapitalLetterString() {
        final String value = "String";
        final boolean valid = validator.isValid(value, null);
        assertTrue(valid);
    }

    @Test
    @DisplayName("Small letter string should be invalid")
    void testString() {
        final String value = "string";
        final boolean valid = validator.isValid(value, null);
        assertFalse(valid);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "String"})
    @NullSource
    @DisplayName("Parameterized test")
    void parameterizedTest(String value) {
        final boolean valid = validator.isValid(value, null);
        assertTrue(valid);
    }
}