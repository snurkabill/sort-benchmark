package net.petrovicky.quicksort.impl;

import java.util.ArrayList;

public class RadixSort {
	
	private final int base;
	private Integer[] array;
	private ArrayList<Integer>[] helpArray; 
	private Integer largestOne;
	
	public RadixSort() {
		this(10);
	}
	
	public RadixSort(int base) {
		this.base = base;
		this.largestOne = 1;
		helpArray = new ArrayList [base];
		
		for (int i = 0; i < base; i++) {
			helpArray[i] = new ArrayList<>();
		}		
	}
	
	public void sort(Integer[] array) {
		this.array = array;
		int exp = 1;
		
		for (; largestOne / exp > 0; ) {			
			for (int i = 0; i < array.length; i++) {
				helpArray[(array[i] / exp) % base].add(array[i]);
				if(array[i] > largestOne)
					largestOne = array[i];
			}
		
			int index = 0;
			for (int i = 0; i < base; i++) {
				for (int j = 0; j < helpArray[i].size(); j++) {
					array[index + j] = helpArray[i].get(j);
				}
				index += helpArray[i].size();
			}

			for (int i = 0; i < base; i++) {
				helpArray[i].clear();
			}
			exp *= 10;
		}
	}
}
