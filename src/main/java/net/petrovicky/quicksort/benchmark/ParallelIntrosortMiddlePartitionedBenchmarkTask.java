package net.petrovicky.quicksort.benchmark;

import java.util.concurrent.ForkJoinPool;

import net.petrovicky.quicksort.impl.ParallelIntrosort;

public class ParallelIntrosortMiddlePartitionedBenchmarkTask<T extends Comparable<T>> extends MiddlePartitionedBenchmarkTask<T> {

    private final ForkJoinPool pool;
    private final int threads;

    /**
     * 
     * @param numThreads
     *            How many threads to use for sorting.
     */
    public ParallelIntrosortMiddlePartitionedBenchmarkTask(final int numThreads) {
        super();
        this.threads = numThreads;
        this.pool = new ForkJoinPool(numThreads);
    }

    @Override
    public void run(final T[] input) {
        this.pool.invoke(new ParallelIntrosort<>(this.getPartitioningStrategy(), input));
    }

    @Override
    public String toString() {
        return "Multi-" + this.threads + " intro middle pivot";
    }

}
