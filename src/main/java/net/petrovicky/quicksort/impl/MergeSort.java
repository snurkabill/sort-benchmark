package net.petrovicky.quicksort.impl;

public class MergeSort<T extends Comparable<T>> {
	
	private final T[] array;
	private final T[] helpArray;
	private final PartitioningStrategy<T> partitioner;
	
	MergeSort(final T[] array, final T[] helpArray, PartitioningStrategy<T> partitioner) {
		this.array = array;
		this.helpArray = helpArray;
		this.partitioner = partitioner;
	}
	/*
	public void sort(final T[] array) {
		
		long startTime = System.currentTimeMillis();
		
		this.array = array;
		helpArray = new Comparable[this.array.length];	
		
		mergeSort(0, this.array.length - 1);
		
		System.out.println("mergeSort " + (System.currentTimeMillis() - startTime) + "ms");
	}*/
	
	public void mergeSort(final int low, final int high ) {
		
		//int pivot;
	
		if(low < high) {
			final int pivot = this.partitioner.execute(this.array, low, high);
			
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
			
			try { 
			if(array[h].compareTo(array[j]) < 0) {
				helpArray[i] = array[h];
				h++;
			} else {
				helpArray[i] = array[j];
				j++;
			}
			} catch(ArrayIndexOutOfBoundsException e) {
				System.out.println("preteklo nam pole ... a to je jakoze fail :D ");
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
