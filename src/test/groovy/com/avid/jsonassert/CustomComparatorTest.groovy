package com.avid.jsonassert

import org.skyscreamer.jsonassert.JSONAssert
import org.skyscreamer.jsonassert.JSONCompareMode
import org.skyscreamer.jsonassert.RegularExpressionValueMatcher
import org.skyscreamer.jsonassert.ValueMatcher
import org.skyscreamer.jsonassert.comparator.CustomComparator
import org.skyscreamer.jsonassert.comparator.JSONComparator
import spock.lang.Specification

import static org.skyscreamer.jsonassert.Customization.customization

class CustomComparatorTest extends Specification {

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
                      "details": "Validation failure"
                    }"""
    // language=JSON
    private static final String expectedJsonTemplate = """
                    {
                      "code":  400,
                      "message":  "Bad Request",
                      "details": "_"
                    }"""

    def "anonymous value matcher example 1 error message demo"() {
        given:
        JSONComparator cmp = new CustomComparator(JSONCompareMode.STRICT,
                customization("details", new ValueMatcher<Object>() {
                    @Override
                    boolean equal(Object actual, Object expected) {
                        actual == "I expect this details!"
                    }
                }))
        expect:
        JSONAssert.assertEquals(expectedJsonTemplate, actualJson, cmp)
    }

    def "anonymous value matcher example 2 error message demo"() {
        given:
        JSONComparator cmp = new CustomComparator(JSONCompareMode.STRICT,
                customization("details", { actual, expected -> actual == "I expect this details!" }))
        expect:
        JSONAssert.assertEquals(expectedJsonTemplate, actualJson, cmp)
    }

    def "regular expression value matcher error message demo"() {
        given:
        JSONComparator cmp = new CustomComparator(JSONCompareMode.STRICT,
                customization("details", new RegularExpressionValueMatcher("I expect this details!")))

        expect:
        JSONAssert.assertEquals(expectedJsonTemplate, actualJson, cmp)
    }

    def "constant value matcher error message demo"() {
        given:
        JSONComparator cmp = new CustomComparator(JSONCompareMode.STRICT,
                customization("details", new ConstantValueMatcher("I expect this details!")))

        expect:
        JSONAssert.assertEquals(expectedJsonTemplate, actualJson, cmp)
    }
}
