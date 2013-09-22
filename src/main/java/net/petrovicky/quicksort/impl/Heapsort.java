package net.petrovicky.quicksort.impl;

/**
 * Classic heap sort.
 * 
 * @param <T>
 *            Type of the values that are being sorted.
 */
public final class Heapsort<T extends Comparable<T>> {

    private static final void swap(final Object[] input, final int index1, final int index2) {
        final Object tmp = input[index1];
        input[index1] = input[index2];
        input[index2] = tmp;
    }

    private void heapify(final T[] input, final int left, final int count) {
        int start = ((count - 2) / 2);
        while (start >= 0) {
            this.siftDown(input, start, count - 1, left);
            start--;
        }
    }

    private void siftDown(final T[] input, final int start, final int end, final int offset) {
        int root = start;
        while ((root * 2 + 1) <= end) {
            final int child = root * 2 + 1;
            int swap = root;
            if (input[swap + offset].compareTo(input[child + offset]) < 0) {
                swap = child;
            }
            final int next = child + 1;
            if (next <= end && input[swap + offset].compareTo(input[next + offset]) < 0) {
                swap = next;
            }
            if (swap != root) {
                Heapsort.swap(input, root + offset, swap + offset);
                root = swap;
            } else {
                return;
            }
        }
    }

    public void sort(final T[] input) {
        this.sort(input, 0, input.length - 1);
    }

    protected void sort(final T[] input, final int left, final int right) {
        final int count = right - left + 1;
        this.heapify(input, left, count);
        int end = count - 1;
        while (end > 0) {
            Heapsort.swap(input, left + end, left);
            end--;
            this.siftDown(input, 0, end, left);
        }
    }

}
