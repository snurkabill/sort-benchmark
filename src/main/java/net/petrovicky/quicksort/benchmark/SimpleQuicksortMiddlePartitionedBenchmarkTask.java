package net.petrovicky.quicksort.benchmark;

import net.petrovicky.quicksort.impl.Quicksort;

public class SimpleQuicksortMiddlePartitionedBenchmarkTask<T extends Comparable<T>> extends MiddlePartitionedBenchmarkTask<T> {

    public SimpleQuicksortMiddlePartitionedBenchmarkTask() {
        super();
    }

    @Override
    public void run(final T[] input) {
        new Quicksort<T>(this.getPartitioningStrategy()).sort(input);
    }

    @Override
    public String toString() {
        return "Single quick middle pivot";
    }

}
