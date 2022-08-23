package com.epam.cdp.m2.hw2.aggregator;

import java.util.List;

import javafx.util.Pair;

public class Java7Aggregator implements Aggregator {

    @Override
    public int sum(List<Integer> numbers) {

        int sum = 0;
        for (int number: numbers) {
            sum+=number;
        }
        return sum;
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
