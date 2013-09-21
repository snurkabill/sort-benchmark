package net.petrovicky.quicksort.benchmark;

import net.petrovicky.quicksort.impl.PartitioningStrategy;

/**
 * Benchmark test that uses quicksort partitioning where the partition is always in the middle of the range.
 */
public abstract class MiddlePartitionedBenchmarkTask<T extends Comparable<T>> implements BenchmarkTask<T> {

    private final PartitioningStrategy<T> strategy;

    protected MiddlePartitionedBenchmarkTask() {
        this.strategy = new PartitioningStrategy<T>() {

            @Override
            protected int findPivotIndex(final T[] list, final int left, final int right) {
                return left + ((right - left) / 2);
            }
        };
    }

    @Override
    public PartitioningStrategy<T> getPartitioningStrategy() {
        return this.strategy;
    }

}
