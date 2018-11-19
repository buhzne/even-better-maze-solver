// This interface defines an ADT for priority queues of integers
// that allow fast access/removal of the minimum element.

import java.util.Iterator;

/**
 * Interface PriorityQueue represents an abstract data type (ADT) for a map storing
 * a queue of elements that can be accessed/removed in ascending order,
 * giving fast access to the minimum element.
 * 
 * Classes implementing PriorityQueue are generally expected to provide the operations
 * below in O(1) or O(log N) average runtime, except for the clear, contains,
 * and toString operations, which are often O(N).
 */
public interface PriorityQueue<E> extends Iterable<E> {
	
	// Accepts a value and adds it to the heap while maintaining proper vertical ordering.
	void add(E value);

	// Clears out all the elements in the heap and resets the size to zero.
	void clear();

	// Accepts a value and returns whether the value is found within the heap.
	boolean contains(E value);

	// Returns whether the heap is empty.
	boolean isEmpty();

	// Constructs a new iterator to iterate over the elements in the heap. It is able to access the
	// next element and tell whether there is a next element.
	Iterator<E> iterator();

	// Returns the first, or the minimum element, in the heap.
	E peek();

	// Returns and removes the first, or the minimum element, in the heap while maintaining proper
	// vertical ordering.
	E remove();

	// Receives a value and returns it from the heap while maintaining proper vertical ordering.
	void remove(E value);

	// Returns the size of the array.
	int size();
	
	// Returns a string representation of the elements in the heap.
	String toString();
}
