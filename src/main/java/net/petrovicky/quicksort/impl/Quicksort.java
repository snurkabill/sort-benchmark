package net.petrovicky.quicksort.impl;

/**
 * Simple quicksort implementation with no parallelism.
 * 
 * @param <T>
 *            Type of the values that are being sorted.
 */
public final class Quicksort<T extends Comparable<T>> {

    private final PartitioningStrategy<T> partitioner;

    public Quicksort(final PartitioningStrategy<T> partitioner) {
        this.partitioner = partitioner;
    }

    public void sort(final T[] input) {
        this.sort(input, 0, input.length - 1);
    }

    protected void sort(final T[] input, final int left, final int right) {
        if (left >= right) {
            return;
        }
        final int pivotIndex = this.partitioner.execute(input, left, right);
        this.sort(input, left, pivotIndex - 1);
        this.sort(input, pivotIndex + 1, right);
    }

}
