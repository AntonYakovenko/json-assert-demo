package com.avid.jsonassert

import org.skyscreamer.jsonassert.JSONAssert
import org.skyscreamer.jsonassert.JSONCompareMode
import spock.lang.Specification

class CompareModeTest extends Specification {

    // language=JSON
    private static final String expectedJson = '''
                    {
                      "string":  "expected",
                      "array": [ "one", "two" ]
                    }'''

    // language=JSON
    private static final String actualStrictJson = '''
                    {
                      "string":  "expected",
                      "array": [ "one", "two" ]
                    }'''
    // language=JSON
    private static final String actualStrictOrderJson = '''
                    {
                      "string":  "expected",
                      "array": [ "one", "two" ],
                      "custom": "property"
                    }'''
    // language=JSON
    private static final String actualNonExtensibleJson = '''
                    {
                      "string":  "expected",
                      "array": [ "two", "one" ]
                    }'''
    // language=JSON
    private static final String actualLenientJson = '''
                    {
                      "string":  "expected",
                      "array": [ "two", "one" ],
                      "custom": "property"
                    }'''

    def "STRICT mode assert demo"() {
        expect:
        JSONAssert.assertEquals(expectedJson, actualStrictJson, JSONCompareMode.STRICT)
    }

    def "STRICT_ORDER order mode assert demo"() {
        expect:
        JSONAssert.assertEquals(expectedJson, actualStrictOrderJson, JSONCompareMode.STRICT_ORDER)
    }

    def "NON_EXTENSIBLE mode assert demo"() {
        expect:
        JSONAssert.assertEquals(expectedJson, actualNonExtensibleJson, JSONCompareMode.NON_EXTENSIBLE)
    }

    def "LENIENT mode assert demo"() {
        expect:
        JSONAssert.assertEquals(expectedJson, actualLenientJson, JSONCompareMode.LENIENT)
    }
}
