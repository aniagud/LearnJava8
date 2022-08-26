package com.epam.cdp.m2.hw2.aggregator;

import javafx.util.Pair;

import java.util.*;

import static com.epam.cdp.m2.hw2.aggregator.util.Java7SortUtil.sortMapByValue;

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
        Map<String, Long> sortedWordsFrequency = sortMapByValue(wordsFrequency);
        List<Pair<String, Long>> mostFrequentWords = new ArrayList<>();

        Iterator<Map.Entry<String, Long>> iterator = sortedWordsFrequency.entrySet().iterator();
        while (iterator.hasNext() && mostFrequentWords.size() < limit) {
            Map.Entry<String, Long> entry = iterator.next();
            mostFrequentWords.add(new Pair<>(entry.getKey(), entry.getValue()));
        }
        return mostFrequentWords;
    }

    @Override
    public List<String> getDuplicates(List<String> words, long limit) {

        Set<String> wordsSet = new HashSet<>();
        Set<String> duplicates = new TreeSet<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                int comparedLength = Integer.compare(o1.length(), o2.length());
                return comparedLength != 0 ? comparedLength : o1.compareTo(o2);
            }
        });
        for (String word : words) {
            String w = word.toUpperCase();
            if (duplicates.size() < limit && !wordsSet.add(w)) {
                duplicates.add(w);
            }
        }
        return new ArrayList<>(duplicates);
    }
}
