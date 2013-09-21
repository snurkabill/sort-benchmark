package net.petrovicky.quicksort.impl;

/**
 * This class implements Quicksort's partitioning, giving users their choice of a pivot findning strategy.
 * 
 * @param <T>
 *            Type of the list which we're partitioning.
 */
public abstract class PartitioningStrategy<T extends Comparable<T>> {

    /**
     * Splits the range into two partitions.
     * 
     * @param input
     *            Data being sorted.
     * @param left
     *            Left boundary index of the range.
     * @param right
     *            Right boundary index of the range.
     * @return Partition index.
     */
    public int execute(final T[] input, final int left, final int right) {
        return this.partition(input, left, right, this.findPivotIndex(input, left, right));
    }

    /**
     * Determine Quicksort's pivot.
     * 
     * @param input
     *            Data being sorted.
     * @param left
     *            Left boundary index of the range.
     * @param right
     *            Right boundary index of the range.
     * @return Pivot index.
     */
    protected abstract int findPivotIndex(final T[] input, final int left, final int right);

    private int partition(final T[] input, final int left, final int right, final int pivotIndex) {
        final T pivotValue = input[pivotIndex];
        this.swap(input, pivotIndex, right);
        int storeIndex = left;
        for (int i = left; i < right; i++) {
            if (input[i].compareTo(pivotValue) >= 0) {
                continue;
            }
            this.swap(input, i, storeIndex);
            storeIndex++;
        }
        this.swap(input, storeIndex, right);
        return storeIndex;
    }

    private void swap(final T[] input, final int index1, final int index2) {
        final T tmp = input[index1];
        input[index1] = input[index2];
        input[index2] = tmp;
    }

}
