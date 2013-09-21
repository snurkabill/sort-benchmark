package net.petrovicky.quicksort.benchmark;

import java.util.Arrays;

import net.petrovicky.quicksort.impl.PartitioningStrategy;

/**
 * Benchmark test that uses JDK's own sort.
 */
public class NativeBenchmarkTask<T extends Comparable<T>> implements BenchmarkTask<T> {

    @Override
    public PartitioningStrategy<T> getPartitioningStrategy() {
        return null;
    }

    @Override
    public void run(final T[] input) {
        Arrays.sort(input);
    }

    @Override
    public String toString() {
        return "Native";
    }

}
