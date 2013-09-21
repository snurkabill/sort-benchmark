package net.petrovicky.quicksort.benchmark;

import net.petrovicky.quicksort.impl.Quicksort;

public class SingleThreadedMiddlePartitionedBenchmarkTask<T extends Comparable<T>> extends MiddlePartitionedBenchmarkTask<T> {

    public SingleThreadedMiddlePartitionedBenchmarkTask() {
        super();
    }

    @Override
    public void run(final T[] input) {
        new Quicksort<T>(this.getPartitioningStrategy()).sort(input);
    }

    @Override
    public String toString() {
        return "Single middle pivot";
    }

}
