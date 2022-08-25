package com.epam.cdp.m2.hw2.aggregator;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javafx.util.Pair;

import static java.util.stream.Collectors.*;

public class Java8ParallelAggregator implements Aggregator {

    @Override
    public int sum(List<Integer> numbers) {

        return numbers.parallelStream().reduce(0, Integer::sum);
    }

    @Override
    public List<Pair<String, Long>> getMostFrequentWords(List<String> words, long limit) {

        return words.parallelStream()
                .collect(toMap(w -> w, w -> 1L, Long::sum))
                .entrySet()
                .parallelStream()
                .sorted((o1, o2) -> {
                    int comparedValue = o2.getValue().compareTo(o1.getValue());
                    return comparedValue != 0 ? comparedValue : o1.getKey().compareTo(o2.getKey());
                })
                .limit(limit)
                .map(e -> new Pair<>(e.getKey(), e.getValue()))
                .collect(toList());
    }

    @Override
    public List<String> getDuplicates(List<String> words, long limit) {

        return words.parallelStream()
                .map(String::toUpperCase)
                .collect(groupingBy(Function.identity(),counting()))
                .entrySet()
                .parallelStream()
                .filter(entry -> entry.getValue() > 1)
                .sorted(Comparator.comparingInt((Map.Entry<String, Long> o) ->
                        o.getKey().length()).thenComparing(Map.Entry::getKey))
                .limit(limit)
                .map(Map.Entry::getKey)
                .collect(toList());
    }
}
