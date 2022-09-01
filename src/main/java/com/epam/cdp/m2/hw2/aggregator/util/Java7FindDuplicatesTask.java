package com.epam.cdp.m2.hw2.aggregator.util;

import java.util.*;
import java.util.concurrent.Callable;

public class Java7FindDuplicatesTask implements Callable {

    private List<String> words;
    private long limit;

    public Java7FindDuplicatesTask(List<String> words, long limit) {
        this.words = words;
        this.limit = limit;
    }


    @Override
    public Set<String> call() {

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
        return duplicates;
    }
}
