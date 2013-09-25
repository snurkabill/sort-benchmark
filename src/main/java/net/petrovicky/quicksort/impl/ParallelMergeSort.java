package net.petrovicky.quicksort.impl;

import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ForkJoinTask;

public class ParallelMergeSort<T extends Comparable<T>> extends RecursiveTask<Boolean> {
	
	private static final class Holder {
	
		private final Comparable[] array;
		private final Comparable[] helpArray;
		private final PartitioningStrategy strategy;
		
		Holder(final Comparable[] array, PartitioningStrategy strategy) {
			this.array = array;
			helpArray = new Comparable[this.array.length];
			this.strategy = strategy;
		}
	}
	
	private static final int CONCURRENCY_THRESHOLD = 1000;
	
	private final Holder holder;
	private final int left, right;
	
	ParallelMergeSort(final PartitioningStrategy<T> strategy, final T[] array) {	
		this(new Holder(array, strategy), 0, array.length - 1);
	}
	
	ParallelMergeSort(final Holder holder, final int left, final int right ) {
		super();
		this.holder = holder;
		this.left = left;
		this.right = right;
	}
	
	@Override
	protected Boolean compute() {
		
		final int size = this.right - this.left + 1;
		
		
		System.out.println(size);
		
		if(size < 1) {
			return true;
		} else if (size < ParallelMergeSort.CONCURRENCY_THRESHOLD) {
			
			final int pivot = (left + right) / 2;
			
			final MergeSort leftMerge = new MergeSort(this.holder.array, this.holder.helpArray, this.holder.strategy);
			leftMerge.mergeSort(this.left, pivot);
			
			final MergeSort rightMerge = new MergeSort(this.holder.array, this.holder.helpArray, this.holder.strategy);
			rightMerge.mergeSort(pivot + 1, this.right);
			
			merge(pivot);
			
			return true;
		} else {
			
			final int pivot = (left + right) / 2;
			
			ForkJoinTask<Boolean> leftTask = new ParallelMergeSort<T>(this.holder, left, pivot).fork();
			ForkJoinTask<Boolean> rightTask = new ParallelMergeSort<T>(this.holder, pivot + 1, right).fork();
			
			leftTask.join();
			rightTask.join();
			
			merge(pivot);
			
			return true;
		}
	}
	
	private void merge(final int pivot) {
		
		int j = pivot + 1;
		int h = left;
		int i = left;
		
		while ((h <= pivot) && (j <= right)) {
			
			if(holder.array[h].compareTo(holder.array[j]) <= 0) {
				holder.helpArray[i] = holder.array[h];
				h++;
			} else {
				holder.helpArray[i] = holder.array[j];
				j++;
			}
			i++;					
		}
		
		if(h > pivot) {
			for (int k = j; k <= right; k++) {
				holder.helpArray[i] = holder.array[k];
				i++;
			}
		} else {
			for (int k = h;	k <= pivot; k++) {
				holder.helpArray[i] = holder.array[k];
				i++;
			}
		}
		
		for (int k = left; k <= right; k++) {
			holder.array[k] = holder.helpArray[k];
		}
	}	
}
