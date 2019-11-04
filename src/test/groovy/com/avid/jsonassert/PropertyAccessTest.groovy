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
    String actual = '''{
                "first": "actual", 
                "second": 1
            }'''
    // language=JSON
    String expected = '''{
                "first": "expected", 
                "second": 1
            }'''
    // language=JSON
    String deepActual = '''{
                "outer": {
                    "inner": {
                        "value": "actual",
                        "otherValue": "foo"
                    }
                }
            }'''
    // language=JSON
    String deepExpected = '''{
                "outer": {
                    "inner": {
                        "value": "expected",
                        "otherValue": "foo"
                    }
                }
            }'''
    // language=JSON
    String simpleWildcardActual = '''{
              "foo": {
                "bar1": {
                  "baz": "actual"
                },
                "bar2": {
                  "baz": "actual"
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
    String deepWildcardActual = '''{
              "root": {
                "baz": "actual",
                "foo": {
                  "baz": "actual",
                  "bar": {
                    "baz": "actual"
                  }
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
    String rootDeepWildcardActual = '''{
              "baz": "actual",
              "root": {
                "baz": "actual",
                "foo": {
                  "baz": "actual",
                  "bar": {
                    "baz": "actual"
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
        return actual == "actual" && expected == "expected"
    }

    def "when path matches in customization then call custom matcher"() {
        given:
        JSONComparator jsonCmp = new CustomComparator(JSONCompareMode.STRICT,
                customization("first", comparator))
        when:
        JSONCompareResult result = compareJSON(expected, actual, jsonCmp)
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
        JSONCompareResult result = compareJSON(deepExpected, deepActual, jsonCmp)
        then:
        result.passed()
        and:
        1 == comparatorCallCount
    }

    def "when simple wildcard path matches call custom matcher"() {
        JSONComparator jsonCmp = new CustomComparator(JSONCompareMode.STRICT,
                customization("foo.*.baz", comparator))
        when:
        JSONCompareResult result = compareJSON(simpleWildcardExpected, simpleWildcardActual, jsonCmp)
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
        JSONCompareResult result = compareJSON(deepWildcardExpected, deepWildcardActual, jsonCmp)
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
        JSONCompareResult result = compareJSON(rootDeepWildcardExpected, rootDeepWildcardActual, jsonCmp)
        then:
        result.passed()
        and:
        4 == comparatorCallCount
    }
}
