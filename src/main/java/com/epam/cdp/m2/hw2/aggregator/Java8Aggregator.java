package com.epam.cdp.m2.hw2.aggregator;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

import javafx.util.Pair;

public class Java8Aggregator implements Aggregator {

    @Override
    public int sum(List<Integer> numbers) {

        return numbers.stream().reduce(0,Integer::sum);
    }

    @Override
    public List<Pair<String, Long>> getMostFrequentWords(List<String> words, long limit) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> getDuplicates(List<String> words, long limit) {
        throw new UnsupportedOperationException();
    }
}