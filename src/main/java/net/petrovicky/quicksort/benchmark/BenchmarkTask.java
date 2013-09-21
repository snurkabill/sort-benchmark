package net.petrovicky.quicksort.benchmark;

import net.petrovicky.quicksort.impl.PartitioningStrategy;

/**
 * Represents a single unit of work that is to be run in the benchmark.
 * 
 * @param <T>
 *            Type of the values that are being sorter.
 */
public interface BenchmarkTask<T extends Comparable<T>> {

    /**
     * Allows users to provide their own quicksort partitioning strategy.
     * 
     * @return
     */
    PartitioningStrategy<T> getPartitioningStrategy();

    /**
     * Users should use this method to call the actual sorting algorithm. This length of this method's run time will be
     * timed.
     * 
     * @param input
     *            The input to sort.
     */
    void run(T[] input);

}
