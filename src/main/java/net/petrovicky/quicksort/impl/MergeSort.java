package net.petrovicky.quicksort.impl;

public class MergeSort<T extends Comparable<T>> {
	
	private final Comparable[] array;
	private final Comparable[] helpArray;
	private PartitioningStrategy<T> partitioner;
	
	public MergeSort(final T[] array, final T[] helpArray, PartitioningStrategy<T> partitioner) {
		this.array = array;
		this.helpArray = helpArray;
		this.partitioner = partitioner;
	}
	
	public MergeSort(final T[] array, final T[] helpArray) {
		this.array = array;
		this.helpArray = helpArray;
	}
	
	public MergeSort(final T[] array){
	
		this.array = array;
		helpArray = new Comparable [array.length];
	}
	
	public void mergeSort(final int low, final int high ) {
		
		if(low < high) {
			final int pivot = (low + high) / 2;
			
			mergeSort(low, pivot);
			mergeSort(pivot + 1, high);
			
			merge(low, pivot, high);
		}
	}
	
	private void merge(int low, int pivot, int high) {
		
		int j = pivot + 1;
		int h = low;
		int i = low;
		
		while ((h <= pivot) && (j <= high)) {
			
			if(array[h].compareTo(array[j]) < 0) {
				helpArray[i] = array[h];
				h++;
			} else {
				helpArray[i] = array[j];
				j++;
			}
			i++;					
		}
		
		if(h > pivot) {
			for (int k = j; k <= high; k++) {
				helpArray[i] = array[k];
				i++;
			}
		} else {
			for (int k = h;	k <= pivot; k++) {
				helpArray[i] = array[k];
				i++;
			}
		}
		
		for (int k = low; k <= high; k++) {
			array[k] = helpArray[k];
		}
	}
}
