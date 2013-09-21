package net.petrovicky.quicksort.impl;

import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * Parallel quicksort implementation, leveraging JDK 7's fork-join implementation. When the length of the unsorted range
 * drops under {@link #CONCURRENCY_THRESHOLD}, the algorithm switches to {@link Quicksort}.
 * 
 * @param <T>
 *            Type of the values that are being sorted.
 */
public final class ParallelQuicksort<T extends Comparable<T>> extends RecursiveTask<Boolean> {

    /**
     * The sole purpose of this class is to prevent {@link #ParallelQuicksort} from referencing two objects. Since there
     * will be millions of those objects in memory, saving even one reference in each will be significant.
     * 
     * This class is static for that same reason.
     * 
     */
    private static final class Holder {

        @SuppressWarnings("rawtypes")
        private final Comparable[] input;
        @SuppressWarnings("rawtypes")
        private final PartitioningStrategy strategy;

        @SuppressWarnings("rawtypes")
        public Holder(final Comparable[] input, final PartitioningStrategy strategy) {
            this.input = input;
            this.strategy = strategy;
        }

    }

    /**
     * When the range is shorter than this number, we don't use concurrency anymore.
     */
    private static final int CONCURRENCY_THRESHOLD = 1000;
    /**
     * 
     */
    private static final long serialVersionUID = -5434239700032599439L;
    private final Holder holder;
    private final int left, right;

    protected ParallelQuicksort(final Holder holder, final int left, final int right) {
        super();
        this.holder = holder;
        this.left = left;
        this.right = right;
    }

    public ParallelQuicksort(final PartitioningStrategy<T> partitioner, final T[] input) {
        this(new Holder(input, partitioner), 0, input.length - 1);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Boolean compute() {
        if (this.left >= this.right) {
            return true;
        }
        final PartitioningStrategy<T> partitioner = this.holder.strategy;
        final T[] input = (T[]) this.holder.input; // we know this is safe, since we assign it in the constructor
        final int pivotIndex = partitioner.execute(input, this.left, this.right);
        if ((this.right - this.left) < ParallelQuicksort.CONCURRENCY_THRESHOLD) {
            final Quicksort<T> serial = new Quicksort<>(partitioner);
            serial.sort(input, this.left, pivotIndex - 1);
            serial.sort(input, pivotIndex + 1, this.right);
        } else {
            final ForkJoinTask<Boolean> left = new ParallelQuicksort<T>(this.holder, this.left, pivotIndex - 1).fork();
            new ParallelQuicksort<T>(this.holder, pivotIndex + 1, this.right).fork().join();
            left.join();
        }
        return true;
    }

}
