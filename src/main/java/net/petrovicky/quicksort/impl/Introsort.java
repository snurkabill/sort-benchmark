package net.petrovicky.quicksort.impl;

/**
 * Simple quicksort implementation with no parallelism.
 * 
 * @param <T>
 *            Type of the values that are being sorted.
 */
public final class Introsort<T extends Comparable<T>> {

    private static final int log2(final int x) {
        return (int) Math.round(Math.log(x) / Math.log(2));
    }

    private final PartitioningStrategy<T> partitioner;

    public Introsort(final PartitioningStrategy<T> partitioner) {
        this.partitioner = partitioner;
    }

    public void sort(final T[] input) {
        this.sort(input, 0, input.length - 1);
    }

    protected void sort(final T[] input, final int left, final int right) {
        this.sort(input, left, right, 2 * Introsort.log2(right - left));
    }

    private void sort(final T[] input, final int left, final int right, final int depth) {
        if (right <= left) {
            return;
        } else if (depth == 0) {
            new Heapsort<T>().sort(input, left, right);
            return;
        }
        final int pivotIndex = this.partitioner.execute(input, left, right);
        this.sort(input, left, pivotIndex - 1, depth - 1);
        this.sort(input, pivotIndex + 1, right, depth - 1);
    }

}
