package net.petrovicky.quicksort.benchmark;

import net.petrovicky.quicksort.impl.Introsort;

public class SimpleIntrosortMiddlePartitionedBenchmarkTask<T extends Comparable<T>> extends MiddlePartitionedBenchmarkTask<T> {

    public SimpleIntrosortMiddlePartitionedBenchmarkTask() {
        super();
    }

    @Override
    public void run(final T[] input) {
        new Introsort<T>(this.getPartitioningStrategy()).sort(input);
    }

    @Override
    public String toString() {
        return "Single intro middle pivot";
    }

}
