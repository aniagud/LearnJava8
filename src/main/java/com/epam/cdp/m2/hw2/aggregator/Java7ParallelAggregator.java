package com.epam.cdp.m2.hw2.aggregator;

import javafx.util.Pair;

import java.util.*;
import java.util.concurrent.*;

import static com.epam.cdp.m2.hw2.aggregator.util.Java7SortUtil.sortMapByValueAndKey;

public class Java7ParallelAggregator implements Aggregator {

    @Override
    public int sum(List<Integer> numbers) {

        if (numbers == null || numbers.isEmpty()) {
            return 0;
        } else {

            List<Integer> numbers1 = new ArrayList<>();
            List<Integer> numbers2 = new ArrayList<>();

            for (int i = 0; i < numbers.size(); i++) {
                if (i % 2 == 0) {
                    numbers1.add(numbers.get(i));
                } else {
                    numbers2.add(numbers.get(i));
                }
            }
            Callable<Integer> task1 = new Callable<Integer>() {
                @Override
                public Integer call() {
                    return executeSum(numbers1);
                }
            };

            Callable<Integer> task2 = new Callable<Integer>() {
                @Override
                public Integer call() {
                    return executeSum(numbers2);
                }
            };

            ExecutorService executor = Executors.newFixedThreadPool(2);
            Future<Integer> future1 = executor.submit(task1);
            Future<Integer> future2 = executor.submit(task2);

            try {
                return future1.get() + future2.get();
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                executor.shutdown();
            }
        }
    }

    private int executeSum(List<Integer> numbers) {
        int sum = 0;
        for (int number : numbers) {
            sum += number;
        }
        return sum;
    }

    @Override
    public List<Pair<String, Long>> getMostFrequentWords(List<String> words, long limit) {

        if (words == null || words.isEmpty()){
            return new ArrayList<>();
        } else {

            Callable<Map<String,Long>> taskFindFrequencies = new Callable<Map<String,Long>>() {
                @Override
                public Map<String,Long> call() {
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
            };
            ExecutorService executor = Executors.newFixedThreadPool(2);
            Future<Map<String,Long>> futureFrequencies = executor.submit(taskFindFrequencies);

            try {
                Map<String, Long> sortedWordsFrequency = sortMapByValueAndKey(futureFrequencies.get());

                Callable<List<Pair<String,Long>>> taskCreateList = new Callable<List<Pair<String,Long>>>() {
                    @Override
                    public List<Pair<String,Long>> call() {

                        List<Pair<String, Long>> mostFrequentWords = new ArrayList<>();

                        Iterator<Map.Entry<String, Long>> iterator = sortedWordsFrequency.entrySet().iterator();
                        while (iterator.hasNext() && mostFrequentWords.size() < limit) {
                            Map.Entry<String, Long> entry = iterator.next();
                            mostFrequentWords.add(new Pair<>(entry.getKey(), entry.getValue()));
                        }
                        return mostFrequentWords;
                    }
                };
                Future<List<Pair<String,Long>>>  futureList = executor.submit(taskCreateList);

                return futureList.get();

            } catch (Exception e){
                throw new RuntimeException(e);
            }finally {
                executor.shutdown();
            }
        }
    }

    @Override
    public List<String> getDuplicates(List<String> words, long limit) {

        if (words == null || words.isEmpty()){
            return new ArrayList<>();
        } else {

            Callable<Set<String>> taskFindDuplicates = new Callable<Set<String>>() {
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
            };
            ExecutorService executor = Executors.newFixedThreadPool(2);
            Future<Set<String>> futureDuplicates = executor.submit(taskFindDuplicates);

            try {
                return new ArrayList<>(futureDuplicates.get());
            } catch (Exception e){
                throw new RuntimeException(e);
            } finally {
                executor.shutdown();
            }
        }
    }

}
