package net.petrovicky.quicksort;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import net.petrovicky.quicksort.benchmark.BenchmarkTask;
import net.petrovicky.quicksort.benchmark.Benchmarker;
import net.petrovicky.quicksort.benchmark.ParallelIntrosortMiddlePartitionedBenchmarkTask;
import net.petrovicky.quicksort.benchmark.ParallelQuicksortMiddlePartitionedBenchmarkTask;
import net.petrovicky.quicksort.benchmark.NativeBenchmarkTask;
import net.petrovicky.quicksort.benchmark.SimpleIntrosortMiddlePartitionedBenchmarkTask;
import net.petrovicky.quicksort.benchmark.SimpleQuicksortMiddlePartitionedBenchmarkTask;

/**
 * This app benchmarks single-threaded and multi-threaded quicksort implementations under various conditions.
 */
public class Main {

    private static final int MAX_LIST_SIZE = 100 * 1000 * 1000; // more will not fit into -Xmx2048m

    /**
     * Find the median value in a list of values.
     * 
     * @param values
     *            Values in which to find median.
     * @return Median.
     */
    private static long findMiddle(final List<Long> values) {
        Collections.sort(values);
        if (values.size() % 2 == 1) {
            return values.get((values.size() + 1) / 2 - 1);
        } else {
            final long lower = values.get(values.size() / 2 - 1);
            final long upper = values.get(values.size() / 2);
            return (lower + upper) / 2;
        }
    }

    public static void main(final String[] args) {
        // assemble tasks
        final List<BenchmarkTask<Integer>> tasks = new ArrayList<>();
        tasks.add(new NativeBenchmarkTask<Integer>());
        tasks.add(new SimpleQuicksortMiddlePartitionedBenchmarkTask<Integer>());
        tasks.add(new SimpleIntrosortMiddlePartitionedBenchmarkTask<Integer>());
        for (int power = 0; power < 6; power++) {
            final int numThreads = (int) Math.pow(2, power);
            tasks.add(new ParallelQuicksortMiddlePartitionedBenchmarkTask<Integer>(numThreads));
            tasks.add(new ParallelIntrosortMiddlePartitionedBenchmarkTask<Integer>(numThreads));
        }
        // prepare the benchmarker and warm up the JVM, so that we have consistent results
        final Benchmarker b = new Benchmarker(tasks);
        b.warmup();
        final Map<String, Map<Integer, Integer>> aggregate = new HashMap<>();
        // run those tasks on increasingly long lists; report results
        for (int size = 1000; size <= Main.MAX_LIST_SIZE; size *= 10) {
            // run benchmark on a particular list size
            final Map<String, Map<Integer, Integer>> intermediate = Main.processBenchmark(b, size);
            // format results of that benchmark so that they fit in the chart later
            for (final Map.Entry<String, Map<Integer, Integer>> entry : intermediate.entrySet()) {
                if (!aggregate.containsKey(entry.getKey())) {
                    aggregate.put(entry.getKey(), new TreeMap<Integer, Integer>());
                }
                aggregate.get(entry.getKey()).putAll(entry.getValue());
            }
        }
        // create a chart from those results
        new Chart().chart(new File("result.png"), aggregate);
    }

    private static Map<String, Map<Integer, Integer>> processBenchmark(final Benchmarker b, final int inputSize) {
        final Map<BenchmarkTask<Integer>, Integer> data = Main.runBenchmark(b, inputSize);
        final Map<String, Map<Integer, Integer>> result = new HashMap<>();
        for (final Map.Entry<BenchmarkTask<Integer>, Integer> entry : data.entrySet()) {
            final Map<Integer, Integer> record = new HashMap<>();
            record.put(inputSize, entry.getValue());
            result.put(entry.getKey().toString(), record);
        }
        return result;
    }

    /**
     * Will run a given benchmark on a particularly sized input, then summarize and output results.
     * 
     * @param b
     *            Sorting benchmark to run.
     * @param inputSize
     *            Number of items in the list to be sorted.
     * @return A map where key is the task and the respective value is the median run time for the task.
     */
    private static Map<BenchmarkTask<Integer>, Integer> runBenchmark(final Benchmarker b, final int inputSize) {
        System.gc();
        final Map<BenchmarkTask<Integer>, List<Long>> results = b.benchmark(10, inputSize);
        // sort benchmarks in increasing order of processing time
        final Map<Long, Set<BenchmarkTask<Integer>>> orderedResults = new TreeMap<>();
        for (final Map.Entry<BenchmarkTask<Integer>, List<Long>> result : results.entrySet()) {
            final long middle = Main.findMiddle(result.getValue());
            if (!orderedResults.containsKey(middle)) {
                orderedResults.put(middle, new HashSet<BenchmarkTask<Integer>>());
            }
            orderedResults.get(middle).add(result.getKey());
        }
        // output results
        System.out.println("Benchmark results for lists of " + inputSize + " numbers: ");
        final Map<BenchmarkTask<Integer>, Integer> averages = new HashMap<>();
        for (final Map.Entry<Long, Set<BenchmarkTask<Integer>>> result : orderedResults.entrySet()) {
            final int nsPerValue = Math.round(result.getKey() / inputSize);
            for (final BenchmarkTask<Integer> task : result.getValue()) {
                averages.put(task, nsPerValue);
            }
            System.out.println(result.getKey() + " ns: " + result.getValue() + "; " + nsPerValue + " ns per value.");
        }
        return averages;
    }

}
