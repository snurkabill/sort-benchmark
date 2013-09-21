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

    /**
     * Get a list of random values in a random order.
     * 
     * @param size
     *            Target .size() of the list.
     * @return The list.
     */
    private static Integer[] getAssortedList(final int size) {
        final Integer[] toSort = new Integer[size];
        for (int i = 0; i < size; i++) {
            toSort[i] = Benchmarker.RANDOM.nextInt(Integer.MAX_VALUE);
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
            Benchmarker.LOGGER.debug("Benchmark iteration {}", i);
            final Integer[] toSort = Benchmarker.getAssortedList(numValues);
            for (final BenchmarkTask<Integer> task : this.tasks) {
                Benchmarker.LOGGER.trace("[{}] Preparing data for task '{}'", i, task);
                final Integer[] copy = Arrays.copyOf(toSort, toSort.length);
                if (!results.containsKey(task)) {
                    results.put(task, new ArrayList<Long>());
                }
                System.gc(); // better now than during the task
                Benchmarker.LOGGER.trace("[{}] Running task '{}'", i, task);
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
            }
        }
    }

}
