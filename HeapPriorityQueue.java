/* 
 * HeapPriorityQueue is an implementation of a priority queue using an array with the behaviors of 
 * a binary min-heap which include adding, removing arbitrary elements, clearing out the elements,
 * peeking at the minimum element, printing out the elements, and checking whether an element is 
 * found in the heap while maintaining the proper vertical ordering of a complete tree. 
 * 
 * This class also has the function of iterating over the heap's elements by using an iterator 
 * that is implemented in a separate class.
 */

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class HeapPriorityQueue<E> implements PriorityQueue<E> {
	private static final int DEFAULT_CAPACITY = 10; // Remembers the default capacity of the array.
	private E[] elements; // Remembers the array of elements.
	private int size; // Remembers the size of the heap.
	private Comparator<E> comp; // Remembers the comparator.

	// Constructs a new HeapPriorityQueue of the default capacity 10.
	public HeapPriorityQueue() {
		this(DEFAULT_CAPACITY, null);
	}
	
	// Accepts a capacity and comparator and constructs a new HeapPriorityQueue with the given 
	// capacity and comparator.
	@SuppressWarnings("unchecked")
	public HeapPriorityQueue(int capacity, Comparator<E> comparator) {
		if (capacity < 2) 
			throw new IllegalArgumentException();
		elements = (E[]) new Object[capacity];
		size = 0;
		comp = comparator;
	}
	
	// Accepts a value and adds it to the heap while maintaining proper vertical ordering.
	// Throws a NullPointerException if the value passed is null.
	public void add(E value) {
		check(value);
		if (size + 1 >= elements.length) // Resize if necessary.
			elements = Arrays.copyOf(elements, 2 * elements.length);
		int index = size + 1;
		elements[index] = value; // put in last slot
		bubbleUp(index);
		size++;
	}
	
	// Clears out all the elements in the heap and resets the size to zero.
	@SuppressWarnings("unchecked")
	public void clear() {
		elements = (E[]) new Object[DEFAULT_CAPACITY];
		size = 0;
	}

	// Accepts a value and returns whether the value is found within the heap.
	// Throws a NullPointerException if the value passed is null.
	public boolean contains(E value) {
		check(value);
		return find(value) != 0;
	}

	// Returns whether the heap is empty.
	public boolean isEmpty() {
		return size == 0;
	}

	// Constructs a new iterator to iterate over the elements in the heap. It is able to access the
	// next element and tell whether there is a next element.
	public Iterator<E> iterator() {
		return new iterator();
	}

	// Returns the first, or the minimum element, in the heap.
	// Throws a NoSuchElementException if the heap is empty.
	public E peek() {
		if (isEmpty())
			throw new NoSuchElementException();
		else 
			return elements[1];
	}

	// Returns and removes the first, or the minimum element, in the heap while maintaining proper
	// vertical ordering.
	// Throws a NoSuchElementException if the heap is empty.
	public E remove() {
		if (isEmpty())
			throw new NoSuchElementException();
		E result = elements[1];
		elements[1] = elements[size];
		elements[size] = null;
		size--;
		bubbleDown();
		return result;
	}

	// Receives a value and returns it from the heap while maintaining proper vertical ordering.
	// Throws a NullPointerException if the value passed is null. 
	public void remove(E value) {
		check(value);
		int index = find(value);
		if (index != 0) {
			justBubbleUp(index);
			remove();
		}
	}
	
	// Returns the size of the array.
	public int size() {
		return size;
	}
	
	// Returns a string representation of the elements in the heap.
	public String toString() {
		String heap = "[";
		if (size >= 1) {
			heap += elements[1];
			for (int i = 2; i <= size; i++) 
				heap += ", " + elements[i];
		}
		heap += "]";
		return heap;
	}
	
	// Private helper method that receives an item and throws a NullPointerException if it is null.
	private void check(E item) {
		if (item == null)
			throw new NullPointerException();
	}
	
	// Private helper method the receives a value and returns its index in the array.
	private int find(E value) {
		for (int i = 1; i <= size(); i++) {
			if (value.equals(elements[i]))
				return i;
		}
		return 0;
	}
	
	// Private helper method that receives an index and "bubbles up" to restore proper ordering.
	@SuppressWarnings("unchecked")
	private void bubbleUp(int index) {
		int comparator;
		boolean done = false;
		while (!done && hasParent(index)) { 
			int daddy = parent(index);
			if (comp == null) 
				comparator = ((Comparable<E>) elements[daddy]).compareTo(elements[index]);
			else  // A comparator was passed to use.
				comparator = comp.compare(elements[daddy], elements[index]);
			if (comparator > 0) {
				swap(index, daddy);
				index = daddy;
			} else 
				done = true;
		}
	}
	
	// Private helper method that receives an index and just "bubbles up" to the root of the heap,
	// disregarding proper ordering. 
	private void justBubbleUp(int index) {
		boolean done = false;
		while (!done && hasParent(index)) { 
			int daddy = parent(index);
			swap(index, daddy);
			index = daddy;
		}
	}
	
	// Private helper method that receives an index and "bubbles down" to restore proper ordering. 
	@SuppressWarnings("unchecked")
	private void bubbleDown() {
		int comparator;
		boolean done = false;
		int index = 1;
		while (!done && hasLeftChild(index)) { 
			int left = leftChild(index);
			int kiddo = left;
			if (comp == null) {
				comparator = ((Comparable<E>) elements[index]).compareTo(elements[kiddo]);
			} else { // A comparator was passed to use.
				comparator = comp.compare(elements[index], elements[kiddo]);
			}
			if (hasRightChild(index)) {
				int right = rightChild(index);
				if (comparator < 0)
					kiddo = right;
			}
			if (comparator > 0) {
				swap(index, kiddo);
				index = kiddo;
			} else 
				done = true;
		}
	}

	// Private helper method that accepts an index and returns the index of its parent.
	private int parent(int index) {
		return index / 2;
	}
	
	// Private helper method that accepts an index and returns the index of its left child.
	private int leftChild(int index) {
		return index * 2;
	}

	// Private helper method that accepts an index and returns the index of its right child.
	private int rightChild(int index) {
		return index * 2 + 1;
	}

	// Private helper method that accepts an index and returns whether there it has a parent.
	private boolean hasParent(int index) {
		return index > 1;
	}

	// Private helper method that accepts an index and returns whether there it has a left child.
	private boolean hasLeftChild(int index) {
		return leftChild(index) <= size;
	}

	// Private helper method that accepts an index and returns whether there it has a right child.
	private boolean hasRightChild(int index) {
		return rightChild(index) <= size;
	}
	
	// Private helper method that accepts two indexes in an array and swaps their position.
	private void swap(int index1, int index2) {
		E temp = elements[index1];
		elements[index1] = elements[index2];
		elements[index2] = temp;
	}
	
	// Private class that implements the iterator interface to iterate over the heap's elements.
	private class iterator implements Iterator<E> {
		private int index; // Remembers the index of the iterator.
		
		// Sets the iterator at the beginning of the heap.
		public iterator() {
			index = 1;
		}
		
		// Returns whether the iterator has a next element to examine.
		public boolean hasNext() {
			return index <= size;
		}
		
		// Returns the next element the iterator is examining.
		public E next() {
			E item = elements[index];
			index++;
			return item;
		}
		
		// Throws an UnsupportedOperationException when this method is called.
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}
