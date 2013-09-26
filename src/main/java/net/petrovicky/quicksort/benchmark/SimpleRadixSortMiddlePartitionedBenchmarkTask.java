package net.petrovicky.quicksort.benchmark;

import net.petrovicky.quicksort.impl.RadixSort;
	
public class SimpleRadixSortMiddlePartitionedBenchmarkTask<T extends Comparable<T>> extends MiddlePartitionedBenchmarkTask<T> {
		
	public SimpleRadixSortMiddlePartitionedBenchmarkTask() {
        super();
     }
	
	@Override
    public void run(final T[] input) {
		new RadixSort(2).sort((Integer[])input);
	}

    @Override
    public String toString() {
        return "Single radix";
    }
	
}
