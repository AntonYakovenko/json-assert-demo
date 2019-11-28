package com.avid.jsonassert

import org.skyscreamer.jsonassert.JSONAssert
import org.skyscreamer.jsonassert.JSONCompareMode
import org.skyscreamer.jsonassert.RegularExpressionValueMatcher
import org.skyscreamer.jsonassert.ValueMatcher
import org.skyscreamer.jsonassert.comparator.CustomComparator
import org.skyscreamer.jsonassert.comparator.JSONComparator
import spock.lang.Specification

import static org.skyscreamer.jsonassert.Customization.customization

class CustomComparatorTemplateTest extends Specification {

    // language=JSON
    private static final String expectedJsonTemplate = """
                    {
                      "code":  400,
                      "message":  "Bad Request",
                      "details": "_"
                    }"""
    // language=JSON
    private static final String actualJson = """
                    {
                      "code":  400,
                      "message":  "Bad Request",
                      "details": "Validation failure"
                    }"""

    def "closure value matcher positive case"() {
        given:
        JSONComparator cmp = new CustomComparator(JSONCompareMode.STRICT,
                customization("details", { actual, expected -> actual == "Validation failure" }))
        expect:
        JSONAssert.assertEquals(expectedJsonTemplate, actualJson, cmp)
    }

    def "closure value matcher error message demo"() {
        given:
        JSONComparator cmp = new CustomComparator(JSONCompareMode.STRICT,
                customization("details", { actual, expected -> actual == "I expect this details!" }))
        expect:
        JSONAssert.assertEquals(expectedJsonTemplate, actualJson, cmp)
    }

    def "regular expression value matcher error message demo"() {
        given:
        JSONComparator cmp = new CustomComparator(JSONCompareMode.STRICT,
                customization("details", // language=RegExp
                        new RegularExpressionValueMatcher("I expect this details!")))

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
