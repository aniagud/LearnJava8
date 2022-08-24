package com.epam.cdp.m2.hw2.aggregator;

import javafx.util.Pair;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class Java8Aggregator implements Aggregator {

    @Override
    public int sum(List<Integer> numbers) {

        return numbers.stream().reduce(0, Integer::sum);
    }

    @Override
    public List<Pair<String, Long>> getMostFrequentWords(List<String> words, long limit) {

//        Map<String, Long> wordsFrequency = new HashMap<>();
//        words.forEach(w -> wordsFrequency.compute(w, (k, v) -> v == null ? 1 : v + 1));
//                           wordsFrequency.merge(w, 1L, Long::sum);     //(oldValue, initValue) -> oldValue + initValue)
        return words.stream()
                .collect(toMap(w -> w, w -> 1L, Long::sum))
                .entrySet()
                .stream()
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
        throw new UnsupportedOperationException();
    }
}