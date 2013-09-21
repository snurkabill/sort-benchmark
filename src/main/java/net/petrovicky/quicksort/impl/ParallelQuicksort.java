package net.petrovicky.quicksort.impl;

import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * Parallel quicksort implementation, leveraging JDK 7's fork-join implementation. When the range to be sorted drops
 * under {@link #CONCURRENCY_THRESHOLD}, all the recursion happens in the same thread.
 * 
 * @param <T>
 *            Type of the values that are being sorted.
 */
public final class ParallelQuicksort<T extends Comparable<T>> extends RecursiveTask<Boolean> {

    /**
     * When the range is shorter than this number, we don't use concurrency anymore.
     */
    private static final int CONCURRENCY_THRESHOLD = 1000;
    /**
     * 
     */
    private static final long serialVersionUID = -5434239700032599439L;
    private final T[] input;
    private final int left, right;
    private final PartitioningStrategy<T> partitioner;

    public ParallelQuicksort(final PartitioningStrategy<T> partitioner, final T[] input) {
        this(partitioner, input, 0, input.length - 1);
    }

    protected ParallelQuicksort(final PartitioningStrategy<T> partitioner, final T[] input, final int left, final int right) {
        super();
        this.partitioner = partitioner;
        this.input = input;
        this.left = left;
        this.right = right;
    }

    @Override
    protected Boolean compute() {
        if (this.left >= this.right) {
            return true;
        }
        final int pivotIndex = this.partitioner.execute(this.input, this.left, this.right);
        if ((this.right - this.left) < ParallelQuicksort.CONCURRENCY_THRESHOLD) {
            new ParallelQuicksort<T>(this.partitioner, this.input, this.left, pivotIndex - 1).compute();
            new ParallelQuicksort<T>(this.partitioner, this.input, pivotIndex + 1, this.right).compute();
        } else {
            final ForkJoinTask<Boolean> left = new ParallelQuicksort<T>(this.partitioner, this.input, this.left, pivotIndex - 1).fork();
            final ForkJoinTask<Boolean> right = new ParallelQuicksort<T>(this.partitioner, this.input, pivotIndex + 1, this.right).fork();
            right.join();
            left.join();
        }
        return true;
    }

}
