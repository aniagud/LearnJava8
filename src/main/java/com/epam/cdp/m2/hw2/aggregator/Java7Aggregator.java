package com.epam.cdp.m2.hw2.aggregator;

import java.util.*;

import javafx.util.Pair;

public class Java7Aggregator implements Aggregator {

    @Override
    public int sum(List<Integer> numbers) {

        int sum = 0;
        for (int number : numbers) {
            sum += number;
        }
        return sum;
    }

    @Override
    public List<Pair<String, Long>> getMostFrequentWords(List<String> words, long limit) {

        Map<String, Long> wordsFrequency = new HashMap<>();
        for (String word : words) {
            if (wordsFrequency.containsKey(word)) {
                wordsFrequency.replace(word, wordsFrequency.get(word) + 1);
            } else {
                wordsFrequency.put(word, 1L);
            }
        }
        Map<String, Long> sortedWordsFrequency = sortMap(wordsFrequency);
        List<Pair<String,Long>> mostFrequentWords = new ArrayList<>();

        Iterator<Map.Entry<String, Long>> iterator = sortedWordsFrequency.entrySet().iterator();
        while (iterator.hasNext() && mostFrequentWords.size() < limit){
            Map.Entry<String, Long> entry = iterator.next();
            mostFrequentWords.add(new Pair<>(entry.getKey(), entry.getValue()));
        }
        return mostFrequentWords;
    }

    @Override
    public List<String> getDuplicates(List<String> words, long limit) {
        throw new UnsupportedOperationException();
    }

    // to sort a map by Long values descending first and by String keys alphabetically when values are equal
    private static Map<String, Long> sortMap(Map<String, Long> map) {
        List<Map.Entry<String, Long>> list = new ArrayList<>(map.entrySet());

        list.sort(new Comparator<Map.Entry<String, Long>>() {
            @Override
            public int compare(Map.Entry<String, Long> o1, Map.Entry<String, Long> o2) {
                int comparedValue = o2.getValue().compareTo(o1.getValue());
                return comparedValue != 0 ? comparedValue : o1.getKey().compareTo(o2.getKey());
            }
        });
//        list.sort((o1, o2) -> (o1.getValue()).compareTo(o2.getValue()));

        Map<String, Long> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Long> e : list) {
            sortedMap.put(e.getKey(), e.getValue());
        }

        return sortedMap;
    }
}
