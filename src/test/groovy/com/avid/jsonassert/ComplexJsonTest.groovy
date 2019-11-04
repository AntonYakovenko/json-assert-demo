package com.avid.jsonassert

import org.json.JSONArray
import org.json.JSONObject
import org.skyscreamer.jsonassert.JSONAssert
import org.skyscreamer.jsonassert.JSONCompareMode
import org.skyscreamer.jsonassert.comparator.CustomComparator
import spock.lang.Specification
import spock.lang.Unroll

import static org.skyscreamer.jsonassert.Customization.customization

class ComplexJsonTest extends Specification {

    // language=JSON
    private static final String expectedJson = '''
                    {
                        "message": "Bad Request",
                        "errors": [ "field1", "field2" ],
                        "details": {
                            "code": "aa-error"
                        }
                    }'''
    // language=JSON
    private static final String fieldNullJson = '''
                    {
                        "message": null,
                        "errors": [ "field1", "field2" ],
                        "details": {
                            "code": "aa-error"
                        }
                    }'''
    // language=JSON
    private static final String fieldUpdatedJson = '''
                    {
                        "message": "Not Found",
                        "errors": [ "field1", "field2" ],
                        "details": {
                            "code": "aa-error"
                        }
                    }'''
    // language=JSON
    private static final String arrayEmptyJson = '''
                    {
                        "message": "Bad Request",
                        "errors": [],
                        "details": {
                            "code": "aa-error"
                        }
                    }'''
    // language=JSON
    private static final String arrayUpdatedJson = '''
                    {
                        "message": "Bad Request",
                        "errors": [ "new_1", "new_2" ],
                        "details": {
                            "code": "aa-error"
                        }
                    }'''
    // language=JSON
    private static final String nestedFieldUpdatedJson = '''
                    {
                        "message": "Bad Request",
                        "errors": [ "field1", "field2" ],
                        "details": {
                            "code": "new-error"
                        }
                    }'''

    @Unroll
    def "should update #expectedKey with #expectedValue"() {
        given:
        CustomComparator responseComparator = new CustomComparator(JSONCompareMode.STRICT,
                customization(expectedKey, new ConstantValueMatcher(expectedValue)))

        expect:
        JSONAssert.assertEquals(expectedJson, actualJson, responseComparator)

        where:
        actualJson             || expectedKey    | expectedValue
        fieldNullJson          || "message"      | JSONObject.NULL
        fieldUpdatedJson       || "message"      | "Not Found"
        arrayEmptyJson         || "errors"       | new JSONArray()
        arrayUpdatedJson       || "errors"       | new JSONArray(["new_1", "new_2"])
        nestedFieldUpdatedJson || "details.code" | "new-error"
    }
}
