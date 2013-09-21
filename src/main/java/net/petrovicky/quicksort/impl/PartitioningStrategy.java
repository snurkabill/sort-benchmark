package net.petrovicky.quicksort.impl;

/**
 * This class implements Quicksort's partitioning, giving users their choice of a pivot finding strategy.
 * 
 * Due to its nature, this class takes roughly 80 % of the whole app's run time. For this reason, even the tiniest
 * performance improvements help the app a lot. This is why {@link #partition(Comparable[], int, int, int)} and
 * {@link #swap(Object[], int, int)} are private static final, although they normally wouldn't be.
 * 
 * @param <T>
 *            Type of the data which we're partitioning.
 */
public abstract class PartitioningStrategy<T extends Comparable<T>> {

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static final int partition(final Comparable[] input, final int left, final int right, final int pivotIndex) {
        final Comparable pivotValue = input[pivotIndex];
        PartitioningStrategy.swap(input, pivotIndex, right);
        int storeIndex = left;
        for (int i = left; i < right; i++) {
            if (input[i].compareTo(pivotValue) >= 0) {
                continue;
            }
            PartitioningStrategy.swap(input, i, storeIndex);
            storeIndex++;
        }
        PartitioningStrategy.swap(input, storeIndex, right);
        return storeIndex;
    }

    private static final void swap(final Object[] input, final int index1, final int index2) {
        final Object tmp = input[index1];
        input[index1] = input[index2];
        input[index2] = tmp;
    }

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
        return PartitioningStrategy.partition(input, left, right, this.findPivotIndex(input, left, right));
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

}
