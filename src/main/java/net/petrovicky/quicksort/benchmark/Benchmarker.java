package net.petrovicky.quicksort.benchmark;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Benchmarker {

    private static final Logger LOGGER = LoggerFactory.getLogger(Benchmarker.class);

    private static final int NUM_VALUES_WARMUP = 2500000;
    private static final Random RANDOM = new Random(0);

    private static final int WARMUP_RUNS = 30;

    private static void assertSorted(final BenchmarkTask<Integer> task, final Integer[] input) {
        Integer previous = Integer.MIN_VALUE;
        for (final Integer i : input) {
            if (i < previous) {
                throw new IllegalArgumentException("Unsorted array produced by " + task);
            }
            previous = i;
        }
    }

    /**
     * Get an array of random values in a random order.
     * 
     * @param size
     *            Target .size() of the list.
     * @return The list.
     */
    private static Integer[] getAssortedList(final int size) {
        final int[] streaks = new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 4, 8, 16};
        final int maxNum = (int) Math.round(size * 1.1); // some numbers are likely to repeat
        final Integer[] toSort = new Integer[size];
        int filled = 0;
        while (filled < size) {
            // we also want some negative numbers
            final int startingNumber = Benchmarker.RANDOM.nextInt(maxNum) - (maxNum / 2);
            // how many items already sorted there will be
            final int instances = streaks[Benchmarker.RANDOM.nextInt(streaks.length)];
            for (int i = 0; i < instances; i++) {
                toSort[filled] = startingNumber + i;
                filled++;
                if (filled >= size) { // don't allow the array to be over-filled
                    break;
                }
            }
        }
        return toSort;
    }

    private final Set<BenchmarkTask<Integer>> tasks;

    public Benchmarker(final Collection<BenchmarkTask<Integer>> tasks) {
        this.tasks = new LinkedHashSet<>(tasks);
    }

    public Map<BenchmarkTask<Integer>, List<Long>> benchmark(final int numIterations, final int numValues) {
        Benchmarker.LOGGER.info("Benchmarking arrays of {} values in {} iterations.", numValues, numIterations);
        final Map<BenchmarkTask<Integer>, List<Long>> results = new HashMap<>();
        for (int i = 1; i <= numIterations; i++) {
            Benchmarker.LOGGER.debug("[{}] Benchmark iteration {}", numValues, i);
            final Integer[] toSort = Benchmarker.getAssortedList(numValues);
            for (final BenchmarkTask<Integer> task : this.tasks) {
                Benchmarker.LOGGER.trace("[{}x{}] Preparing data for task '{}'", numValues, i, task);
                final Integer[] copy = Arrays.copyOf(toSort, toSort.length);
                if (!results.containsKey(task)) {
                    results.put(task, new ArrayList<Long>());
                }
                System.gc(); // better now than during the task
                Benchmarker.LOGGER.trace("[{}x{}] Running task '{}'", numValues, i, task);
                results.get(task).add(this.timeTask(task, copy));
            }
        }
        return results;
    }

    /**
     * Run the task and time it.
     * 
     * @param task
     *            Task to time.
     * @param input
     *            Input to sort.
     * @return Time it took to run the task, in nanoseconds.
     */
    private long timeTask(final BenchmarkTask<Integer> task, final Integer[] input) {
        final long start = System.nanoTime();
        task.run(input);
        final long result = System.nanoTime() - start;
        Benchmarker.LOGGER.trace("Task took " + result + " nanoseconds to execute.");
        return result;
    }

    public void warmup() {
        Benchmarker.LOGGER.info("Warming up the JVM...");
        for (int i = 1; i <= Benchmarker.WARMUP_RUNS; i++) {
            Benchmarker.LOGGER.debug("Warm-up iteration {}", i);
            final Integer[] toSort = Benchmarker.getAssortedList(Benchmarker.NUM_VALUES_WARMUP);
            for (final BenchmarkTask<Integer> task : this.tasks) {
                Benchmarker.LOGGER.trace("[{}] Warming up task '{}'", i, task);
                final Integer[] input = Arrays.copyOf(toSort, toSort.length);
                task.run(input);
                Benchmarker.LOGGER.trace("[{}] Validating result of task '{}'", i, task);
                Benchmarker.assertSorted(task, input);
            }
        }
    }

}
