package net.petrovicky.quicksort.benchmark;

import java.util.Random;

import net.petrovicky.quicksort.impl.PartitioningStrategy;

/**
 * Benchmark test that uses quicksort partitioning where the partition is always randomly placed within the range.
 */
public abstract class RandomlyPartitionedBenchmarkTask<T extends Comparable<T>> implements BenchmarkTask<T> {

    private static final Random RANDOM = new Random(0);
    private final PartitioningStrategy<T> strategy;

    protected RandomlyPartitionedBenchmarkTask() {
        this.strategy = new PartitioningStrategy<T>() {

            @Override
            protected int findPivotIndex(final T[] list, final int left, final int right) {
                return left + RandomlyPartitionedBenchmarkTask.RANDOM.nextInt(right - left);
            }
        };
    }

    @Override
    public PartitioningStrategy<T> getPartitioningStrategy() {
        return this.strategy;
    }

}
