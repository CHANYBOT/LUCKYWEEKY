package io.ssafy.luckyweeky.common.implement;

public interface Converter<S, T> {
    T convert(S source);
}
