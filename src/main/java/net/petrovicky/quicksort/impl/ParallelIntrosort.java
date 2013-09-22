package net.petrovicky.quicksort.impl;

import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * Parallel introsort implementation; first multi-threaded quicksort, then degrades into {@link Heapsort}.
 * 
 * @param <T>
 *            Type of the values that are being sorted.
 */
public final class ParallelIntrosort<T extends Comparable<T>> extends RecursiveTask<Boolean> {

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
     * 
     */
    private static final long serialVersionUID = -5434239700032599439L;

    private static final int log2(final int x) {
        return (int) Math.round(Math.log(x) / Math.log(2));
    }

    private final Holder holder;

    private final int left, right, depth;

    protected ParallelIntrosort(final Holder holder, final int left, final int right) {
        this(holder, left, right, ParallelIntrosort.log2(right - left));
    }

    private ParallelIntrosort(final Holder holder, final int left, final int right, final int depth) {
        super();
        this.depth = depth;
        this.holder = holder;
        this.left = left;
        this.right = right;
    }

    public ParallelIntrosort(final PartitioningStrategy<T> partitioner, final T[] input) {
        this(new Holder(input, partitioner), 0, input.length - 1);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Boolean compute() {
        final T[] input = (T[]) this.holder.input; // we know this is safe, since we assign it in the constructor
        if (this.left >= this.right) {
            return true;
        } else if (this.depth == 0) {
            new Heapsort<T>().sort(input, this.left, this.right);
            return true;
        }
        final PartitioningStrategy<T> partitioner = this.holder.strategy;
        final int pivotIndex = partitioner.execute(input, this.left, this.right);
        final ForkJoinTask<Boolean> left = new ParallelIntrosort<T>(this.holder, this.left, pivotIndex - 1, this.depth - 1).fork();
        new ParallelIntrosort<T>(this.holder, pivotIndex + 1, this.right, this.depth - 1).fork().join();
        left.join();
        return true;
    }

}
