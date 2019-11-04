package com.avid.jsonassert


import org.skyscreamer.jsonassert.ValueMatcher
import org.skyscreamer.jsonassert.ValueMatcherException

final class ConstantValueMatcher<T> implements ValueMatcher<T> {

    private final T expected

    ConstantValueMatcher(T expected) {
        this.expected = expected
    }

    @Override
    boolean equal(T actual, T ignoredExpected) {
        if (actual != expected) {
            throw new ValueMatcherException("Unexpected value", expected.toString(), actual.toString())
        }
        return true
    }
}
