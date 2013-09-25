package net.petrovicky.quicksort.benchmark;

import net.petrovicky.quicksort.impl.MergeSort;

public class SimpleMergeSortMiddlePartitionedBenchmarkTask<T extends Comparable<T>> extends MiddlePartitionedBenchmarkTask<T> {
		
	public SimpleMergeSortMiddlePartitionedBenchmarkTask() {
        super();
     }
	
	@Override
    public void run(final T[] input) {
		new MergeSort(input).mergeSort(0, input.length - 1);
	}

    @Override
    public String toString() {
        return "Single merge";
    }
	
}
