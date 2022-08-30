package com.epam.cdp.m2.hw2.aggregator.util;

import java.util.*;

public class Java7SortUtil {

    /**
     * use to sort a map by Long values descending first and by String keys alphabetically when values are equal
     * @param map map of strings with long values
     * @return a sorted map
     */
    public static Map<String, Long> sortMapByValueAndKey(Map<String, Long> map) {
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
