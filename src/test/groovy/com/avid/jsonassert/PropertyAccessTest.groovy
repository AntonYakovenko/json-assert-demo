package com.avid.jsonassert


import org.skyscreamer.jsonassert.JSONCompareMode
import org.skyscreamer.jsonassert.JSONCompareResult
import org.skyscreamer.jsonassert.ValueMatcher
import org.skyscreamer.jsonassert.comparator.CustomComparator
import org.skyscreamer.jsonassert.comparator.JSONComparator
import spock.lang.Specification

import static org.skyscreamer.jsonassert.Customization.customization
import static org.skyscreamer.jsonassert.JSONCompare.compareJSON

class PropertyAccessTest extends Specification {

    // language=JSON
    String expected = '''{
                "first": "expected", 
                "second": 1
            }'''
    // language=JSON
    String deepExpected = '''{
                "outer": {
                    "inner": {
                        "value": "expected",
                        "otherValue": "expected"
                    }
                }
            }'''
    // language=JSON
    String simpleWildcardExpected = '''{
              "foo": {
                "bar1": {
                  "baz": "expected"
                },
                "bar2": {
                  "baz": "expected"
                }
              }
            }'''
    // language=JSON
    String deepWildcardExpected = '''{
              "root": {
                "baz": "expected",
                "foo": {
                  "baz": "expected",
                  "bar": {
                    "baz": "expected"
                  }
                }
              }
            }'''
    // language=JSON
    String rootDeepWildcardExpected = '''{
              "baz": "expected",
              "root": {
                "baz": "expected",
                "foo": {
                  "baz": "expected",
                  "bar": {
                    "baz": "expected"
                  }
                }
              }
            }'''

    int comparatorCallCount = 0
    ValueMatcher<Object> comparator = { actual, expected ->
        comparatorCallCount++
        return actual == expected
    }

    def "when path matches in customization then call custom matcher"() {
        given:
        JSONComparator jsonCmp = new CustomComparator(JSONCompareMode.STRICT,
                customization("first", comparator))
        when:
        JSONCompareResult result = compareJSON(expected, expected, jsonCmp)
        then:
        result.passed()
        and:
        1 == comparatorCallCount
    }

    def "when deep path matches call custom matcher"() {
        given:
        JSONComparator jsonCmp = new CustomComparator(JSONCompareMode.STRICT,
                customization("outer.inner.value", comparator))
        when:
        JSONCompareResult result = compareJSON(deepExpected, deepExpected, jsonCmp)
        then:
        result.passed()
        and:
        1 == comparatorCallCount
    }

    def "when simple wildcard path matches call custom matcher"() {
        JSONComparator jsonCmp = new CustomComparator(JSONCompareMode.STRICT,
                customization("foo.*.baz", comparator))
        when:
        JSONCompareResult result = compareJSON(simpleWildcardExpected, simpleWildcardExpected, jsonCmp)
        then:
        result.passed()
        and:
        2 == comparatorCallCount
    }

    def "when deep wildcard path matches call custom matcher"() {
        given:
        JSONComparator jsonCmp = new CustomComparator(JSONCompareMode.STRICT,
                customization("root.**.baz", comparator))
        when:
        JSONCompareResult result = compareJSON(deepWildcardExpected, deepWildcardExpected, jsonCmp)
        then:
        result.passed()
        and:
        3 == comparatorCallCount
    }

    def "when root deep wildcard path matches call custom matcher"() {
        given:
        JSONComparator jsonCmp = new CustomComparator(JSONCompareMode.STRICT,
                customization("**.baz", comparator))
        when:
        JSONCompareResult result = compareJSON(rootDeepWildcardExpected, rootDeepWildcardExpected, jsonCmp)
        then:
        result.passed()
        and:
        4 == comparatorCallCount
    }
}
