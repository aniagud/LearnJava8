package com.epam.cdp.m2.hw2.aggregator;

import com.epam.cdp.m2.hw2.aggregator.util.Java7FindFrequencyTask;
import com.epam.cdp.m2.hw2.aggregator.util.Java7SumTask;
import javafx.util.Pair;

import java.util.*;
import java.util.concurrent.*;

import static com.epam.cdp.m2.hw2.aggregator.util.Java7SortUtil.getListFromMap;
import static com.epam.cdp.m2.hw2.aggregator.util.Java7SortUtil.sortMapByValueAndKey;

public class Java7ParallelAggregator implements Aggregator {

    private ExecutorService executor = Executors.newFixedThreadPool(2);

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

            Future<Integer> future1 = executor.submit(new Java7SumTask(numbers1));
            Future<Integer> future2 = executor.submit(new Java7SumTask(numbers2));

            try {
                return future1.get() + future2.get();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }


    @Override
    public List<Pair<String, Long>> getMostFrequentWords(List<String> words, long limit) {

        if (words == null || words.isEmpty()) {
            return new ArrayList<>();
        } else {

            int subsetsNr = 5;
            List<List<String>> wordsSublists = divideStringList(words, subsetsNr);

            List<Future<Map<String,Long>>> futureFrequencies = new ArrayList<>();
            for (List<String> wordSublist: wordsSublists) {
                futureFrequencies.add(executor.submit(new Java7FindFrequencyTask(wordSublist)));
            }

            Map<String,Long> mergedFrequenciesMap = new HashMap<>();
            for (Future<Map<String,Long>> future: futureFrequencies) {
                try {
                    Map<String,Long> map = future.get();
                    for (Map.Entry<String,Long> entry : map.entrySet()){
                        if (mergedFrequenciesMap.containsKey(entry.getKey())){
                            mergedFrequenciesMap.replace(entry.getKey(),
                                    mergedFrequenciesMap.get(entry.getKey()) + entry.getValue());
                        } else {
                            mergedFrequenciesMap.put(entry.getKey(), entry.getValue());
                        }
                    }
                } catch (ExecutionException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            Map<String, Long> sortedWordsFrequency = sortMapByValueAndKey(mergedFrequenciesMap);
            return getListFromMap(sortedWordsFrequency, limit);
        }
    }

    @Override
    public List<String> getDuplicates(List<String> words, long limit) {

        if (words == null || words.isEmpty()) {
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
            Future<Set<String>> futureDuplicates = executor.submit(taskFindDuplicates);

            try {
                return new ArrayList<>(futureDuplicates.get());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private List<List<String>> divideStringList(List<String> words, int subsetsNr){
        int counter = 0;
        List<List<String>> wordsSublists = new ArrayList<>();
        for (int i = 0; i < subsetsNr; i++)  {
            wordsSublists.add(new ArrayList<>());
        }
        for (String word : words) {
            wordsSublists.get(counter).add(word);
            if (++counter == subsetsNr){
                counter = 0;
            }
        }
        return wordsSublists;
    }

}
