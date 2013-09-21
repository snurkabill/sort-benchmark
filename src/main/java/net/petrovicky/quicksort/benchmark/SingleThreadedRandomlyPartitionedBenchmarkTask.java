package net.petrovicky.quicksort.benchmark;

import net.petrovicky.quicksort.impl.Quicksort;

public class SingleThreadedRandomlyPartitionedBenchmarkTask<T extends Comparable<T>> extends RandomlyPartitionedBenchmarkTask<T> {

    public SingleThreadedRandomlyPartitionedBenchmarkTask() {
        super();
    }

    @Override
    public void run(final T[] input) {
        new Quicksort<T>(this.getPartitioningStrategy()).sort(input);
    }

    @Override
    public String toString() {
        return "Single random pivot";
    }

}
