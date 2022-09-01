package com.epam.cdp.m2.hw2.aggregator.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Task class to be executed to find words' frequencies from a String list
 */
public class Java7FindFrequencyTask implements Callable {

    private List<String> words;

    public Java7FindFrequencyTask(List<String> words) {
        this.words = words;
    }

    @Override
    public Map<String, Long> call() {
        Map<String, Long> wordsFrequency = new HashMap<>();

        for (String word : words) {
            if (wordsFrequency.containsKey(word)) {
                wordsFrequency.replace(word, wordsFrequency.get(word) + 1);
            } else {
                wordsFrequency.put(word, 1L);
            }
        }
        return wordsFrequency;
    }
}
