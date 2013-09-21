package net.petrovicky.quicksort.benchmark;

import java.util.concurrent.ForkJoinPool;

import net.petrovicky.quicksort.impl.ParallelQuicksort;

public class MultiThreadedMiddlePartitionedBenchmarkTask<T extends Comparable<T>> extends MiddlePartitionedBenchmarkTask<T> {

    private final ForkJoinPool pool;
    private final int threads;

    /**
     * 
     * @param numThreads
     *            How many threads to use for sorting.
     */
    public MultiThreadedMiddlePartitionedBenchmarkTask(final int numThreads) {
        super();
        this.threads = numThreads;
        this.pool = new ForkJoinPool(numThreads);
    }

    @Override
    public void run(final T[] input) {
        this.pool.invoke(new ParallelQuicksort<>(this.getPartitioningStrategy(), input));
    }

    @Override
    public String toString() {
        return "Multi-" + this.threads + ", middle pivot";
    }

}
