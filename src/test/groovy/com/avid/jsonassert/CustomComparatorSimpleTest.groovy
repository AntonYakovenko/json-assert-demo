package com.avid.jsonassert

import org.skyscreamer.jsonassert.JSONAssert
import org.skyscreamer.jsonassert.JSONCompareMode
import org.skyscreamer.jsonassert.RegularExpressionValueMatcher
import org.skyscreamer.jsonassert.ValueMatcher
import org.skyscreamer.jsonassert.comparator.CustomComparator
import org.skyscreamer.jsonassert.comparator.JSONComparator
import spock.lang.Specification

import static org.skyscreamer.jsonassert.Customization.customization

class CustomComparatorSimpleTest extends Specification {

    // language=JSON
    private static final String expectedJson = """
                    {
                      "code":  400,
                      "message":  "Bad Request",
                      "details": "Validation failure"
                    }"""
    // language=JSON
    private static final String actualJson = """
                    {
                      "code":  400,
                      "message":  "Bad Request",
                      "details": "Validation failure: missing required property"
                    }"""

    def "anonymous value matcher example"() {
        given:
        JSONComparator cmp = new CustomComparator(JSONCompareMode.STRICT,
                customization("details", new ValueMatcher<String>() {
                    @Override
                    boolean equal(String actual, String expected) {
                        actual.startsWith(expected)
                    }
                }))
        expect:
        JSONAssert.assertEquals(expectedJson, actualJson, cmp)
    }

    def "closure value matcher example"() {
        given:
        JSONComparator cmp = new CustomComparator(JSONCompareMode.STRICT,
                customization("details", { String actual, String expected -> actual.startsWith(expected) }))
        expect:
        JSONAssert.assertEquals(expectedJson, actualJson, cmp)
    }

    def "regular expression value matcher example"() {
        given:
        JSONComparator cmp = new CustomComparator(JSONCompareMode.STRICT,
                customization("details", // language=regExp
                        new RegularExpressionValueMatcher("Validation failure:.*")))

        expect:
        JSONAssert.assertEquals(expectedJson, actualJson, cmp)
    }

    def "ignore property value matcher example"() {
        given:
        JSONComparator cmp = new CustomComparator(JSONCompareMode.STRICT,
                customization("details", { actual, expected -> true }))

        expect:
        JSONAssert.assertEquals(expectedJson, actualJson, cmp)
    }
}
