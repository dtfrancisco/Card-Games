package cardGames;

/**
 * Constructs a queue that can add to the rear of the queue, and remove, and 
 * lookup items at the start of the queue using a circular array.
 *
 */

public class SimpleQueue<E> implements QueueADT<E>{

	private static final int INITSIZE = 10;  // initial array size
	private E[] items; // the items in the queue
	private int numItems;   // the number of items in the queue
	private int frontIndex; // index at front of the queue
	private int rearIndex; // index at rear of the queue

	/**
	 * Sets items to an empty array initial of size 10, sets numItems to zero,
	 * and sets both the frontIndex and rearIndex to -1.
	 */

	public SimpleQueue() { 
		items = (E[]) new Object [INITSIZE];
		numItems = 0;
		frontIndex = -1; // set to zero in enqueue
		rearIndex = -1; // increments by 1 each time enqueue is called
	}  

	/**
	 * Adds an item to the end of the queue. If the queue is full, then the
	 * array size is doubled.
	 * @param ob the object to add to the queue
	 * @throws IllegalArgumentException if ob is null
	 */

	public void enqueue(E ob) {

		if (ob == null) {
			throw new IllegalArgumentException();
		}

		// check for full array and expand if necessary
		if (items.length == numItems) {
			E[] tmp = (E[])(new Object[items.length*2]);
			System.arraycopy(items, frontIndex, tmp, frontIndex,
					items.length-frontIndex);
			if (frontIndex != 0) {
				System.arraycopy(items, 0, tmp, items.length, frontIndex);
			}
			items = tmp;
			frontIndex = 0; // set front index back to zero
			rearIndex = frontIndex + numItems - 1;		
		}
		if (frontIndex == -1) {
			frontIndex = incrementIndex(frontIndex); // sets frontIndex to zero
		}
		// use auxiliary method to increment rear index with wraparound
		rearIndex = incrementIndex(rearIndex);
		// insert new item at rear of queue
		items[rearIndex] = ob;
		numItems++;
	}


	/**
	 * Increments the index of either the frontIndex or rearIndex. Index resets
	 * to zero if index is the last index in the array.
	 * @param index the index to be incremented
	 * @return the new index after being incremented
	 * @throws IndexOutOfBoundsException if index is less than -1, the value
	 * set in the constructor or greater than or equal to the length of the items 
	 */

	private int incrementIndex(int index) {
		if (index < -1 || index >= items.length) {
			throw new IndexOutOfBoundsException();
		}

		if (index == items.length-1) {
			return 0;
		}
		else {
			return index + 1;
		}
	}

	/**
	 * Removes the item at the front of the queue 
	 * @return temp the object that was removed from the queue
	 * @throws EmptyQueueException if the queue is empty
	 */

	public E dequeue() throws EmptyQueueException {

		if (isEmpty()) {
			throw new EmptyQueueException();
		}

		E temp = items[frontIndex];
		frontIndex = incrementIndex(frontIndex);
		numItems--;
		// sets indexes back to original values if no items are left in the queue
		if (numItems == 0) {
			frontIndex = -1;
			rearIndex = -1;
		}
		return temp;
	}

	/**
	 * Looks at the item at the front of the queue without removing it
	 * @return temp the object that is at the front of the queue
	 * @throws EmptyQueueException if the queue is empty
	 */

	public E peek() throws EmptyQueueException {
		if (isEmpty()) {
			throw new EmptyQueueException();
		}
		E temp = items[frontIndex];
		return temp;
	}

	/**
	 * Checks to see if the queue is empty
	 * return true if numItems is zero, false otherwise
	 */

	public boolean isEmpty() {
		return numItems == 0; 
	}


	/**
	 * Returns the number of items in the queue
	 * @return numItems the number of items enqueued
	 */
	public int size() {
		return numItems;
	}  

	/**
	 * Returns the string representation of the queue object.
	 * @return str the array of the queue from the front to the rear
	 */

	public String toString() {
		String str = "";
		for (int i = frontIndex; i <= rearIndex; i++) {
			str += items[i]+"\n";
		}
		return str;
	}
}


