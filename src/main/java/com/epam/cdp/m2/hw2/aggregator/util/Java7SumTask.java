package com.epam.cdp.m2.hw2.aggregator.util;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Task class to be executed to count the sum of numbers from an integer list
 */
public class Java7SumTask implements Callable {

    private List<Integer> numbers;

    public Java7SumTask(List<Integer> numbers) {
        this.numbers = numbers;
    }

    @Override
    public Integer call() {
        return executeSum(numbers);
    }

    private int executeSum(List<Integer> numbers) {
        int sum = 0;
        for (int number : numbers) {
            sum += number;
        }
        return sum;
    }
}
